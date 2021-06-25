package com.example.dogpark.UI

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dogpark.adapter.MasterBreedAdapter
import com.example.dogpark.databinding.MasterBreedActivityBinding
import com.example.dogpark.model.MasterBreedList
import com.example.dogpark.state.MasterBreedActivityUIState


/**
 * Screen to show Master Breed List of Dogs
 */
class MasterBreedActivity: AppCompatActivity() {

    private val viewBinding: MasterBreedActivityBinding by lazy { MasterBreedActivityBinding.inflate(layoutInflater) }
    private val viewModel: MasterBreedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        viewModel.getUIState().observe(this, Observer {
            uiState: MasterBreedActivityUIState -> updateUIState(uiState)
        })
        viewModel.getMastBreed()
    }

    private fun updateUIState(uiState: MasterBreedActivityUIState) {
        when(uiState) {
            is MasterBreedActivityUIState.isLoading -> showLoading()
            is MasterBreedActivityUIState.onSuccess -> processResponse()
            is MasterBreedActivityUIState.onError -> showError(uiState.errorMsg)
            is MasterBreedActivityUIState.onMasterBreedCollectionReady -> showList(uiState.masterBreedList)
        }
    }

    private fun processResponse() {
        viewModel.getMasterBreedImageUrlMap()
    }

    private fun showLoading() {
        viewBinding.progressBar.visibility = View.VISIBLE
    }
    private fun showList(masterBreedList: MasterBreedList) {
        viewBinding.progressBar.visibility = View.GONE
        val llManger = LinearLayoutManager(this)
        val masterAdapter = MasterBreedAdapter(this, masterBreedList.masterBreedList!!, masterBreedList.masterBreedRandomImageUrlMap!!)
        viewBinding.recyclerView.layoutManager = llManger
        viewBinding.recyclerView.adapter = masterAdapter
    }

    private fun showError(errorMsg: String) {
        viewBinding.progressBar.visibility = View.GONE
        if(!TextUtils.isEmpty(errorMsg)) {
            viewBinding.breedDesTxtId.text = errorMsg
        }
    }

}