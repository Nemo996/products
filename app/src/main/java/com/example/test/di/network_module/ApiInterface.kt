package com.example.test.di.network_module

import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.POST

interface ApiInterface {


    @POST("/api/register/")
    fun register(@Field("username")username: String,@Field("password")password: String)//:Single

    @POST("/api/login/")
    fun logIn(@Field("username")username: String,@Field("password")password: String)//:Single
}