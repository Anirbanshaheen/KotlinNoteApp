package com.example.kotlinnoteapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kotlinnoteapp.api.UserAPI
import com.example.kotlinnoteapp.models.UserRequest
import com.example.kotlinnoteapp.models.UserResponse
import com.example.kotlinnoteapp.utils.NetworkResult
import kotlinx.coroutines.flow.MutableSharedFlow
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userAPI: UserAPI) {

    // todo new flow structure
    //val userResponseObserver = MutableSharedFlow<NetworkResult<UserResponse>>()


    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = _userResponseLiveData

    suspend fun registerUser(userRequest: UserRequest) {
        // todo new flow structure
        //userResponseObserver.emit(NetworkResult.LOADING())

        val response = userAPI.signup(userRequest)
        handleResponse(response)
    }

    suspend fun loginUser(userRequest: UserRequest) {
        val response = userAPI.signin(userRequest)
        handleResponse(response)
    }

    private suspend fun handleResponse(response: Response<UserResponse>) {
        if (response.isSuccessful && response.body() != null) {
            // todo new flow structure
            //userResponseObserver.emit(NetworkResult.SUCCESS(response.body()!!))

            _userResponseLiveData.postValue(NetworkResult.SUCCESS(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

            _userResponseLiveData.postValue(NetworkResult.ERROR(errorObj.getString("message")))
        } else {
            _userResponseLiveData.postValue(NetworkResult.ERROR("Something went wrong"))
        }
    }
}