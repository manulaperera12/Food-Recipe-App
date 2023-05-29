package com.example.android.myapplication.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.example.android.myapplication.R
import com.example.android.myapplication.dao.Dao
import com.example.android.myapplication.database.MealDatabase
import com.example.android.myapplication.entities.Meal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Define the MainActivity class, which extends the AppCompatActivity class
class MainActivity : AppCompatActivity() {

    // Declare variables for the UI components and the DAO
    private lateinit var addMeals:Button
    private lateinit var mealDao: Dao

    private lateinit var searchIngredient:Button
    private lateinit var searchMeals:Button
    private lateinit var onlineSearch:Button

    private lateinit var videoView: VideoView
    private var currentPosition: Int = 0
    private lateinit var mealDatabase: MealDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the VideoView and set its properties
        videoView = findViewById(R.id.videoView)
        videoView.setVideoPath("android.resource://" + packageName + "/" + R.raw.bg_video)


        // Set a listener to prepare the video player, loop the video, seek to the saved position, and start playing the video
        videoView.setOnPreparedListener { player ->
            player.isLooping = true
            player.seekTo(currentPosition)
            player.start()
        }




        // Initialize the UI components by finding their IDs and setting click listeners
        addMeals = findViewById(R.id.addMeals)
        addMeals.setOnClickListener{
            // Initialize the database instance
            mealDatabase = MealDatabase.getDatabase(this)

            // Call the function to add meals to the database
            addMealsToDatabase()
        }


        searchIngredient = findViewById(R.id.searchIngredients)
        searchIngredient.setOnClickListener{
            val intent = Intent(this, SearchIngredientsActivity::class.java)
            startActivity(intent)
        }

        searchMeals = findViewById(R.id.searchMeals)
        searchMeals.setOnClickListener{
            val intent = Intent(this, SearchMealsActivity::class.java)
            startActivity(intent)
        }

