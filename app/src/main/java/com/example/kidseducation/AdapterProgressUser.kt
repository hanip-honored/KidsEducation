package com.example.kidseducation

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kidseducation.client.RetrofitClient
import com.example.kidseducation.response.quizcategory.QuizCategoryResponse
import com.squareup.picasso.Picasso

class AdapterProgressUser(private val listProgressUser:ArrayList<QuizCategoryResponse>): RecyclerView.Adapter<AdapterProgressUser.ViewHolder>() {

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val img: ImageView = v.findViewById(R.id.image_icon)
        val textPercentage: TextView = v.findViewById(R.id.text_percentage)
        val viewProgress: View = v.findViewById(R.id.view_progress)
        val strokeProgress: FrameLayout = v.findViewById(R.id.strokeProgress)

        fun bind(response: QuizCategoryResponse, position: Int) {
            val url = RetrofitClient.URL + response.image_kategori
            textPercentage.text = "${response.progress * 10}%"
            Picasso.get().load(url).into(img)

            val colors = listOf("#FECAC3", "#A2DDC2", "#FF8A00", "#FFE3C1", "#BAE3FF", "#E7E1FF")
            val color = Color.parseColor(colors[position % colors.size])

            val drawable = GradientDrawable()
            drawable.shape = GradientDrawable.RECTANGLE
            drawable.setColor(color)
            drawable.cornerRadius = 16f
            viewProgress.background = drawable

            val progressHeight = (response.progress / 10.0 * 550).toInt()
            val layoutParams = viewProgress.layoutParams
            layoutParams.height = progressHeight
            viewProgress.layoutParams = layoutParams

            val strokeDrawable = strokeProgress.background as GradientDrawable
            strokeDrawable.setStroke(8, color)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layout_progress, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listProgressUser.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listProgressUser[position], position)
    }
}