package com.example.savr

import retrofit2.http.GET
import retrofit2.http.Query

interface PexelsApiService {
    @GET("search")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("per_page") perPage: Int = 3
    ): PexelsResponse
}

data class PexelsResponse(
    val photos: List<PexelsPhoto>
)