        onlineSearch = findViewById(R.id.onlineSearch)
        onlineSearch.setOnClickListener{
            val intent = Intent(this, OnlineSearchActivity::class.java)
            startActivity(intent)
        }
    }

    // Override the onPause() method to pause the video player and save the current position
    override fun onPause() {
        super.onPause()
        videoView.pause()
        currentPosition = videoView.currentPosition
    }

    // Override the onResume() method to seek to the saved position and start playing the video again
    override fun onResume() {
        super.onResume()
        videoView.seekTo(currentPosition)
        videoView.start()
    }



    private fun addMealsToDatabase() {
        val meals = listOf(
            createMeal1(),
            createMeal2(),
            createMeal3(),
            createMeal4()
        )

        val appScope= CoroutineScope(Dispatchers.Main)

        // Use a coroutine to perform database operations in the background
        appScope.launch {
            withContext(Dispatchers.IO) {
                // Insert the meals into the database
                mealDatabase.dao().insertMeals(meals)
            }

            // Fetch all meals from the database to verify insertion
            val fetchedMeals = withContext(Dispatchers.IO) {
                mealDatabase.dao().getAll()
            }

            // Log the fetched meals
            for (meal in fetchedMeals) {
                Log.d("MainActivity", meal.toString())
            }
            Toast.makeText(this@MainActivity, "Meals Added", Toast.LENGTH_SHORT).show()

        }
    }

    private fun createMeal1(): Meal {
        return Meal(
            idMeal = "1",
            mealName = "Sweet and Sour Pork",
            drinkAlternate = null,
            category = "Pork",
            area = "Chinese",
            instructions = "Preparation...\nCooking instructions...",
            mealThumb = "https://www.themealdb.com/images/media/meals/1529442316.jpg",
            tags = "Sweet",
            youtube = "https://www.youtube.com/watch?v=mdaBIhgEAMo",
            ingredients = listOf("Pork", "Egg", "Water", "Salt", "Sugar", "Soy Sauce", "Starch", "Tomato Puree", "Vinegar", "Coriander"),
            measures = listOf("200g", "1", "Dash", "1/2 tsp", "1 tsp", "10g", "10g", "30g", "10g", "Dash"),
            source = null,
            imageSource = null,
            creativeCommonsConfirmed = null,
            modified = null
        )
    }

    private fun createMeal2(): Meal {
        return Meal(
            idMeal = "2",
            mealName = "Chicken Marengo",
            drinkAlternate = null,
            category = "Chicken",
            area = "French",
            instructions = "Cooking instructions...",
            mealThumb = "https://www.themealdb.com/images/media/meals/qpxvuq1511798906.jpg",
            tags = null,
            youtube = "https://www.youtube.com/watch?v=U33HYUr-0Fw",
            ingredients = listOf("Olive Oil", "Mushrooms", "Chicken Legs", "Passata", "Chicken Stock Cube", "Black Olives", "Parsley"),
            measures = listOf("1 tbs", "300g", "4", "500g", "1", "100g", "Chopped"),
            source = "https://www.bbcgoodfood.com/recipes/3146682/chicken-marengo",
            imageSource = null,
            creativeCommonsConfirmed = null,
            modified = null
        )
    }

    private fun createMeal3(): Meal {
        return Meal(
            idMeal = "3",
            mealName = "Beef Banh Mi Bowls with Sriracha Mayo, Carrot & Pickled Cucumber",
            drinkAlternate = null,
            category = "Beef",
            area = "Vietnamese",
            instructions = "Additional ingredients: mayonnaise, sriracha\n\n1. Place rice in a fine-mesh sieve and rinse until water runs clear. Add to a small pot with 1 cup water (2 cups for 4 servings) and a pinch of salt. Bring to a boil, then cover and reduce heat to low. Cook until rice is tender, 15 minutes. Keep covered off heat for at least 10 minutes or until ready to serve.\n\n2. Meanwhile, wash and dry all produce. Peel and finely chop garlic. Zest and quarter lime (for 4 servings, zest 1 lime and quarter both). Trim and halve cucumber lengthwise; thinly slice crosswise into half-moons. Halve, peel, and medium dice onion. Trim, peel, and grate carrot.\n\n3. In a medium bowl, combine cucumber, juice from half the lime, ¼ tsp sugar (½ tsp for 4 servings), and a pinch of salt. In a small bowl, combine mayonnaise, a pinch of garlic, a squeeze of lime juice, and as much sriracha as you'd like. Season with salt and pepper.\n\n4. Heat a drizzle of oil in a large pan over medium-high heat. Add onion and cook, stirring, until softened, 4-5 minutes. Add beef, remaining garlic, and 2 tsp sugar (4 tsp for 4 servings). Cook, breaking up meat into pieces, until beef is browned and cooked through, 4-5 minutes. Stir in soy sauce. Turn off heat; taste and season with salt and pepper.\n\n5. Fluff rice with a fork; stir in lime zest and 1 TBSP butter. Divide rice between bowls. Arrange beef, grated carrot, and pickled cucumber on top. Top with a squeeze of lime juice. Drizzle with sriracha mayo.",
            mealThumb = "https://www.themealdb.com/images/media/meals/z0ageb1583189517.jpg",
            tags = null,
            youtube = "",
            ingredients = listOf("Rice", "Onion", "Lime", "Garlic Clove", "Cucumber", "Carrots", "Ground Beef", "Soy Sauce"),
            measures = listOf("White", "1", "1", "3", "1", "3 oz", "1 lb", "2 oz"),
            source = "",
            imageSource = null,
            creativeCommonsConfirmed = null,
            modified = null
        )
    }

    private fun createMeal4(): Meal {
        return Meal(
            idMeal = "4",
            mealName = "Leblebi Soup",
            drinkAlternate = null,
            category = "Vegetarian",
            area = "Tunisian",
            instructions = "Heat the oil in a large pot. Add the onion and cook until translucent.\\r\\nDrain the soaked chickpeas and add them to the pot together with the vegetable stock. Bring to the boil, then reduce the heat and cover. Simmer for 30 minutes.\\r\\nIn the meantime toast the cumin in a small ungreased frying pan, then grind them in a mortar. Add the garlic and salt and pound to a fine paste.\\r\\nAdd the paste and the harissa to the soup and simmer until the chickpeas are tender, about 30 minutes.\\r\\nSeason to taste with salt, pepper and lemon juice and serve hot.",
            mealThumb = "https://www.themealdb.com/images/media/meals/x2fw9e1560460636.jpg",
            tags = "Soup",
            youtube = "",
            ingredients = listOf("Olive Oil", "Onion", "Chickpeas", "Vegetable Stock", "Cumin", "Garlic", "Salt", "Harissa Spice", "Pepper", "Lime" ),
            measures = listOf("2 tbs", "1 medium finely diced", "250g", "1.5L", "1 tsp", "5 cloves", "1\\/2 tsp", "1 tsp", "Pinch", "1\\/2"),
            source = "",
            imageSource = null,
            creativeCommonsConfirmed = null,
            modified = null
        )
    }


}