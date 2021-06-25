package com.example.dogpark.UI

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogpark.model.BreedModel
import com.example.dogpark.model.BreedRandomImageModel
import com.example.dogpark.model.MasterBreedList
import com.example.dogpark.model.MasterBreedModel
import com.example.dogpark.network.Repository
import com.example.dogpark.state.MasterBreedActivityUIState
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

class MasterBreedViewModel: ViewModel() {

    private var uiState: MutableLiveData<MasterBreedActivityUIState> = MutableLiveData<MasterBreedActivityUIState>()

    fun getUIState(): LiveData<MasterBreedActivityUIState> {
        return uiState;
    }

    fun getMastBreed() {
        viewModelScope.launch {
            uiState.value = MasterBreedActivityUIState.isLoading
            var masterBreedModel: MasterBreedModel? = null;
            var masterBreedList: MutableList<BreedModel>? = null;
            try {
                masterBreedModel = Repository.getMasterBreed()
                masterBreedList = Repository.getPocessedMasterBread(masterBreedModel)
            } catch (e: Exception) {
                uiState.value = MasterBreedActivityUIState.onError("Error in fetching Master Dog Breed List")
            }
            uiState.value = MasterBreedActivityUIState.onSuccess(masterBreedList!!)
        }
    }

    /**
     * Method to get Image Url for Master breed
     */
    fun getMasterBreedImageUrlMap() {
        val successUiState : MasterBreedActivityUIState.onSuccess? = getUIState().value as MasterBreedActivityUIState.onSuccess
        val masterBreedList: MutableList<BreedModel>? = successUiState?.masterBreedDetail
        val masterBreedRandomImageUrlMap: ConcurrentHashMap<String, BreedRandomImageModel> = ConcurrentHashMap<String, BreedRandomImageModel>()
        //Collection of all deferred task
        val deferredTaskList : MutableList<Deferred<BreedRandomImageModel>> = mutableListOf()
        // API call to get random image response for each master breed
        masterBreedList?.forEach {
            val randomImageDeferred = viewModelScope.async {
                val randomImgResponse = Repository.getMasterBreedRandomImage(it.breedName)
                randomImgResponse
            }
            deferredTaskList.add(randomImageDeferred)
        }

        // Make key-value pair of master_breed and its random image response
        var index : Int = 0 // Can be optimized further
        viewModelScope.launch {
            deferredTaskList.forEach {
                val masterBreed: String? = masterBreedList?.get(index)?.breedName
                if (masterBreed != null) {
                    masterBreedRandomImageUrlMap.put(masterBreed, it.await())
                }
                index++
            }
            uiState.value = MasterBreedActivityUIState.onMasterBreedCollectionReady(MasterBreedList(masterBreedList, masterBreedRandomImageUrlMap))
        }
    }
}