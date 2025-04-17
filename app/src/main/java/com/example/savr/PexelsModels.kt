package com.example.savr

data class PexelsSearchResponse(
    val photos: List<PexelsPhoto>
)

data class PexelsPhoto(
    val src: PexelsSrc
)

data class PexelsSrc(
    val large: String
)
