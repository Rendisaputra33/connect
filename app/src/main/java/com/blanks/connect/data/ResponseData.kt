package com.blanks.connect.data

data class ResponseData<T>(
    val status: String,
    val message: String,
    val data: T
)
