package com.example.android.myapplication.repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.android.myapplication.database.MealDatabase
import com.example.android.myapplication.entities.Meal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class Repository(application: Application) {
    val searchResultsOnline = MutableLiveData<List<Meal>>()
    val searchResults = MutableLiveData<List<Meal>>()
    private val dao = MealDatabase.getDatabase(application).dao()
    private val meals: MutableList<Meal> = mutableListOf()

    // Search for meals in the local database by keyword
    fun searchMeal(keyword: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val searchResults = withContext(Dispatchers.IO) {
                dao.searchMeals(keyword)
            }
            this@Repository.searchResults.postValue(searchResults)
        }
    }

    // Search for meals that use a specific ingredient by calling the API
    fun getMealsUsingIngredient(keyword: String){
        CoroutineScope(Dispatchers.IO).launch {
            getMealsAPI(keyword)
        }
    }

    // Search for meals byname by calling the API
    fun getMealsUsingName(keyword : String){
        CoroutineScope(Dispatchers.IO).launch {
            getMealsByNameAPI(keyword)
        }
    }

    // Save the meals obtained from the API to the local database
    fun saveMealsToDatabase(){
        CoroutineScope(Dispatchers.Main).launch {
            dao.insertMeals(meals.toList())
        }
    }

    // Call the API to get meals that use a specific ingredient
    suspend fun getMealsAPI(keyword:String){
        val urlString = "https://www.themealdb.com/api/json/v1/1/filter.php?i=$keyword"
        val url = URL(urlString)
        val responseText = suspendCoroutine<String> { continuation ->
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 50000
            connection.readTimeout = 50000
            try {
                connection.inputStream.use { inputStream ->
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val response = StringBuilder()
                    var line: String? = reader.readLine()
                    while (line != null) {
                        response.append(line)
                        line = reader.readLine()
                    }
                    continuation.resume(response.toString())
                }
            } catch (e: Exception) {
                continuation.resumeWithException(e)
            } finally {
                connection.disconnect()
            }
        }
        getMealsID(responseText)
    }

    // Get the meal IDs from the API response and call the API to getthe details of each meal
    private suspend fun getMealsID(mealData :String){
        val json = JSONObject(mealData)
        val jsonArray: JSONArray = json.getJSONArray("meals")
        val mealsIdList: ArrayList<String> = arrayListOf()
        for(i in 0 until jsonArray.length()) {
            val meal: JSONObject = jsonArray[i] as JSONObject
            val mealId = meal.getString("idMeal")
            mealsIdList.add(mealId)
        }
        for(mealId in mealsIdList){
            getMealsByIdAPI(mealId)
        }
    }

    // Call the API to get details of a meal by ID
    suspend fun getMealsByIdAPI(keyword:String){
        val urlString = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=$keyword"
        val url = URL(urlString)
        val responseText = suspendCoroutine<String> { continuation ->
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 50000
            connection.readTimeout = 50000
            try {
                connection.inputStream.use { inputStream ->
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val response = StringBuilder()
                    var line: String? = reader.readLine()
                    while (line != null) {
                        response.append(line)
                        line = reader.readLine()
                    }
                    continuation.resume(response.toString())
                }
            } catch (e: Exception) {
                continuation.resumeWithException(e)
            } finally {
                connection.disconnect()
            }
        }
        parseJSON(responseText)
    }

    // Call the API to get meals by name
    suspend fun getMealsByNameAPI(keyword:String){
        val urlString = "https://www.themealdb.com/api/json/v1/1/search.php?s=$keyword"
        val url = URL(urlString)
        val responseText = suspendCoroutine<String> { continuation ->
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 50000
            connection.readTimeout = 50000
            try {
                connection.inputStream.use { inputStream ->
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val response = StringBuilder()
                    var line: String? = reader.readLine()
                    while (line != null) {
                        response.append(line)
                        line = reader.readLine()
                    }
                    continuation.resume(response.toString())
                }
            } catch (e: Exception) {
                continuation.resumeWithException(e)
            } finally {
                connection.disconnect()
            }
        }
       parseJSON(responseText)
    }

    // Parse the JSON data obtained from the API and create Meal objects
    fun parseJSON(data: String) {
        val json = JSONObject(data)
        val mealsArray = json.getJSONArray("meals")
        for (i in 0 until mealsArray.length()) {
            val mealObj = mealsArray.getJSONObject(i)
            val ingredients = mutableListOf<String>()
            for (j in 1..20) {
                val ingredient = mealObj.optString("strIngredient$j", null)
                ingredients.add(ingredient)
            }
            val measures = mutableListOf<String>()
            for (j in 1..20) {
                val measure = mealObj.optString("strMeasure$j", null)
                measures.add(measure)
            }
            val mealData = Meal(
                mealObj.getString("idMeal"),
                mealObj.getString("strMeal"),
                mealObj.getString("strDrinkAlternate"),
                mealObj.getString("strCategory"),
                mealObj.getString("strArea"),
                mealObj.getString("strInstructions"),
                mealObj.getString("strMealThumb"),
                mealObj.getString("strTags"),
                mealObj.getString("strYoutube"),
                ingredients, measures,
                mealObj.getString("strSource"),
                mealObj.getString("strImageSource"),
                mealObj.getString("strCreativeCommonsConfirmed"),
                mealObj.getString("dateModified")
            )
            meals.add(mealData)
        }
        searchResultsOnline.postValue(meals)
    }
}