package com.example.kidseducation.response.data

data class UserAnswerResponse(
    val id_pertanyaan: String,
    val jawaban: String,
    val is_correct: Int,
    val soal: String,
    val ulasan: String,
    val gambar_ulasan: String,
    val id_quiz: String
)
