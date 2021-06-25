package com.example.dogpark.model
import com.google.gson.annotations.SerializedName
data class BreedRandomImageModel(
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)