package com.example.savr

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavoritesActivity : ComponentActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var recipeAdapter: RecipeFilterAdapter
    private lateinit var noFavoritesTextView: TextView
    private lateinit var favoritesManager: FavoritesManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.favorites)

        // Initialize FavoritesManager
        favoritesManager = FavoritesManager.getInstance(this)

        val home1Button = findViewById<Button>(R.id.button2)
        home1Button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val Me2Button = findViewById<Button>(R.id.button5)
        Me2Button.setOnClickListener {
            val intent = Intent(this, MeActivity::class.java)
            startActivity(intent)
        }

        val pantryButton = findViewById<Button>(R.id.button6) // "Pantry" button
        pantryButton.setOnClickListener {
            val intent = Intent(this, PantryActivity::class.java)
            startActivity(intent)
        }

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerViewFavorites)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set up no favorites text
        noFavoritesTextView = findViewById(R.id.noFavoritesText)

        // Load favorite recipes
        loadFavorites()
    }

    override fun onResume() {
        super.onResume()
        // Reload favorites when the activity is resumed
        // This ensures the list is updated if changes were made in other activities
        loadFavorites()
    }

    private fun loadFavorites() {
        val favorites = favoritesManager.getFavorites()

        if (favorites.isEmpty()) {
            recyclerView.visibility = View.GONE
            noFavoritesTextView.visibility = View.VISIBLE
            noFavoritesTextView.text = "You haven't added any favorite recipes yet."
        } else {
            recyclerView.visibility = View.VISIBLE
            noFavoritesTextView.visibility = View.GONE

            // Create adapter with favorite recipes
            recipeAdapter = RecipeFilterAdapter(favorites)
            recyclerView.adapter = recipeAdapter
        }
    }
}