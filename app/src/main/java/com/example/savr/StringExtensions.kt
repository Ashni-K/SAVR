package com.example.savr

// Extension function to capitalize first letter of string
fun String.capitalize(): String {
    return if (this.isNotEmpty()) {
        this.substring(0, 1).uppercase() + this.substring(1).lowercase()
    } else {
        this
    }
}