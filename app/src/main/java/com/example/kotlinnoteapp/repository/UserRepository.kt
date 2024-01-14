package com.example.kotlinnoteapp.repository

import android.util.Log
import com.example.kotlinnoteapp.api.UserAPI
import com.example.kotlinnoteapp.models.UserRequest
import javax.inject.Inject

class UserRepository @Inject constructor(private val userAPI: UserAPI) {

    suspend fun registerUser(userRequest: UserRequest) {
        val response = userAPI.signup(userRequest)
        Log.d("response_test","${response.body().toString()}")
    }

    suspend fun loginUser(userRequest: UserRequest) {
        val response = userAPI.signin(userRequest)
        Log.d("response_test","${response.body().toString()}")
    }
}