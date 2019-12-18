package com.example.test.di.network_module

import com.example.test.data.LoginResponce
import com.example.test.data.ReviewModel
import com.example.test.data.ReviewResponse
import com.example.test.data.product_list.Product
import io.reactivex.Single
import retrofit2.http.*

interface ApiInterface {

    @FormUrlEncoded
    @POST("/api/register/")
    fun register(@Field("username")username: String,@Field("password")password: String):Single<LoginResponce>

    @FormUrlEncoded
    @POST("/api/login/")
    fun logIn(@Field("username")username: String,@Field("password")password: String):Single<LoginResponce>

    @GET("/api/products/")
    fun products():Single<MutableList<Product>>

    @GET("/api/reviews/{id}")
    fun getReview(@Path("id") id:String):Single<MutableList<ReviewModel>>

    @FormUrlEncoded
    @POST("/api/reviews/{id}")
    fun addReview(@Header("Authorization") token: String,@Path("id") id:String,@Field("rate")rate: String,@Field("text")comment: String):Single<ReviewResponse>
}