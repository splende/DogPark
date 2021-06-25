package com.example.dogpark.state

import com.example.dogpark.model.BreedModel
import com.example.dogpark.model.MasterBreedList

sealed class MasterBreedActivityUIState {
    object isLoading: MasterBreedActivityUIState()
    class onSuccess(val masterBreedDetail: MutableList<BreedModel>): MasterBreedActivityUIState()
    class onMasterBreedCollectionReady(val masterBreedList: MasterBreedList): MasterBreedActivityUIState()
    class onError(val errorMsg: String): MasterBreedActivityUIState()
}