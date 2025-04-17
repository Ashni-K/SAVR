package com.example.savr

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object PexelsApiClient {
    private const val BASE_URL = "https://api.pexels.com/v1/"
    private const val API_KEY = "NPaqzxre3cvyLFoOn25OTbmW454Dsj7Cz4L4vf0XyJcj3STXxqUmYZEv"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", API_KEY)
                .build()
            chain.proceed(request)
        }
        .build()

    val instance: PexelsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PexelsApiService::class.java)
    }
}
