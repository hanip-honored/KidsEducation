package com.example.kidseducation

import android.content.Context
import android.content.Intent
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
import com.squareup.picasso.Picasso

class AdapterCollectionCategory(private val listCollection:ArrayList<CollectionCategoryResponse>,  private val context: Context): RecyclerView.Adapter<AdapterCollectionCategory.ViewHolder>() {

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val txtNumber: TextView = v.findViewById(R.id.textNumber)
        val imgCollection: ImageView = v.findViewById(R.id.imageIcon)
        val textCategory: TextView = v.findViewById(R.id.textKategori)
        val cardCollection: FrameLayout = v.findViewById(R.id.cardCollection)


        fun bind(response: CollectionCategoryResponse, position: Int) {
            txtNumber.text = (position + 1).toString()
            textCategory.text = response.nama_kategori_objek
            val url = RetrofitClient.URL_COLLECTION + response.gambar_kategori_objek
            Picasso.get().load(url).into(imgCollection)

            val colors = listOf("#FECAC3", "#B6EFE8", "#FFE3C1", "#BAE3FF", "#A2DDC2", "#E7E1FF")
            val color = android.graphics.Color.parseColor(colors[position % colors.size])
            val background = cardCollection.background as GradientDrawable
            background.setColor(color)

            val colorsNumber = listOf("#F9A89D", "#53E4D3", "#FAC380", "#7BC1F0", "#34D58B", "#B9ABF0")
            val numberColor = android.graphics.Color.parseColor(colorsNumber[position % colorsNumber.size])

            val drawable = GradientDrawable()
            drawable.shape = GradientDrawable.OVAL
            drawable.setColor(numberColor)
            drawable.setSize(100, 100)
            txtNumber.background = drawable

            cardCollection.setOnClickListener {
                val intent = Intent(context, CollectionListActivity::class.java)
                intent.putExtra("id_kategori_objek", response.id_kategori_objek)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layout_collection, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listCollection.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listCollection[position], position)
    }
}