package com.example.savr

data class PexelsSearchResponse(
    val photos: List<PexelsPhoto>
)

data class PexelsPhoto(
    val width: Int,
    val height: Int,
    val alt: String?,
    val src: Src
)

data class Src(
    val large: String
)