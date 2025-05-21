package com.example.savr

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts

class MeActivity : ComponentActivity(){

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

        profileImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        // Button navigation
        val pantryButton = findViewById<Button>(R.id.button42)
        pantryButton.setOnClickListener {
            val intent = Intent(this, PantryActivity::class.java)
            startActivity(intent)
        }
        val home4Button = findViewById<Button>(R.id.button40)
        home4Button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val favoritesButton = findViewById<Button>(R.id.button41)
        favoritesButton.setOnClickListener {
            val intent = Intent(this, FavoritesActivity::class.java)
            startActivity(intent)
        }
    }
}