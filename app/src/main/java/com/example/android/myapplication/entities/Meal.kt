package com.example.android.myapplication.entities


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.android.myapplication.entities.converter.ListTypeConverter

@Entity(tableName = "Meals")

// Use the ListTypeConverter class to convert the ingredients and measures fields to and from a String
@TypeConverters(ListTypeConverter::class)
data class Meal (

    // Declare the entity fields and annotate the primary key
    @PrimaryKey
    val idMeal: String, // Primary key for the Meal entity
    val mealName: String,
    val drinkAlternate: String?,
    val category: String?,
    val area: String?,
    val instructions: String?,
    val mealThumb: String?,
    val tags: String?,
    val youtube: String?,
    val ingredients: List<String>,  // List of ingredients for the meal
    val measures: List<String>,    // List of corresponding measures for the ingredients
    val source: String?,
    val imageSource: String?,
    val creativeCommonsConfirmed: String?,
    val modified: String?,
)

//class RetrieveMealsTask(private val ingredient: String, private val callback: (List<Meal>?) -> Unit) :
//    AsyncTask<Void, Void, List<Meal>?>() {
//
//    override fun doInBackground(vararg params: Void?): List<Meal>? {
//        var meals: List<Meal>? = null
//        try {
//            val url = URL("https://www.themealdb.com/api/json/v1/1/filter.php?i=$ingredient")
//            val connection = url.openConnection() as HttpURLConnection
//            connection.requestMethod = "GET"
//            connection.connect()
//
//            val responseCode = connection.responseCode
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                val bufferedReader = BufferedReader(InputStreamReader(connection.inputStream))
//                val stringBuilder = StringBuilder()
//                var line: String? = null
//                while ({ line = bufferedReader.readLine(); line }() != null) {
//                    stringBuilder.append(line)
//                }
//                bufferedReader.close()
//
//                val jsonObject = JSONObject(stringBuilder.toString())
//                val jsonArray = jsonObject.getJSONArray("meals")
//                meals = mutableListOf()
//                for (i in 0 until jsonArray.length()) {
//                    val mealObject = jsonArray.getJSONObject(i)
//                    val mealId = mealObject.getString("idMeal")
//                    val mealDetailsResponse =
//                        URL("https://www.themealdb.com/api/json/v1/1/lookup.php?i=$mealId").readText()
//                    val mealDetails = JSONObject(mealDetailsResponse).getJSONArray("meals").getJSONObject(0)
//                    val id = mealDetails.getString("idMeal").toInt()
//                    val name = mealDetails.getString("strMeal")
//                    val category = mealDetails.getString("strCategory")
//                    val area = mealDetails.getString("strArea")
//                    val instructions = mealDetails.getString("strInstructions")
//                    val tags = mealDetails.optString("strTags", "")
//                    val youtubeLink = mealDetails.optString("strYoutube", "")
//                    val mealThumb = mealDetails.optString("strMealThumb", "")
//                    val modified = ""
//                    val imageSource = ""
//                    val creativeCommonsConfirmed = ""
//                    val drinkAlternate = ""
//                    val source = ""
//
//                    val ingredients = mutableListOf<String>()
//                    val measures = mutableListOf<String>()
//                    for (j in 1..20) {
//                        val ingredient = mealDetails.optString("strIngredient$j", "")
//                        if (ingredient.isNotEmpty()) {
//                            ingredients.add(ingredient)
//                            measures.add(mealDetails.optString("strMeasure$j", ""))
//                        }
//                    }
//                    val meal = Meal(
//                        idMeal="$id",
//                        mealName = name,
//                        category = category,
//                        area = area,
//                        instructions = instructions,
//                        tags = tags,
//                        youtube = youtubeLink,
//                        measures = measures,
//                        ingredients = ingredients,
//                        mealThumb = mealThumb,
//                        source = source,
//                        modified = modified,
//                        imageSource = imageSource,
//                        creativeCommonsConfirmed = creativeCommonsConfirmed,
//                        drinkAlternate = drinkAlternate
//                    )
//                    meals.add(meal)
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return meals
//    }
//
//    override fun onPostExecute(result: List<Meal>?) {
//        callback(result)
//    }
//}

//class ListStringConverter {
//    @TypeConverter
//    fun fromListString(value: List<String>?): String? {
//        return value?.joinToString(",")
//    }
//
//    @TypeConverter
//    fun toListString(value: String?): List<String>? {
//        return value?.split(",")?.map { it.trim() }
//    }
//}


