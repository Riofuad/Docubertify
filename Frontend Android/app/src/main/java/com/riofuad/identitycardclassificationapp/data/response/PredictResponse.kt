package com.riofuad.identitycardclassificationapp.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PredictResponse(
    @field:SerializedName("prediction")
    val prediction: String,

    @field:SerializedName("gemini_response")
    val geminiResponse: GeminiResponse
) : Parcelable

@Parcelize
data class GeminiResponse(
    @field:SerializedName("document_type")
    val documentType: String,

    @field:SerializedName("data")
    val data: Map<String, String>
) : Parcelable
