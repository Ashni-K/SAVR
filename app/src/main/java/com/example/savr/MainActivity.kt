package com.example.savr

import android.os.Bundle
import android.content.Intent
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homescreen)

        // Button navigation
        val pantryButton = findViewById<Button>(R.id.button6)
        pantryButton.setOnClickListener {
            val intent = Intent(this, PantryActivity::class.java)
            startActivity(intent)
        }
        val Me1Button = findViewById<Button>(R.id.button5)
        Me1Button.setOnClickListener {
            val intent = Intent(this, MeActivity::class.java)
            startActivity(intent)
        }

        val favoritesButton = findViewById<Button>(R.id.button3)
        favoritesButton.setOnClickListener {
            val intent = Intent(this, FavoritesActivity::class.java)
            startActivity(intent)
        }

        // Animations
        val fork = findViewById<ImageView>(R.id.forkImage)
        val spoon = findViewById<ImageView>(R.id.spoonImage)
        val logo = findViewById<ImageView>(R.id.logoImage)
        val heading = findViewById<ImageView>(R.id.headingImage)
        val slogan = findViewById<TextView>(R.id.descriptionText)

        val forkAnim = AnimationUtils.loadAnimation(this, R.anim.fork_slide)
        val spoonAnim = AnimationUtils.loadAnimation(this, R.anim.spoon_slide)
        val fadeSlide = AnimationUtils.loadAnimation(this, R.anim.fade_slide_up)

        fork.startAnimation(forkAnim)
        spoon.startAnimation(spoonAnim)
        logo.startAnimation(fadeSlide)
        heading.startAnimation(fadeSlide)
        slogan.startAnimation(fadeSlide)
    }
}
