package com.example.savr

import android.os.Bundle
import androidx.activity.ComponentActivity

import android.content.Intent
import android.widget.Button

class FavoritesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.favorites)

        val homeButton = findViewById<Button>(R.id.button2)
        homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val pantry1Button = findViewById<Button>(R.id.button6)
        pantry1Button.setOnClickListener {
            val intent = Intent(this, PantryActivity::class.java)
            startActivity(intent)
        }


    }
}

