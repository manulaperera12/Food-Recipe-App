package com.example.android.myapplication.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.myapplication.entities.Meal
import com.example.android.myapplication.R
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class RecyclerAdapter(val context: Context, private val meals: List<Meal>): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        // Declare variables for views in grid item layout
        val mealTopic: TextView = itemView.findViewById(R.id.meal)
        val mealInstructionTitle: TextView = itemView.findViewById(R.id.mealInstructionTitle)
        val drinkAlternate: TextView = itemView.findViewById(R.id.drinkAlternate)
        val category: TextView = itemView.findViewById(R.id.category)
        val area: TextView = itemView.findViewById(R.id.area)
        val mealInstruction: TextView = itemView.findViewById(R.id.mealInstruction)
        val tags: TextView = itemView.findViewById(R.id.tags)
        val youtube: TextView = itemView.findViewById(R.id.youtube)
        val ingredient: TextView = itemView.findViewById(R.id.ingredient)
        val measure: TextView = itemView.findViewById(R.id.measure)
        val mealThumb: ImageView = itemView.findViewById(R.id.mealThumb)
        val ingredientsTitle: TextView = itemView.findViewById(R.id.ingredientsTitle)
        val measureTitle: TextView = itemView.findViewById(R.id.measureTitle)


    }

    override fun getItemCount(): Int {
        return meals.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        // Inflate grid item layout and return ViewHolder
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.grid_item, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // Get meal at current position and bind data to views
        val meal = meals[position]
        with(holder) {

            // Load image from URL and set it to mealThumb ImageView
            val url = "${meal.mealThumb}"
            loadImageFromUrl(url, mealThumb)

            // Set text to TextViews using meal object properties
            mealTopic.text = "${meal.mealName}"
            drinkAlternate.text = "\"DrinkAlternate\" : ${meal.drinkAlternate}"
            category.text = "\"Category\" : \"${meal.category}\""
            area.text = "\"Area\" : \"${meal.area}\""
            mealInstructionTitle.text = "Instructions"
            mealInstruction.text = "${meal.instructions}....\n"
            tags.text = "\"Tags\" : \"${meal.tags}\""
            youtube.text = "\"Youtube\" : \"${meal.youtube}\""

            // Set title and list of ingredients to ingredient TextView
            ingredientsTitle.text = "Ingredients"
            val ingredientStringBuilder = StringBuilder()
            meal.ingredients.forEachIndexed { index, ingredient ->
                if (ingredient != "null") {
                    ingredientStringBuilder.append("\"Ingredient${index + 1}\" : \"${ingredient}\"")
                    if (index < meal.ingredients.size - 1) {
                        ingredientStringBuilder.append("\n")
                    }
                }
            }
            ingredient.text = ingredientStringBuilder.toString()

            // Set title and list of measures to measure TextView
            measureTitle.text = "Measures"
            val measureStringBuilder = StringBuilder()
            meal.measures.forEachIndexed { index, measure ->
                if (measure != "null") {
                    measureStringBuilder.append("\"Measure${index + 1}\" : \"${measure}\"")
                    if (index < meal.measures.size - 1) {
                        measureStringBuilder.append("\n")
                    }
                }
            }
            measure.text = measureStringBuilder.toString()
        }
    }

    // Function to load image from URL and set it to ImageView
    fun loadImageFromUrl(url: String, imageView: ImageView) {
        val thread = Thread {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input = connection.inputStream
                val bitmap = BitmapFactory.decodeStream(input)
                imageView.post {
                    imageView.setImageBitmap(bitmap)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        thread.start()
    }
}