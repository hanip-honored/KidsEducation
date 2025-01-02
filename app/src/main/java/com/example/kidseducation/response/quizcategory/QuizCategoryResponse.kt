package com.example.kidseducation.response.quizcategory

data class QuizCategoryResponse(
    val id_kategori: String,
    val nama_pelajaran: String,
    val image_kategori: String,
    val progress: Int,
    val tingkat_kesulitan: String
)
