package com.example.kotlinnoteapp.models

data class UserRequest(
    val email: String,
    val password: String,
    val username: String
)