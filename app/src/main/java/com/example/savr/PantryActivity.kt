package com.example.savr

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.ComponentActivity
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class PantryActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantry)

        Log.d("PantryActivity", "PantryActivity created") // Debug log

        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.autoCompleteSearch)

        autoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    if (it.length > 2) { // Fetch suggestions only if user types more than 2 letters
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
    }

    private fun fetchIngredientSuggestions(query: String, callback: (List<String>) -> Unit) {
        val apiKey = "12329ad60bb048b291b046053c313e41"
        val url = "https://api.spoonacular.com/food/ingredients/autocomplete?query=$query&number=5&apiKey=$apiKey"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.e("API_ERROR", "Request failed: ${e.message}") // Log error
                e.printStackTrace()
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val responseBody = response.body?.string()
                Log.d("API_RESPONSE", "Response: $responseBody") // Log API response

                if (!response.isSuccessful || responseBody.isNullOrEmpty()) {
                    Log.e("API_ERROR", "Failed response: ${response.code}")
                    return
                }

                responseBody.let {
                    val suggestions = Gson().fromJson(it, Array<IngredientSuggestion>::class.java)
                    callback(suggestions.map { it.name })
                }
            }
        })
    }

    data class IngredientSuggestion(val name: String)

    override fun onDestroy() {
        super.onDestroy()
        Log.d("PantryActivity", "PantryActivity destroyed") // Debug log for activity lifecycle
    }
}
