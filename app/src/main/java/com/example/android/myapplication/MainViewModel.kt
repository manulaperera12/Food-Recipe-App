package com.example.android.myapplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.android.myapplication.repository.Repository

class MainViewModel( application: Application):AndroidViewModel(application) {
    private val repository = Repository(application)

    // MutableLiveData objects that can be observed in the UI
    val searchResultsOnline = repository.searchResultsOnline
    val searchResults = repository.searchResults
    val app:Application = application

    // Get meals that use a specific ingredient from the API
    fun getMealsUsingIngredient(keyword: String){ repository.getMealsUsingIngredient(keyword) }

    // Save the meals obtained from the API to the local database
    fun saveDatabase(){ repository.saveMealsToDatabase() }

    // Search for meals by keyword in the local database
    fun searchMeal(keyword: String){ repository.searchMeal(keyword) }

    // Search for meals by name in the API
    fun getMealsUsingName(keyword : String){ repository.getMealsUsingName(keyword) }
}