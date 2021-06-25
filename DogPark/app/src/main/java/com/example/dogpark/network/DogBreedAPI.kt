package com.example.dogpark.network

import com.example.dogpark.model.BreedRandomImageModel
import com.example.dogpark.model.MasterBreedModel
import retrofit2.http.GET
import retrofit2.http.Path

interface DogBreedAPI {

    @GET("breeds/list/all")
    suspend fun getMasterDogBreedList() : MasterBreedModel

    @GET("breed/{master_breed_name}/images/random")
    suspend fun getMasterBreedRandomImage(@Path("master_breed_name") breedName:String): BreedRandomImageModel
}