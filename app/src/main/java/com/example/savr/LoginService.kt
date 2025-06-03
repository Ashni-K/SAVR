package com.example.savr

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

// Request and Response models
data class RegisterRequest(val email: String, val password: String)
data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val message: String, val token: String)

interface LoginService {
    @POST("/register")
    fun register(@Body request: RegisterRequest): Call<Void>

    @POST("/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}