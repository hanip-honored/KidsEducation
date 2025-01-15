package com.example.kidseducation.response.user

data class UserQuizResponse(
    val nama_pelajaran: String,
    val id_progress: String,
    val id_user: String,
    val id_kategori: String,
    val presentase: Int
)
