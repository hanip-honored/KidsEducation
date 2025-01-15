package com.example.kidseducation.response.user

data class AnswerRequest(
    val id_user: String,
    val id_kategori: String,
    val id_quiz: String,
    val jawaban: String,
    val isCorrect: Boolean
)
