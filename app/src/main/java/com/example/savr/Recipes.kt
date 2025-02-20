package com.example.savr

import android.os.Bundle
import androidx.activity.ComponentActivity

class Recipes : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recipes) // Link to recipes.xml
    }
}