package com.example.savr

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class PantryActivity : ComponentActivity() {

    private val selectedIngredients = mutableListOf<String>()
    private lateinit var pantryAdapter: PantryAdapter
    private lateinit var myPantryTitle: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var seeAllRecipesButton: Button
    private lateinit var clearButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantry)

        clearButton = findViewById(R.id.clearbutton)
        sharedPreferences = getSharedPreferences("PantryPrefs", Context.MODE_PRIVATE)
        loadIngredients() // Load saved ingredients

        val home1Button = findViewById<Button>(R.id.button20)
        home1Button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val recipesButton = findViewById<Button>(R.id.button14)
        recipesButton.setOnClickListener {
            val intent = Intent(this, Recipes::class.java)
            intent.putStringArrayListExtra("SELECTED_INGREDIENTS", ArrayList(selectedIngredients))
            startActivity(intent)
        }

        val clearButton = findViewById<Button>(R.id.clearbutton)
        clearButton.setOnClickListener {
            selectedIngredients.clear() // Clear the list
            pantryAdapter.notifyDataSetChanged() // Update RecyclerView
            saveIngredients() // Clear saved data

            // Hide UI elements since pantry is empty
            myPantryTitle.visibility = View.GONE
            recyclerView.visibility = View.GONE
            seeAllRecipesButton.visibility = View.GONE
            clearButton.visibility = View.GONE

        }

        val favoritesButton = findViewById<Button>(R.id.button22) // "Favorites" button
        favoritesButton.setOnClickListener {
            val intent = Intent(this, FavoritesActivity::class.java)
            startActivity(intent)

        }

        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.autoCompleteSearch)
        myPantryTitle = findViewById(R.id.textViewMyPantry)
        recyclerView = findViewById(R.id.recyclerViewPantry)

        // Initialize the See All Recipes button
        seeAllRecipesButton = findViewById(R.id.button14)
        seeAllRecipesButton.visibility = if (selectedIngredients.isNotEmpty()) View.VISIBLE else View.GONE
        seeAllRecipesButton.setOnClickListener {
            val intent = Intent(this, Recipes::class.java)
            intent.putStringArrayListExtra("SELECTED_INGREDIENTS", ArrayList(selectedIngredients))
            startActivity(intent)
        }

        // Initialize RecyclerView with adapter
        pantryAdapter = PantryAdapter(selectedIngredients)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = pantryAdapter

        // Attach swipe-to-delete functionality
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                // Remove the item from the list
                selectedIngredients.removeAt(position)
                pantryAdapter.notifyItemRemoved(position)

                // Save updated list
                saveIngredients()
                // Hide UI elements if the pantry is now empty
                if (selectedIngredients.isEmpty()) {
                    myPantryTitle.visibility = View.GONE
                    recyclerView.visibility = View.GONE
                    seeAllRecipesButton.visibility = View.GONE
                    clearButton.visibility = View.GONE
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerView)

        // Make "My Pantry" and RecyclerView visible if there are saved ingredients
        if (selectedIngredients.isNotEmpty()) {
            myPantryTitle.visibility = View.VISIBLE
            recyclerView.visibility = View.VISIBLE
        } else {
            myPantryTitle.visibility = View.GONE
            recyclerView.visibility = View.GONE
        }

        autoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    if (it.length > 2) {
                        fetchIngredientSuggestions(it.toString()) { suggestions ->
                            runOnUiThread {
                                val adapter = ArrayAdapter(
                                    this@PantryActivity,
                                    android.R.layout.simple_dropdown_item_1line,
                                    suggestions
                                )
                                autoCompleteTextView.setAdapter(adapter)
                                autoCompleteTextView.showDropDown()
                            }
                        }
                    }
                }
            }
        })

        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val selectedIngredient = autoCompleteTextView.adapter.getItem(position) as String
            selectIngredient(selectedIngredient)
            autoCompleteTextView.text.clear()
        }

        // Ensure the Clear button is only visible when the pantry has items
        clearButton.visibility = if (selectedIngredients.isNotEmpty()) View.VISIBLE else View.GONE
    }

    private fun fetchIngredientSuggestions(query: String, callback: (List<String>) -> Unit) {
        val apiKey = "12329ad60bb048b291b046053c313e41"
        val url = "https://api.spoonacular.com/food/ingredients/autocomplete?query=$query&number=5&apiKey=$apiKey"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.e("API_ERROR", "Request failed: ${e.message}")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val responseBody = response.body?.string()
                if (response.isSuccessful && !responseBody.isNullOrEmpty()) {
                    val suggestions = Gson().fromJson(responseBody, Array<IngredientSuggestion>::class.java)
                    callback(suggestions.map { it.name })
                }
            }
        })
    }

    private fun selectIngredient(ingredient: String) {
        if (!selectedIngredients.contains(ingredient)) {
            selectedIngredients.add(ingredient)
            pantryAdapter.notifyDataSetChanged()
            saveIngredients() // Save the updated list

            myPantryTitle.visibility = View.VISIBLE
            recyclerView.visibility = View.VISIBLE
            seeAllRecipesButton.visibility = View.VISIBLE
            clearButton.visibility = View.VISIBLE // Show clear button
        }
    }

    private fun saveIngredients() {
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(selectedIngredients)
        editor.putString("SAVED_INGREDIENTS", json)
        editor.apply()
    }

    private fun loadIngredients() {
        val json = sharedPreferences.getString("SAVED_INGREDIENTS", null)
        val type = object : TypeToken<MutableList<String>>() {}.type
        val savedList: MutableList<String>? = Gson().fromJson(json, type)

        if (savedList != null) {
            selectedIngredients.clear()
            selectedIngredients.addAll(savedList)
        }
    }

    data class IngredientSuggestion(val name: String)
}

