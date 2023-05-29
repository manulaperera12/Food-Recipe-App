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
import com.example.android.myapplication.entities.Meal

class OnlineSearchActivity : AppCompatActivity() {
    // Declare variables
    private lateinit var viewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchOnline: Button
    private lateinit var mealET: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_online_search)
        recyclerView = findViewById(R.id.recyclerView)

        // Initialize ViewModel and observe search results
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.searchResultsOnline.observe(this, Observer {
            // Create adapter and set it to RecyclerView
            val adapter = RecyclerAdapter(this, it)
            recyclerView.adapter = adapter
        })

        searchOnline = findViewById(R.id.searchOnline)
        mealET = findViewById(R.id.mealET)

        // Set click listener for search button
        searchOnline.setOnClickListener{
            // Call validateTextView function with mealET as argument
            validateTextView(mealET)

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
            // Display toast with "Invalid Input" message
            val myToast = Toast.makeText(this, "Invalid Input", Toast.LENGTH_SHORT)
            val toastView = layoutInflater.inflate(R.layout.toast_layout, null)
            val toastText = toastView.findViewById<TextView>(R.id.toast_text)
            toastText.text = "Invalid Input"
            myToast.view = toastView
            myToast.show()
            return false
        } else {
            // If input text is valid and not empty, call ViewModel function to get meals using name
            if (inputText.isNotEmpty()){
                viewModel.getMealsUsingName(inputText)
            }
        }
        return true
    }
}