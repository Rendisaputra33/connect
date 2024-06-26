package com.blanks.connect.service

import ENDPOINT_CREATE
import ENDPOINT_DELETE_STAF
import ENDPOINT_GET
import ENDPOINT_GET_STAFF
import ENDPOINT_UPDATE
import com.blanks.connect.data.EmptyResponse
import com.blanks.connect.data.ResponseData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface EmployeeService {
    @POST(ENDPOINT_CREATE)
    fun createEmployee(@Body body: HashMap<String, Any>): Call<EmptyResponse>

    @GET(ENDPOINT_GET)
    fun getEmployees(): Call<ResponseData>

    @POST(ENDPOINT_DELETE_STAF)
    fun deleteEmployee(@Body body: HashMap<String, Any>): Call<EmptyResponse>

    @POST(ENDPOINT_UPDATE)
    fun updateStaff(@Body body: HashMap<String, Any>): Call<EmptyResponse>

    @POST(ENDPOINT_GET_STAFF)
    fun getEmployee(@Body body: HashMap<String, Any>): Call<ResponseData>
}