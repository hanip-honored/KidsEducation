package com.example.kidseducation.response.account

data class RegisterRequest(
    val nickname: String,
    val email: String,
    val password: String
)
