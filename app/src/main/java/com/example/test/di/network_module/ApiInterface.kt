package com.example.test.di.network_module

import com.example.test.data.LoginResponce
import com.example.test.data.product_list.Product
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiInterface {


    @POST("/api/register/")
    fun register(@Field("username")username: String,@Field("password")password: String):Single<LoginResponce>

    @POST("/api/login/")
    fun logIn(@Field("username")username: String,@Field("password")password: String):Single<LoginResponce>

    @GET("/api/products/")
    fun products():Single<MutableList<Product>>

    @POST("/api/reviews/")
    fun addReview(@Url id:String)
}