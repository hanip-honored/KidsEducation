package com.example.kidseducation

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kidseducation.client.RetrofitClient
import com.example.kidseducation.response.collection.CollectionCategoryResponse
import com.example.kidseducation.response.collection.UserCollectionResponse
import com.squareup.picasso.Picasso

class AdapterCollectionList(private val userCollection:ArrayList<UserCollectionResponse>): RecyclerView.Adapter<AdapterCollectionList.ViewHolder>() {

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val objectName: TextView = v.findViewById(R.id.textObjectName)
        val imgObject: ImageView = v.findViewById(R.id.imgObject)


        fun bind(response: UserCollectionResponse, position: Int) {
            objectName.text = response.nama_objek
            val url = RetrofitClient.URL_COLLECTION + response.gambar_objek
            Picasso.get().load(url).into(imgObject)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_collection, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userCollection.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userCollection[position], position)
    }
}