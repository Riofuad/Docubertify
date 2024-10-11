package com.riofuad.identitycardclassificationapp.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.riofuad.identitycardclassificationapp.data.response.PredictResponse
import com.riofuad.identitycardclassificationapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResultViewModel : ViewModel() {

    private val _predictResponse = MutableLiveData<PredictResponse>()
    val predictResponse: LiveData<PredictResponse> get() = _predictResponse

    private val _apiError = MutableLiveData<String>()
    val apiError: LiveData<String> get() = _apiError

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun sendTextToApi(recognizedText: String) {
        _loading.value = true
        val apiService = ApiConfig.getApiService()
        apiService.predict(recognizedText).enqueue(object : Callback<PredictResponse> {
            override fun onResponse(
                call: Call<PredictResponse>,
                response: Response<PredictResponse>
            ) {
                _loading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _predictResponse.value = response.body()!!
                } else {
                    _apiError.value = "Failed to get response"
                }
            }

            override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                _loading.value = false
                _apiError.value = "API call failed: ${t.message}"
            }
        })
    }
}