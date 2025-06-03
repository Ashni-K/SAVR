package com.example.savr

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Singleton class to manage favorite recipes
class FavoritesManager private constructor(context: Context) {
    private val PREFS_NAME = "savr_favorites"
    private val KEY_FAVORITES = "favorite_recipes"
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    // Get all favorite recipes
    fun getFavorites(): List<Recipe> {
        val json = prefs.getString(KEY_FAVORITES, "[]")
        val type = object : TypeToken<List<Recipe>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    // Add a recipe to favorites
    fun addFavorite(recipe: Recipe) {
        val favorites = getFavorites().toMutableList()
        // Check if the recipe is already in favorites
        if (!favorites.any { it.title == recipe.title }) {
            favorites.add(recipe)
            saveFavorites(favorites)
        }
    }

    // Remove a recipe from favorites
    fun removeFavorite(recipe: Recipe) {
        val favorites = getFavorites().toMutableList()
        favorites.removeIf { it.title == recipe.title }
        saveFavorites(favorites)
    }

    // Check if a recipe is in favorites
    fun isFavorite(recipe: Recipe): Boolean {
        return getFavorites().any { it.title == recipe.title }
    }

    // Save favorites to SharedPreferences
    private fun saveFavorites(favorites: List<Recipe>) {
        val json = gson.toJson(favorites)
        prefs.edit().putString(KEY_FAVORITES, json).apply()
    }

    companion object {
        @Volatile
        private var instance: FavoritesManager? = null

        fun getInstance(context: Context): FavoritesManager {
            return instance ?: synchronized(this) {
                instance ?: FavoritesManager(context.applicationContext).also { instance = it }
            }
        }
    }
}