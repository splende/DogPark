package com.example.dogpark.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.dogpark.R
import com.example.dogpark.model.BreedModel
import com.example.dogpark.model.BreedRandomImageModel
import com.squareup.picasso.Picasso
import java.util.concurrent.ConcurrentHashMap

class MasterBreedAdapter(
    private val mContext: Context, private val masterBreedList: MutableList<BreedModel>,
    private val masterBreedRandomImageUrlMap: ConcurrentHashMap<String, BreedRandomImageModel>) : RecyclerView.Adapter<MasterBreedAdapter.BreedViewHolder>() {

    override fun onBindViewHolder(holder: BreedViewHolder, position: Int) {
        val currentBreedModel: BreedModel = masterBreedList.get(position)
        val breedName = currentBreedModel.breedName
        val randomImageModel: BreedRandomImageModel? = masterBreedRandomImageUrlMap.get(breedName)
        val imageUrl : String? = randomImageModel?.message
        holder.breedTypeName.text = breedName
        Picasso.with(mContext).load(imageUrl).into(holder.breedImage)
        if(currentBreedModel.isSubBreedAvailable) {
            holder.cardViewRoot.isClickable = true
            holder.cardViewRoot.setOnClickListener(View.OnClickListener {
                Toast.makeText(mContext, "Sub Breeds Available", Toast.LENGTH_SHORT).show()
            })
        }else {
            holder.cardViewRoot.isClickable = false
            holder.cardViewRoot.setOnClickListener(null)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dog_breed_layout_item, parent, false)
        return BreedViewHolder(view)
    }

    override fun getItemCount(): Int {
        return masterBreedList.size
    }

    class BreedViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val breedImage: ImageView = view.findViewById(R.id.dogBreedImgView)
        val breedTypeName: TextView = view.findViewById(R.id.dogBreedType)
        val cardViewRoot: CardView = view.findViewById(R.id.rootCardViewId)
    }
}