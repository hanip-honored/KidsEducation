package com.example.kidseducation.response.data

data class QuizQuestionResponse(
    val id_pertanyaan: String,
    val id_quiz: String,
    val soal: String,
    val jawaban: String,
    val jawaban_salah: String,
    val image: String,
    val ulasan: String
)
