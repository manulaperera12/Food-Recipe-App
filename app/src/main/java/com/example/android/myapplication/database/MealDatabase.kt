package com.example.android.myapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.android.myapplication.entities.Meal
import com.example.android.myapplication.dao.Dao

// Define the database schema and version number using the @Database annotation
@Database(entities = [Meal::class], version = 1, exportSchema = false)
abstract class MealDatabase : RoomDatabase() {

    // Declare abstract function for getting DAO
    abstract fun dao(): Dao

    // Create a singleton instance of the database using the companion object
    companion object {
        @Volatile
        private var instance: MealDatabase? = null

        fun getDatabase(context: Context): MealDatabase {

            // If instance is null, create a new instance of the database using Room.databaseBuilder
            if (instance == null) {

                // Synchronize the block to ensure that only one thread can access the code at a time
                synchronized(this) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MealDatabase::class.java,
                        "meals-database.db"
                    ).build()
                }
            }
            return instance!!
        }
    }
}