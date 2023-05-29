package com.example.android.myapplication.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.android.myapplication.MainViewModel
import com.example.android.myapplication.R
import com.example.android.myapplication.adapter.RecyclerAdapter

class SearchIngredientsActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var getMeals: Button
    private lateinit var saveToDatabase: Button
    private lateinit var ingredientsET: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_ingredients)

        // Initialize variables and ViewModel
        getMeals = findViewById(R.id.getMeals)
        ingredientsET = findViewById(R.id.ingredientsET)
        saveToDatabase = findViewById(R.id.saveToDatabase)
        recyclerView = findViewById(R.id.recyclerView)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        // Observe search results
        viewModel.searchResultsOnline.observe(this, Observer {
            // Create adapter and set it to RecyclerView
            val adapter = RecyclerAdapter(this,it)
            recyclerView.adapter = adapter
        })
        // Set click listener for "getMeals" button
        getMeals.setOnClickListener{
            // Call validateTextView function with ingredientsET as argument
            validateTextView(ingredientsET)
        }
        // Set click listener for "Save to Database" button
        saveToDatabase.setOnClickListener{
            // Call ViewModel function to save meals to database and display a toast message
            viewModel.saveDatabase()
            Toast.makeText(this, "Meals added to the database", Toast.LENGTH_SHORT).show()

        }
    }

    // Function to validate input text
    private fun validateTextView(textView: TextView): Boolean {

        // Convert input text to lowercase and remove leading/trailing white spaces
        val inputText = textView.text.toString().lowercase().trim()
        // Define filter to allow only letters in input text
        val letterFilter = InputFilter { source, start, end, dest, dstart, dend ->
            val sb = StringBuilder()
            for (i in start until end) {
                val c = source[i]
                if (Character.isLetter(c)) {
                    sb.append(c)
                }
            }
            sb.toString()
        }
        // Apply letter filter to text view
        textView.filters = arrayOf(letterFilter)
        // Check if input text is valid
        if (inputText.matches(Regex(".*\\d+.*")) || !inputText.matches(Regex("[a-zA-Z]+"))) {
            val myToast = Toast.makeText(this, "Invalid Input", Toast.LENGTH_SHORT)
            val toastView = layoutInflater.inflate(R.layout.toast_layout, null)
            val toastText = toastView.findViewById<TextView>(R.id.toast_text)
            toastText.text = "Invalid Input"
            myToast.view = toastView
            myToast.show()
//            Toast.makeText(textView.context, "Please enter only letters", Toast.LENGTH_SHORT).show()
            return false
        }else{
            // If input text is valid and not empty, call ViewModel function to get meals using ingredient
            if (inputText.isNotEmpty()){
                viewModel.getMealsUsingIngredient(inputText)
            }
        }
        return true
    }
}
