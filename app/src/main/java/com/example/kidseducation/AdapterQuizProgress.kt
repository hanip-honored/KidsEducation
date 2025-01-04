package com.example.kidseducation

import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.kidseducation.client.RetrofitClient
import com.example.kidseducation.response.quizcategory.QuizCategoryResponse
import com.example.kidseducation.response.quizcategory.QuizProgressResponse
import com.squareup.picasso.Picasso

class AdapterQuizProgress(private val listProgress:ArrayList<QuizProgressResponse>): RecyclerView.Adapter<AdapterQuizProgress.ViewHolder>() {

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val textNumber: TextView = v.findViewById(R.id.textNumber)
        val textCategory: TextView = v.findViewById(R.id.textCategory)
        val textProgress: TextView = v.findViewById(R.id.textProgress)
        val progress: ProgressBar = v.findViewById(R.id.progressBar)
        val relativeProgress: RelativeLayout = v.findViewById (R.id.progressContainer)

        fun bind(response: QuizProgressResponse, position: Int) {
            textNumber.text = (position + 1).toString()
            textCategory.text = response.nama_pelajaran
            textProgress.text = "${response.progress}/10"
            progress.progress = response.progress*10

            val colors = listOf("#FF90DE", "#FFFF86", "#FF7D2D")
            val color = android.graphics.Color.parseColor(colors[position % colors.size])

            val shadowDrawable = GradientDrawable().apply {
                setColor(android.graphics.Color.BLACK)
//                cornerRadius = 16f
            }

            val mainLayerDrawable = GradientDrawable().apply {
                setColor(color)
                cornerRadius = 32f
                setStroke(10, android.graphics.Color.BLACK)
            }

            val layerDrawable = LayerDrawable(arrayOf(shadowDrawable, mainLayerDrawable)).apply {
                setLayerInset(1, 0, 0, 12, 12) // Mengatur inset untuk main layer
            }

            relativeProgress.background = layerDrawable
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layout_quiz_progress, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listProgress.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listProgress[position], position)
    }


}