package com.blanks.connect.data

import com.google.gson.annotations.SerializedName

data class ResponseData(
    @field:SerializedName("status")
    val status: String,
    @field:SerializedName("message")
    val message: String,
    @field:SerializedName("data")
    val data: List<Employee>
)
