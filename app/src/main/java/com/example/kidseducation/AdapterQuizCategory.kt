package com.example.kidseducation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kidseducation.response.quizcategory.QuizCategoryResponse
import com.squareup.picasso.Picasso

class AdapterQuizCategory(private val listQuiz:ArrayList<QuizCategoryResponse>): RecyclerView.Adapter<AdapterQuizCategory.ViewHolder>() {

    inner class ViewHolder(v: View):RecyclerView.ViewHolder(v) {
        val imgQuiz: ImageView = v.findViewById(R.id.imageCategory)
        val textCategory: TextView = v.findViewById(R.id.textCategoryName)

        fun bind(response: QuizCategoryResponse) {
            val name = "${response.nama_pelajaran}"
            val image = "${response.image_kategori}"

            textCategory.text = name
            var url = "http://172.20.10.8:80/edukids_api/gambar_kategori/" + image
            Picasso.get().load(url).into(imgQuiz)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listQuiz[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layout_quiz_category, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listQuiz.size
}