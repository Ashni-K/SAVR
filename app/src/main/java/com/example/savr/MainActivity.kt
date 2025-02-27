package com.example.savr

import android.os.Bundle
import androidx.activity.ComponentActivity

import android.content.Intent
import android.widget.Button

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homescreen)

        val pantryButton = findViewById<Button>(R.id.button6) // "Pantry" button
        pantryButton.setOnClickListener {
            val intent = Intent(this, PantryActivity::class.java)
            startActivity(intent)
            //setContent {
            //  SAVRTheme {
            // A surface container using the 'background' color from the theme
            //   Surface(
            //     modifier = Modifier.fillMaxSize(),
            //       color = MaterialTheme.colorScheme.background,
            //) {
            //      BakingScreen()
            //  }
            //}
        }
        val recipesButton = findViewById<Button>(R.id.button4) // "Pantry" button
        recipesButton.setOnClickListener {
            val intent = Intent(this, Recipes::class.java)
            startActivity(intent)


    }

}



