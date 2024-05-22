package com.blanks.connect.data

import com.google.gson.annotations.SerializedName

data class Employee(
    @field:SerializedName("id")
    val id: String,
    @field:SerializedName("staf_name")
    val staf_name: String,
    @field:SerializedName("staf_alamat")
    val staf_alamat: String,
    @field:SerializedName("staf_hp")
    val staf_hp: String,
)
