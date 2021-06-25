package com.example.dogpark.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestClient {

    companion object {
        private var retroFitClient: Retrofit? = null

        fun getRestClient(url:String): Retrofit {

            if(retroFitClient == null) {
                val logger = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
                val okClient: OkHttpClient = OkHttpClient.Builder().addInterceptor(logger).build()
                retroFitClient = Retrofit.Builder()
                    .baseUrl(url)
                    .client(okClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retroFitClient!!
        }
    }
}