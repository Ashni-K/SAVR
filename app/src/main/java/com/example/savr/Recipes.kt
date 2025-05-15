package com.example.savr

import android.animation.Animator
import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URLEncoder


class Recipes : ComponentActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var recipeAdapter: RecipeFilterAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var noRecipesTextView: TextView
    private lateinit var filterButton: Button
    private val recipeList = mutableListOf<Recipe>()

    // To store all available cuisines for the spinner
    private val availableCuisines = mutableSetOf<String>()

    private val apiKey = BuildConfig.GEMINI_API_KEY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recipes)

        // Set up navigation buttons
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
            val intent = Intent(this, FavoritesActivity::class.java)
            startActivity(intent)
        }

        // Set up RecyclerView with the new filter adapter
        recyclerView = findViewById(R.id.recyclerViewRecipes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recipeAdapter = RecipeFilterAdapter(recipeList)
        recyclerView.adapter = recipeAdapter

        // Set the filter change listener
        recipeAdapter.setFilterChangeListener(object : RecipeFilterAdapter.FilterChangeListener {
            override fun onFiltersChanged(activeFilters: List<String>) {
                updateActiveFiltersText(activeFilters)
            }
        })

        // Set up progress bar and no recipes text
        progressBar = findViewById(R.id.progressBar)
        noRecipesTextView = findViewById(R.id.noRecipesText)

        // Set up filter button (using checkBox3 as the filter button)
        filterButton = findViewById<Button>(R.id.checkBox3)
        filterButton.text = "Filters"
        filterButton.setOnClickListener {
            showFilterDialog()
        }

        // Set initial active filters text
        val activeFiltersText = findViewById<TextView>(R.id.activeFiltersText)
        activeFiltersText.text = "No active filters"

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

    // Update the active filters text view
    private fun updateActiveFiltersText(activeFilters: List<String>) {
        val activeFiltersText = findViewById<TextView>(R.id.activeFiltersText)
        if (activeFilters.isEmpty()) {
            activeFiltersText.text = "No active filters"
        } else {
            activeFiltersText.text = activeFilters.joinToString(" | ")
        }
    }

    private fun showFilterDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.filter_dialog)

        // Find dialog views
        val prepTimeSeekBar = dialog.findViewById<SeekBar>(R.id.prepTimeSeekBar)
        val prepTimeLabel = dialog.findViewById<TextView>(R.id.prepTimeLabel)
        val cookTimeSeekBar = dialog.findViewById<SeekBar>(R.id.cookTimeSeekBar)
        val cookTimeLabel = dialog.findViewById<TextView>(R.id.cookTimeLabel)
        val difficultyRadioGroup = dialog.findViewById<RadioGroup>(R.id.difficultyRadioGroup)
        val cuisineSpinner = dialog.findViewById<Spinner>(R.id.cuisineSpinner)
        val caloriesSeekBar = dialog.findViewById<SeekBar>(R.id.caloriesSeekBar)
        val caloriesLabel = dialog.findViewById<TextView>(R.id.caloriesLabel)
        val resetButton = dialog.findViewById<Button>(R.id.resetFiltersButton)
        val applyButton = dialog.findViewById<Button>(R.id.applyFiltersButton)

        // Populate cuisine spinner with available cuisines
        val cuisinesList = ArrayList<String>(availableCuisines).apply {
            add(0, "Any Cuisine")
        }
        val cuisineAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            cuisinesList
        )
        cuisineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        cuisineSpinner.adapter = cuisineAdapter

        // Setup SeekBar listeners
        prepTimeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                prepTimeLabel.text = "$progress min"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        cookTimeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                cookTimeLabel.text = "$progress min"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        caloriesSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                caloriesLabel.text = "$progress calories"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Reset button
        resetButton.setOnClickListener {
            recipeAdapter.resetFilters()
            dialog.dismiss()
        }

        // Apply button
        applyButton.setOnClickListener {
            // Get difficulty selection
            val difficultyId = difficultyRadioGroup.checkedRadioButtonId
            val difficulty = if (difficultyId == R.id.anyDifficultyRadioButton) {
                null
            } else {
                dialog.findViewById<RadioButton>(difficultyId)?.text.toString().lowercase()
            }

            // Get cuisine selection
            val cuisine = if (cuisineSpinner.selectedItemPosition == 0) {
                null
            } else {
                cuisineSpinner.selectedItem.toString()
            }

            // Apply filters
            recipeAdapter.filterByPrepTime(prepTimeSeekBar.progress)
            recipeAdapter.filterByCookTime(cookTimeSeekBar.progress)
            recipeAdapter.filterByDifficulty(difficulty)
            recipeAdapter.filterByCuisine(cuisine)

            // Apply calories filter
            val nutritionFilter = mapOf("calories" to caloriesSeekBar.progress.toFloat())
            recipeAdapter.filterByNutrition(nutritionFilter)

            dialog.dismiss()
        }

        dialog.show()
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
                        // Extract available cuisines for filtering
                        availableCuisines.clear()
                        updatedRecipes.forEach { recipe ->
                            availableCuisines.add(recipe.cuisine)
                        }

                        // Update the recipe list
                        recipeList.clear()
                        recipeList.addAll(updatedRecipes)

                        // Update adapter with new recipes
                        recipeAdapter.updateRecipes(recipeList)

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
        - "difficulty": String (easy, medium, hard)
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

    suspend fun fetchImageForRecipe(recipeName: String): String {
        val query = "$recipeName food dish meal"

        return try {
            val response = PexelsApiClient.instance.searchPhotos(query)
            val imageUrl = response.photos.firstOrNull()?.src?.large

            imageUrl ?: "https://via.placeholder.com/150"
        } catch (e: Exception) {
            Log.e("PexelsAPI", "Error fetching image for $recipeName: ${e.message}")
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
class RecipeFilterAdapter(private var allRecipes: List<Recipe>) : RecyclerView.Adapter<RecipeFilterAdapter.ViewHolder>() {

    // List of recipes after filters are applied
    private var filteredRecipes: List<Recipe> = allRecipes.toList()
    private lateinit var favoritesManager: FavoritesManager
    private lateinit var context: Context

    // Filter states
    private var maxPrepTime: Int? = null
    private var maxCookTime: Int? = null
    private var selectedDifficulty: String? = null
    private var selectedCuisine: String? = null
    private var nutritionFilter: Map<String, Float>? = null

    // Filter change listener
    interface FilterChangeListener {
        fun onFiltersChanged(activeFilters: List<String>)
    }

    private var filterChangeListener: FilterChangeListener? = null

    fun setFilterChangeListener(listener: FilterChangeListener) {
        this.filterChangeListener = listener
    }

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
        val favoriteButtonFront: Button = view.findViewById(R.id.favoriteLabelFront)
        val favoriteButtonBack: Button = view.findViewById(R.id.favoriteLabelBack)

        var isFlipped = false // Track flip state
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_item, parent, false)
        context = parent.context
        favoritesManager = FavoritesManager.getInstance(context)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = filteredRecipes[position]

        Log.d("IMAGE_URL_CHECK", "Loading image: ${recipe.imageUrl}")


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

        // Set favorite button state and click listeners
        updateFavoriteButtons(holder, recipe)

        // Set favorite button click listeners
        holder.favoriteButtonFront.setOnClickListener {
            toggleFavorite(holder, recipe)
        }

        holder.favoriteButtonBack.setOnClickListener {
            toggleFavorite(holder, recipe)
        }

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

    // Toggle favorite status
    private fun toggleFavorite(holder: ViewHolder, recipe: Recipe) {
        val isFavorite = favoritesManager.isFavorite(recipe)

        if (isFavorite) {
            favoritesManager.removeFavorite(recipe)
        } else {
            favoritesManager.addFavorite(recipe)
        }

        // Update button text
        updateFavoriteButtons(holder, recipe)
    }

    // Update favorite button text based on favorite status
    private fun updateFavoriteButtons(holder: ViewHolder, recipe: Recipe) {
        val isFavorite = favoritesManager.isFavorite(recipe)

        val buttonText = if (isFavorite) "Remove from favorites" else "Add to favorites"
        holder.favoriteButtonFront.text = buttonText
        holder.favoriteButtonBack.text = buttonText
    }

    override fun getItemCount() = filteredRecipes.size

    // Update the dataset with new recipes
    fun updateRecipes(recipes: List<Recipe>) {
        allRecipes = recipes
        applyFilters() // Re-apply existing filters to the new dataset
    }

    // Apply all filters and update the recycler view
    fun applyFilters() {
        filteredRecipes = allRecipes.filter { recipe ->
            // Apply prep time filter
            (maxPrepTime == null || recipe.prepTime <= maxPrepTime!!) &&
                    // Apply cook time filter
                    (maxCookTime == null || recipe.cookTime <= maxCookTime!!) &&
                    // Apply difficulty filter
                    (selectedDifficulty == null || recipe.difficulty.equals(selectedDifficulty, ignoreCase = true)) &&
                    // Apply cuisine filter
                    (selectedCuisine == null || recipe.cuisine.equals(selectedCuisine, ignoreCase = true)) &&
                    // Apply nutrition filter (if any)
                    (nutritionFilter == null || matchesNutritionFilter(recipe.nutrition, nutritionFilter!!))
        }

        // Notify the listener about filter changes
        notifyFilterChanges()

        notifyDataSetChanged()
    }

    // Get list of active filter descriptions
    private fun notifyFilterChanges() {
        val activeFilters = ArrayList<String>()

        maxPrepTime?.let { activeFilters.add("Prep ≤ ${it}min") }
        maxCookTime?.let { activeFilters.add("Cook ≤ ${it}min") }
        selectedDifficulty?.let { activeFilters.add(it.capitalize()) }
        selectedCuisine?.let { activeFilters.add(it) }
        nutritionFilter?.let { filters ->
            filters.forEach { (key, value) ->
                activeFilters.add("${key.capitalize()} ≤ $value")
            }
        }

        filterChangeListener?.onFiltersChanged(activeFilters)
    }

    // Set max prep time filter
    fun filterByPrepTime(maxMinutes: Int?) {
        maxPrepTime = maxMinutes
        applyFilters()
    }

    // Set max cook time filter
    fun filterByCookTime(maxMinutes: Int?) {
        maxCookTime = maxMinutes
        applyFilters()
    }

    // Set difficulty filter
    fun filterByDifficulty(difficulty: String?) {
        selectedDifficulty = difficulty
        applyFilters()
    }

    // Set cuisine filter
    fun filterByCuisine(cuisine: String?) {
        selectedCuisine = cuisine
        applyFilters()
    }

    // Set nutrition filter
    fun filterByNutrition(nutritionParams: Map<String, Float>?) {
        nutritionFilter = nutritionParams
        applyFilters()
    }

    // Reset all filters
    fun resetFilters() {
        maxPrepTime = null
        maxCookTime = null
        selectedDifficulty = null
        selectedCuisine = null
        nutritionFilter = null
        filteredRecipes = allRecipes

        // Notify the listener that filters were reset
        filterChangeListener?.onFiltersChanged(emptyList())

        notifyDataSetChanged()
    }

    // Get current active filters as text
    fun getActiveFiltersText(): String {
        val activeFilters = ArrayList<String>()

        maxPrepTime?.let { activeFilters.add("Prep ≤ ${it}min") }
        maxCookTime?.let { activeFilters.add("Cook ≤ ${it}min") }
        selectedDifficulty?.let { activeFilters.add(it.capitalize()) }
        selectedCuisine?.let { activeFilters.add(it) }
        nutritionFilter?.let { filters ->
            filters.forEach { (key, value) ->
                activeFilters.add("${key.capitalize()} ≤ $value")
            }
        }

        return if (activeFilters.isEmpty()) {
            "No active filters"
        } else {
            activeFilters.joinToString(" | ")
        }
    }

    // Parse and check if a recipe matches nutrition filters
    private fun matchesNutritionFilter(nutritionInfo: String, filters: Map<String, Float>): Boolean {
        // Convert nutrition string "Calories: 250, Protein: 10g, Fat: 8g, Fiber: 6g" to values
        // and check if they match the filter criteria
        return try {
            val parts = nutritionInfo.split(",")
            val nutritionMap = mutableMapOf<String, Float>()

            for (part in parts) {
                val keyValue = part.trim().split(":")
                if (keyValue.size == 2) {
                    val key = keyValue[0].trim().lowercase()
                    val valueStr = keyValue[1].trim().replace(Regex("[^0-9.]"), "")
                    val value = valueStr.toFloatOrNull() ?: 0f
                    nutritionMap[key] = value
                }
            }

            // Check if all filter criteria are met
            filters.all { (key, maxValue) ->
                val actualValue = nutritionMap[key.lowercase()] ?: 0f
                actualValue <= maxValue
            }
        } catch (e: Exception) {
            Log.e("RecipeAdapter", "Error parsing nutrition: ${e.message}")
            false
        }
    }

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