package com.example.savr

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.view.animation.DecelerateInterpolator
import android.animation.ObjectAnimator
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
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

//image API key = NPaqzxre3cvyLFoOn25OTbmW454Dsj7Cz4L4vf0XyJcj3STXxqUmYZEv

class Recipes : ComponentActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var noRecipesTextView: TextView
    private val recipeList = mutableListOf<Recipe>()

    private val apiKey = BuildConfig.GEMINI_API_KEY

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

                val updatedRecipes = withContext(Dispatchers.IO) {
                    // Create a list of deferred results using async
                    val deferredResults = parsedRecipes.map { recipe ->
                        async {
                            val imageUrl = fetchImageForRecipe(recipe.title)
                            recipe.copy(imageUrl = imageUrl)
                        }
                    }
                    // Await all results to complete in parallel
                    deferredResults.awaitAll()
                }

                // Update the UI on the main thread
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE

                    if (updatedRecipes.isNotEmpty()) {
                        recipeList.clear()
                        recipeList.addAll(updatedRecipes)
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
        You are a cooking assistant. Return exactly 12 recipe recommendations based on these ingredients: ${ingredients.joinToString(", ")}. Each recipe must only use ingredients from the provided list (but not all ingredients need to be used).
        
        Respond with ONLY a JSON array, no markdown formatting, no extra text. Each object in the array should have:
        - "title": Recipe name (String)
        - "ingredients": List of ingredient strings (List<String>)
        - "instructions": List of step-by-step instructions (List<String>)
        - "imageUrl": Valid image URL (use "https://via.placeholder.com/150" if unavailable)
        - "prepTime": Integer (minutes)
        - "cookTime": Integer (minutes)
        - "difficulty": String (easy, medium, hard, etc.)
        - "cuisine": String (e.g., "Mexican", "Italian", "Fusion", "Asian", "Native American", etc.)
        - "nutrition": String (e.g., "Calories: 250, Protein: 10g, Fat: 8g, Fiber: 6g")
        
        Example format (return ONLY valid JSON like this):
        [
            {
                "title": "Pasta Salad",
                "ingredients": ["2 cups pasta", "1/2 cup cherry tomatoes", "1/4 cup olive oil"],
                "instructions": ["Boil pasta", "Mix with tomatoes and oil", "Serve chilled"],
                "imageUrl": "https://via.placeholder.com/150",
                "prepTime": 10,
                "cookTime": 15,
                "difficulty": "easy",
                "cuisine": "Italian",
                "nutrition": "Calories: 250, Protein: 10g, Fat: 8g, Fiber: 6g"
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
    suspend fun fetchImageForRecipe(query: String): String {
        return try {
            val response = PexelsApiClient.instance.searchPhotos(query)
            response.photos.firstOrNull()?.src?.large ?: "https://via.placeholder.com/150"
        } catch (e: Exception) {
            Log.e("PexelsAPI", "Error fetching image for $query: ${e.message}")
            "https://via.placeholder.com/150"
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
    val cookTime: Int,
    val difficulty: String,
    val cuisine: String,
    val nutrition: String
)

// Adapter for displaying recipes
class RecipeAdapter(private val recipes: List<Recipe>) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val frontView: View = view.findViewById(R.id.cardFront)
        val backView: View = view.findViewById(R.id.cardBack)

        val titleTextView: TextView = view.findViewById(R.id.recipeTitle)
        val prepTimeTextView: TextView = view.findViewById(R.id.prepTime)
        val cookTimeTextView: TextView = view.findViewById(R.id.cookTime)
        val difficultyTextView: TextView = view.findViewById(R.id.difficulty)
        val cuisineTextView: TextView = view.findViewById(R.id.cuisine)
        val nutritionTextView: TextView = view.findViewById(R.id.nutritionInfo)
        val recipeImageView: ImageView = view.findViewById(R.id.recipeImage)
        val ingredientsTextView: TextView = view.findViewById(R.id.ingredientsList)
        val instructionsTextView: TextView = view.findViewById(R.id.instructionsList)

        var isFlipped = false // Track flip state
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipes[position]

        holder.titleTextView.text = recipe.title
        holder.prepTimeTextView.text = "Prep: ${recipe.prepTime} min"
        holder.cookTimeTextView.text = "Cook: ${recipe.cookTime} min"
        holder.difficultyTextView.text = "Difficulty: ${recipe.difficulty}"
        holder.cuisineTextView.text = "Cuisine: ${recipe.cuisine}"
        holder.nutritionTextView.text = recipe.nutrition

        Picasso.get()
            .load(recipe.imageUrl)
            .fit()
            .centerCrop()
            .into(holder.recipeImageView, object : com.squareup.picasso.Callback {
                override fun onSuccess() {}
                override fun onError(e: Exception?) {
                    Log.e("RecipeAdapter", "Error loading image: ${e?.message}")
                }
            })

        val ingredientsText = recipe.ingredients.joinToString("\n• ", "• ")
        holder.ingredientsTextView.text = ingredientsText

        val instructionsText = recipe.instructions.mapIndexed { index, instruction ->
            "${index + 1}. $instruction"
        }.joinToString("\n\n")
        holder.instructionsTextView.text = instructionsText

        // Flip animation logic
        holder.itemView.setOnClickListener {
            if (!holder.isFlipped) {
                flipCard(holder.frontView, holder.backView)
            } else {
                flipCard(holder.backView, holder.frontView)
            }
            holder.isFlipped = !holder.isFlipped
        }
    }

    override fun getItemCount() = recipes.size

    private fun flipCard(front: View, back: View) {
        val animator1 = ObjectAnimator.ofFloat(front, "rotationY", 0f, 90f)
        val animator2 = ObjectAnimator.ofFloat(back, "rotationY", -90f, 0f)

        animator1.interpolator = DecelerateInterpolator()
        animator2.interpolator = DecelerateInterpolator()

        animator1.duration = 300
        animator2.duration = 300

        animator1.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                front.visibility = View.GONE
                back.visibility = View.VISIBLE
                animator2.start()
            }
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })

        animator1.start()
    }
}