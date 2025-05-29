package com.example.savr

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// LoginActivity class extends AppCompatActivity, making it a screen on the app
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) // load layout

        // Binds EditText and Button views from the XML layouts
        val emailEditText = findViewById<EditText>(R.id.login_email)
        val passwordEditText = findViewById<EditText>(R.id.login_password)
        val loginButton = findViewById<Button>(R.id.login_button)

        // Button click logic for login
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Input validation
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val loginRequest = LoginRequest(email, password) // sends the request to the API using Retrofit

            // HTTP client library for Android that simplifies the process of making network requests
            RetrofitClient.instance.login(loginRequest).enqueue(object : Callback<LoginResponse> { // enqueue makes it async
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val token = response.body()?.token
                        Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()

                        // Redirect to MainActivity
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish() // Close the current activity
                    } else {
                        Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // Find the "Sign Up" TextView
        val signUpText = findViewById<TextView>(R.id.login_secondary_text)

        // Navigate to RegisterActivity
        signUpText.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

    }
}


