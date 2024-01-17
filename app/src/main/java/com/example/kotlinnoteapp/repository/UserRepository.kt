package com.example.kotlinnoteapp.repository

import com.example.kotlinnoteapp.api.UserAPI
import com.example.kotlinnoteapp.models.UserRequest
import com.example.kotlinnoteapp.models.UserResponse
import com.example.kotlinnoteapp.utils.NetworkResult
import kotlinx.coroutines.flow.MutableSharedFlow
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userAPI: UserAPI) {

    val userResponseObserver = MutableSharedFlow<NetworkResult<UserResponse>>()

    suspend fun registerUser(userRequest: UserRequest) {
        userResponseObserver.emit(NetworkResult.LOADING())

        val response = userAPI.signup(userRequest)
        handleResponse(response)
    }

    suspend fun loginUser(userRequest: UserRequest) {
        val response = userAPI.signin(userRequest)
        handleResponse(response)
    }

    private suspend fun handleResponse(response: Response<UserResponse>) {
        if (response.isSuccessful && response.body() != null) {
            userResponseObserver.emit(NetworkResult.SUCCESS(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

            userResponseObserver.emit(NetworkResult.ERROR(errorObj.getString("message")))
        } else {
            userResponseObserver.emit(NetworkResult.ERROR("Something went wrong"))
        }
    }
}