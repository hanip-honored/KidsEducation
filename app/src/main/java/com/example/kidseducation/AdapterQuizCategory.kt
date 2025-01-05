package com.example.kidseducation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kidseducation.client.RetrofitClient
import com.example.kidseducation.response.quizcategory.QuizCategoryResponse
import com.squareup.picasso.Picasso

class AdapterQuizCategory(private val listQuiz:ArrayList<QuizCategoryResponse>): RecyclerView.Adapter<AdapterQuizCategory.ViewHolder>() {

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val imgQuiz: ImageView = v.findViewById(R.id.imageCategory)
        val textCategory: TextView = v.findViewById(R.id.textCategoryName)
        val cardCategory: androidx.cardview.widget.CardView = v.findViewById(R.id.cardCategory)

        fun bind(response: QuizCategoryResponse, position: Int) {
            textCategory.text = response.nama_pelajaran
            val url = RetrofitClient.URL + response.image_kategori
            Picasso.get().load(url).into(imgQuiz)

            // Set warna latar belakang CardView
            val colors = listOf("#FECAC3", "#A2DDC2", "#FF8A00", "#FFE3C1", "#BAE3FF", "#E7E1FF")
            val color = android.graphics.Color.parseColor(colors[position % colors.size])
            cardCategory.setCardBackgroundColor(color)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listQuiz[position], position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layout_quiz_category, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listQuiz.size
}