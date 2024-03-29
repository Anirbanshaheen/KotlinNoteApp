package com.example.kotlinnoteapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kotlinnoteapp.api.UserAPI
import com.example.kotlinnoteapp.models.UserRequest
import com.example.kotlinnoteapp.models.UserResponse
import com.example.kotlinnoteapp.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userAPI: UserAPI) {

    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = _userResponseLiveData

    suspend fun registerUser(userRequest: UserRequest) {
        val response = userAPI.signup(userRequest)
        handleResponse(response)
    }

    suspend fun loginUser(userRequest: UserRequest) {
        val response = userAPI.signin(userRequest)
        handleResponse(response)
    }

    private suspend fun handleResponse(response: Response<UserResponse>) {
        if (response.isSuccessful && response.body() != null) {

            _userResponseLiveData.postValue(NetworkResult.SUCCESS(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

            _userResponseLiveData.postValue(NetworkResult.ERROR(errorObj.getString("message")))
        } else {
            _userResponseLiveData.postValue(NetworkResult.ERROR("Something went wrong"))
        }
    }
}