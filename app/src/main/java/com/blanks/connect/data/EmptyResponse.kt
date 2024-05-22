package com.blanks.connect.data

import com.google.gson.annotations.SerializedName

data class EmptyResponse(
    @field:SerializedName("status")
    val status: String,
    @field:SerializedName("message")
    val message: String,
)