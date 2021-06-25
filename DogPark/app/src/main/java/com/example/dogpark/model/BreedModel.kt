package com.example.dogpark.model

import java.util.concurrent.ConcurrentHashMap

data class BreedModel(val breedName: String,
val isSubBreedAvailable: Boolean,
val subBreedList: List<String>)

data class MasterBreedList(
    val masterBreedList: MutableList<BreedModel>?,
    val masterBreedRandomImageUrlMap: ConcurrentHashMap<String, BreedRandomImageModel>?
)
