package com.example.savr

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher // listens for text changes that happen in autocomplete textview
import android.util.Log
import android.view.LayoutInflater // UI
import android.view.View // UI
import android.view.ViewGroup // UI
import android.widget.ArrayAdapter // used to display the list of suggestions on search bar
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager // for displaying list of selected ingredients
import androidx.recyclerview.widget.RecyclerView  // for displaying list of selected ingredients
import com.google.gson.Gson // APIs JSON response --> Kotlin Object
import okhttp3.OkHttpClient // Handling API calls
import okhttp3.Request // Handling API calls
import java.io.IOException

class PantryActivity : ComponentActivity() {

    private val selectedIngredients = mutableListOf<String>() // Makes a mutable list to store selected ingredients
    private lateinit var pantryAdapter: PantryAdapter // Adapter for managing ingredient list display in RecyclerView.
    private lateinit var myPantryTitle: TextView
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) { // onCreate function to start an activity
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantry) // Link to load activity_pantry.xml

        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.autoCompleteSearch) // ui element for searching
        myPantryTitle = findViewById(R.id.textViewMyPantry) // Reference "My Pantry" title
        recyclerView = findViewById(R.id.recyclerViewPantry) // ui element for displaying selected ingredients

        // Initialize RecyclerView with adapter
        pantryAdapter = PantryAdapter(selectedIngredients)
        recyclerView.layoutManager = LinearLayoutManager(this) // Displays ingredients in a vertical list.
        recyclerView.adapter = pantryAdapter

        // Make "My Pantry" and RecyclerView initially invisible
        myPantryTitle.visibility = View.GONE
        recyclerView.visibility = View.GONE

        autoCompleteTextView.addTextChangedListener(object : TextWatcher { // listening for text input in search bar
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    if (it.length > 2) { // suggestions after more than 2 chars are inputted
                        fetchIngredientSuggestions(it.toString()) { suggestions ->
                            runOnUiThread {
                                val adapter = ArrayAdapter(
                                    this@PantryActivity,
                                    android.R.layout.simple_dropdown_item_1line,
                                    suggestions
                                )
                                autoCompleteTextView.setAdapter(adapter) // Updates AutoCompleteTextView with API suggestions.
                                autoCompleteTextView.showDropDown()
                            }
                        }
                    }
                }
            }
        })

        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val selectedIngredient = autoCompleteTextView.adapter.getItem(position) as String
            selectIngredient(selectedIngredient) // Call the function when an ingredient is selected
            autoCompleteTextView.text.clear() // Clear search field after selection
        }
    }

    // API CALL
    private fun fetchIngredientSuggestions(query: String, callback: (List<String>) -> Unit) {
        val apiKey = "12329ad60bb048b291b046053c313e41"
        val url = "https://api.spoonacular.com/food/ingredients/autocomplete?query=$query&number=5&apiKey=$apiKey" // builds API request url

        val client = OkHttpClient() // builds HTTP client
        val request = Request.Builder().url(url).build() // builds HTTP req

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) { // if API req fails
                Log.e("API_ERROR", "Request failed: ${e.message}")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) { // if API req successful
                val responseBody = response.body?.string()
                if (response.isSuccessful && !responseBody.isNullOrEmpty()) {
                    val suggestions = Gson().fromJson(responseBody, Array<IngredientSuggestion>::class.java) // converts JSON response to list of ingredient names
                    callback(suggestions.map { it.name }) // returns results
                }
            }
        })
    }

    // Handle ingredient selection
    private fun selectIngredient(ingredient: String) {
        if (!selectedIngredients.contains(ingredient)) {
            selectedIngredients.add(ingredient)
            pantryAdapter.notifyDataSetChanged() // Update RecyclerView

            if (selectedIngredients.isNotEmpty()) {
                myPantryTitle.visibility = View.VISIBLE // makes "My Pantry" visible when selected Ingredients is not empty (when user selects something)
                recyclerView.visibility = View.VISIBLE // makes recycler view visible when selected Ingredients is not empty (when user selects something)
            }
        }
    }

    data class IngredientSuggestion(val name: String)

    override fun onDestroy() { // for clean up after activity over
        super.onDestroy()
    }
}

class PantryAdapter(private val ingredients: MutableList<String>) : RecyclerView.Adapter<PantryAdapter.ViewHolder>() { // Handles displaying selected ingredients in RecyclerView.

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder { // Creates a new view for each ingredient item.
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ingredient_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { // Gets the ingredient from ingredients and sets its text in the TextView.
        val ingredient = ingredients[position]
        holder.ingredientName.text = ingredient
    }

    override fun getItemCount(): Int = ingredients.size // Returns the number of selected ingredients.

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ingredientName: TextView = itemView.findViewById(R.id.ingredientName)
    }
}
