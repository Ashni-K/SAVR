package com.example.savr

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Find views
        val emailEditText = findViewById<EditText>(R.id.login_email)
        val passwordEditText = findViewById<EditText>(R.id.login_password)
        val loginButton = findViewById<Button>(R.id.login_button)
        val secondaryText = findViewById<TextView>(R.id.login_secondary_text)

        // Button click logic for login
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Add navigation or Toast for now
            if (email.isEmpty() || password.isEmpty()) {
                // Show some sort of notification to the user
                emailEditText.error = "Please fill out your email"
                passwordEditText.error = "Please fill out your password"
            } else {
                // Proceed to home page or mock login success
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Closes the LoginActivity
            }
        }

        // Redirect to Signup or Forgot Password
        secondaryText.setOnClickListener {
        }
    }
}

