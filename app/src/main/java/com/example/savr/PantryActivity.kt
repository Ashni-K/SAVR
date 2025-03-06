package com.example.savr

import android.content.Intent
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
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class PantryActivity : ComponentActivity() {

    private val selectedIngredients = mutableListOf<String>()
    private lateinit var pantryAdapter: PantryAdapter
    private lateinit var myPantryTitle: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var seeAllRecipesButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantry)

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

        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.autoCompleteSearch)
        myPantryTitle = findViewById(R.id.textViewMyPantry)
        recyclerView = findViewById(R.id.recyclerViewPantry)

        // Initialize the See All Recipes button
        seeAllRecipesButton = findViewById(R.id.button14)
        seeAllRecipesButton.visibility = View.GONE
        seeAllRecipesButton.setOnClickListener {
            val intent = Intent(this, Recipes::class.java)
            intent.putStringArrayListExtra("SELECTED_INGREDIENTS", ArrayList(selectedIngredients))
            startActivity(intent)
        }

        // Initialize RecyclerView with adapter
        pantryAdapter = PantryAdapter(selectedIngredients, this)
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
                val deletedIngredient = selectedIngredients[position]

                // Remove the item from the list
                selectedIngredients.removeAt(position)
                pantryAdapter.notifyItemRemoved(position)
            }

        })
        itemTouchHelper.attachToRecyclerView(recyclerView)

        // Make "My Pantry" and RecyclerView initially invisible
        myPantryTitle.visibility = View.GONE
        recyclerView.visibility = View.GONE

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

            if (selectedIngredients.isNotEmpty()) {
                myPantryTitle.visibility = View.VISIBLE
                recyclerView.visibility = View.VISIBLE
                seeAllRecipesButton.visibility = View.VISIBLE // Show the button when ingredients are selected
            }
        }
    }

    data class IngredientSuggestion(val name: String)
}

class PantryAdapter(
    private val ingredients: MutableList<String>,
    private val context: PantryActivity
) : RecyclerView.Adapter<PantryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ingredient_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = ingredients[position]
        holder.ingredientName.text = ingredient
    }

    override fun getItemCount(): Int = ingredients.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ingredientName: TextView = itemView.findViewById(R.id.ingredientName)
    }
}
