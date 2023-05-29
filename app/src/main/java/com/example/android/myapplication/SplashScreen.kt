package com.example.android.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.android.myapplication.activities.MainActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Create a Handler to delay the start of the next activity
        Handler(Looper.getMainLooper()).postDelayed({

            // Start the MainActivity and finish the current activity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2500) // Delay for 2.5 seconds (2500 milliseconds)
    }
}