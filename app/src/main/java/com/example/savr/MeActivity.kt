package com.example.savr

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts

class MeActivity : ComponentActivity() {

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            findViewById<ImageView>(R.id.imageView).setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.me)

        val profileImage = findViewById<ImageView>(R.id.imageView)
        val usernameEditText = findViewById<EditText>(R.id.Id)
        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val savedUsername = sharedPref.getString("username", "")
        usernameEditText.setText(savedUsername)


        profileImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        // Finish editing on Enter/Done
        usernameEditText.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                usernameEditText.clearFocus()
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(usernameEditText.windowToken, 0)

                // Save username
                val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                sharedPref.edit().putString("username", usernameEditText.text.toString()).apply()

                true
            } else {
                false
            }
        }

        // Navigation buttons
        val pantryButton = findViewById<Button>(R.id.button42)
        pantryButton.setOnClickListener {
            startActivity(Intent(this, PantryActivity::class.java))
        }

        val home4Button = findViewById<Button>(R.id.button40)
        home4Button.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        val favoritesButton = findViewById<Button>(R.id.button41)
        favoritesButton.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }
    }
}
