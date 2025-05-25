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
import java.io.File

class MeActivity : ComponentActivity() {

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val destinationUri = Uri.fromFile(File(cacheDir, "croppedImage.jpg"))
            val uCrop = com.yalantis.ucrop.UCrop.of(it, destinationUri)
            uCrop.withAspectRatio(1f, 1f)
            uCrop.start(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == com.yalantis.ucrop.UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            val resultUri = com.yalantis.ucrop.UCrop.getOutput(data!!)
            resultUri?.let {
                findViewById<ImageView>(R.id.imageView).setImageURI(it)

                // Save cropped image URI
                val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                sharedPref.edit().putString("profilePicUri", it.toString()).apply()
            }
        } else if (resultCode == com.yalantis.ucrop.UCrop.RESULT_ERROR) {
            val cropError = com.yalantis.ucrop.UCrop.getError(data!!)
            cropError?.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.me)

        val profileImage = findViewById<ImageView>(R.id.imageView)
        val usernameEditText = findViewById<EditText>(R.id.Id)
        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        // Load saved username
        val savedUsername = sharedPref.getString("username", "")
        usernameEditText.setText(savedUsername)

        // Load saved profile picture URI
        val savedProfilePicUri = sharedPref.getString("profilePicUri", null)
        if (savedProfilePicUri != null) {
            profileImage.setImageURI(Uri.parse(savedProfilePicUri))
        }

        profileImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        // Save username on Enter
        usernameEditText.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                usernameEditText.clearFocus()
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(usernameEditText.windowToken, 0)

                sharedPref.edit().putString("username", usernameEditText.text.toString()).apply()
                true
            } else {
                false
            }
        }

        // Navigation buttons
        findViewById<Button>(R.id.button42).setOnClickListener {
            startActivity(Intent(this, PantryActivity::class.java))
        }

        findViewById<Button>(R.id.button40).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        findViewById<Button>(R.id.button41).setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }
    }
}
