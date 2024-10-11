package com.riofuad.identitycardclassificationapp.data.retrofit

import com.riofuad.identitycardclassificationapp.data.response.PredictResponse
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.http.Query

interface ApiService {
    @POST("/predict/")
    fun predict(
        @Query("text") text: String
    ): Call<PredictResponse>
}