package com.example.savr

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.squareup.picasso.Picasso

class Recipes : ComponentActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var noRecipesTextView: TextView
    private val recipeList = mutableListOf<Recipe>()

    private val apiKey = "AIzaSyBqVtZ3fxiOWMkHY1YKtN135cH_ugVpTno"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recipes)

        val homeButton = findViewById<Button>(R.id.button30)
        homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val pantry1Button = findViewById<Button>(R.id.button33)
        pantry1Button.setOnClickListener {
            val intent = Intent(this, PantryActivity::class.java)
            startActivity(intent)
        }

        val favoritesButton = findViewById<Button>(R.id.button32)
        favoritesButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerViewRecipes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recipeAdapter = RecipeAdapter(recipeList)
        recyclerView.adapter = recipeAdapter

        // Set up progress bar and no recipes text
        progressBar = findViewById(R.id.progressBar)
        noRecipesTextView = findViewById(R.id.noRecipesText)

        // Get selected ingredients from intent
        val selectedIngredients = intent.getStringArrayListExtra("SELECTED_INGREDIENTS") ?: ArrayList()

        Log.d("Recipes", "Selected ingredients: $selectedIngredients")

        if (selectedIngredients.isNotEmpty()) {
            // Show progress bar while loading
            progressBar.visibility = View.VISIBLE
            noRecipesTextView.visibility = View.GONE

            // Get recipe recommendations using Gemini API
            getRecipeRecommendations(selectedIngredients)
        } else {
            // Show no recipes text
            progressBar.visibility = View.GONE
            noRecipesTextView.visibility = View.VISIBLE
            noRecipesTextView.text = "No ingredients selected. Please select ingredients from your pantry."
        }
    }

    private fun getRecipeRecommendations(ingredients: List<String>) {
        // Create the generation config correctly using the builder
        val generationConfig = generationConfig {
            temperature = 0.7f
            maxOutputTokens = 2048  // Increased token limit for larger responses
        }

        // Initialize the GenerativeModel
        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = apiKey,
            generationConfig = generationConfig
        )

        // Create the prompt for Gemini
        val prompt = buildPrompt(ingredients)

        // Use coroutines to perform the API call off the main thread
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Make the API call to Gemini
                val response = generativeModel.generateContent(prompt)
                val responseText = response.text?.trim() ?: ""

                Log.d("GEMINI_API", "Raw response: $responseText")

                // Extract JSON from response (in case there's any extra text)
                val jsonResponse = extractJsonFromResponse(responseText)

                Log.d("GEMINI_API", "Extracted JSON: $jsonResponse")

                // Parse the JSON response
                val parsedRecipes = parseRecipeResponse(jsonResponse)

                // Update the UI on the main thread
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE

                    if (parsedRecipes.isNotEmpty()) {
                        recipeList.clear()
                        recipeList.addAll(parsedRecipes)
                        recipeAdapter.notifyDataSetChanged()
                        noRecipesTextView.visibility = View.GONE
                    } else {
                        noRecipesTextView.text = "No recipes found for these ingredients. Try different ingredients."
                        noRecipesTextView.visibility = View.VISIBLE
                    }
                }
            } catch (e: Exception) {
                Log.e("GEMINI_API", "Error: ${e.message}", e)

                // Update UI to show error
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    noRecipesTextView.text = "Error fetching recipes: ${e.message?.take(100) ?: "Unknown error"}. Please try again."
                    noRecipesTextView.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun extractJsonFromResponse(response: String): String {
        // Try to find JSON array in the response
        val jsonStartIndex = response.indexOf("[")
        val jsonEndIndex = response.lastIndexOf("]")

        return if (jsonStartIndex != -1 && jsonEndIndex != -1 && jsonEndIndex > jsonStartIndex) {
            response.substring(jsonStartIndex, jsonEndIndex + 1)
        } else {
            // If we can't find proper JSON brackets, return the original
            // This will likely fail parsing, but it's better than returning an empty string
            response
        }
    }

    private fun buildPrompt(ingredients: List<String>): String {
        return """
        You are a cooking assistant. Return exactly 5 recipe recommendations based on these ingredients: ${ingredients.joinToString(", ")}.
        
        Respond with ONLY a JSON array, no markdown formatting, no extra text. Each object in the array should have:
        - "title": Recipe name (String)
        - "ingredients": List of ingredient strings (List<String>)
        - "instructions": List of step-by-step instructions (List<String>)
        - "imageUrl": Valid image URL (use "https://via.placeholder.com/150" if unavailable)
        - "prepTime": Integer (minutes)
        - "cookTime": Integer (minutes)
        
        Example format (return ONLY valid JSON like this):
        [
            {
                "title": "Pasta Salad",
                "ingredients": ["2 cups pasta", "1/2 cup cherry tomatoes", "1/4 cup olive oil"],
                "instructions": ["Boil pasta", "Mix with tomatoes and oil", "Serve chilled"],
                "imageUrl": "https://via.placeholder.com/150",
                "prepTime": 10,
                "cookTime": 15
            }
        ]
    """.trimIndent()
    }

    private fun parseRecipeResponse(jsonResponse: String): List<Recipe> {
        return try {
            // First attempt: try to parse as an array
            Gson().fromJson(jsonResponse, Array<Recipe>::class.java).toList()
        } catch (e: JsonSyntaxException) {
            Log.e("JSON_PARSE", "Error parsing JSON array: ${e.message}")
            try {
                // Second attempt: try to parse as a single object
                val singleRecipe = Gson().fromJson(jsonResponse, Recipe::class.java)
                listOf(singleRecipe)
            } catch (e: Exception) {
                Log.e("JSON_PARSE", "Error parsing as single object: ${e.message}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("JSON_PARSE", "Unknown error parsing JSON: ${e.message}")
            emptyList()
        }
    }
}

// Data class for Recipe
data class Recipe(
    val title: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val imageUrl: String,
    val prepTime: Int,
    val cookTime: Int
)

// Adapter for displaying recipes
class RecipeAdapter(private val recipes: List<Recipe>) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.recipeTitle)
        val prepTimeTextView: TextView = view.findViewById(R.id.prepTime)
        val cookTimeTextView: TextView = view.findViewById(R.id.cookTime)
        val recipeImageView: ImageView = view.findViewById(R.id.recipeImage)
        val ingredientsTextView: TextView = view.findViewById(R.id.ingredientsList)
        val instructionsTextView: TextView = view.findViewById(R.id.instructionsList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_item, parent, false)
        return ViewHolder(view)
    }

    // In the RecipeAdapter class, update the onBindViewHolder method:

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipes[position]

        holder.titleTextView.text = recipe.title
        holder.prepTimeTextView.text = "Prep: ${recipe.prepTime} min"
        holder.cookTimeTextView.text = "Cook: ${recipe.cookTime} min"

        // Load image using Picasso with error handling but without referencing nonexistent drawables
        Picasso.get()
            .load(recipe.imageUrl)
            .fit()
            .centerCrop()
            .into(holder.recipeImageView, object : com.squareup.picasso.Callback {
                override fun onSuccess() {
                    // Image loaded successfully
                }

                override fun onError(e: Exception?) {
                    Log.e("RecipeAdapter", "Error loading image: ${e?.message}")
                }
            })

        // Format ingredients
        val ingredientsText = recipe.ingredients.joinToString("\n• ", "• ")
        holder.ingredientsTextView.text = ingredientsText

        // Format instructions
        val instructionsText = recipe.instructions.mapIndexed { index, instruction ->
            "${index + 1}. $instruction"
        }.joinToString("\n\n")
        holder.instructionsTextView.text = instructionsText
    }

    override fun getItemCount() = recipes.size
}