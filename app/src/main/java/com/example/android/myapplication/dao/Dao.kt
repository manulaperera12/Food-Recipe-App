package com.example.android.myapplication.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.myapplication.entities.Meal

@Dao
interface Dao {

    // Define DAO functions for database operations
    @Query("SELECT * FROM Meals")
    fun getAll(): List<Meal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeals(meals: List<Meal>)


    @Query("SELECT * FROM Meals WHERE " +
            "LOWER(mealName) LIKE '%' || LOWER(:keyword) || '%' OR " +
            "LOWER(ingredients) LIKE '%' || LOWER(:keyword) || '%'")
    suspend fun searchMeals(keyword: String): List<Meal>
}