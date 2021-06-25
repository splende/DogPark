package com.example.dogpark.network

import android.util.Log
import com.example.dogpark.model.BreedModel
import com.example.dogpark.model.BreedRandomImageModel
import com.example.dogpark.model.MasterBreedModel
import com.example.dogpark.model.Message
import com.example.dogpark.util.Constant
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.lang.reflect.Type

class Repository {

    companion object{

        suspend fun getMasterBreed(): MasterBreedModel {
            val restClient = RestClient.getRestClient(Constant.BASE_URL)
            val dogBreedAPI = restClient.create(DogBreedAPI::class.java)
            return dogBreedAPI.getMasterDogBreedList()
        }

        suspend fun getMasterBreedRandomImage(masterBreedName: String): BreedRandomImageModel {
            val restClient = RestClient.getRestClient(Constant.BASE_URL)
            val dogBreedAPI = restClient.create(DogBreedAPI::class.java)
            return dogBreedAPI.getMasterBreedRandomImage(masterBreedName)
        }

        suspend fun getPocessedMasterBread(masterBreedDetail: MasterBreedModel): MutableList<BreedModel> {
            var masterDogBreedList: MutableList<BreedModel>? = null
            if(masterBreedDetail?.status == "success") {
                val message: Message = masterBreedDetail?.message
                val messageInJsonString = Gson().toJson(message)
                val jsonObject = JSONObject(messageInJsonString)
                val masterBreedList : MutableIterator<String> = jsonObject.keys()
                masterDogBreedList = mutableListOf()
                val statusTypeObject: Type = object: TypeToken<List<String>>(){}.type
                do {
                    val masterBreed = masterBreedList.next()
                    val subBreedJsonArray = jsonObject.get(masterBreed)
                    val subBreedList = Gson().fromJson<List<String>>(subBreedJsonArray.toString(), statusTypeObject)
                    val breed = BreedModel(masterBreed, !subBreedList.isEmpty(), subBreedList)
                    masterDogBreedList.add(breed)
                } while (masterBreedList.hasNext())
                Log.d("sach: Repo ßwhole: ", masterDogBreedList.toString())
            }
            return masterDogBreedList!!
        }
    }
}