package com.example.test.manager

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.example.test.data.LoginResponce
import com.example.test.data.ReviewModel
import com.example.test.data.ReviewResponse
import com.example.test.data.UserProfile
import com.example.test.data.product_list.Product
import com.example.test.di.local_datasource_module.DaoInterface
import com.example.test.di.network_module.ApiInterface
import com.example.test.utils.isInternetAvailable
import io.reactivex.Single
import io.reactivex.SingleObserver


/**
 * repository
 */
class DataManager(private val context: Context,private val apiClient:ApiInterface, private val dao:DaoInterface){

    fun getProducts() = if (isInternetAvailable(context)){
            apiClient.products()
        }else{
            dao.getAllSavedProducts()
        }

    fun register(username: String,password:String) = if (isInternetAvailable(context)){
        apiClient.register(username,password)
    }else{
        object: Single<LoginResponce>() {
            override fun subscribeActual(observer: SingleObserver<in LoginResponce>) {
                observer.onError(Exception("No internet connection"))
            }

        }
    }

    fun login(username: String,password:String) = if (isInternetAvailable(context)){
        apiClient.logIn(username,password)/*.map {
            it.token
        }.doOnSuccess {

        }*/
    }else{
        object: Single<LoginResponce>() {
            override fun subscribeActual(observer: SingleObserver<in LoginResponce>) {
                observer.onError(Exception("No internet connection"))
            }

        }
    }

    fun getReview(id:String):Single<MutableList<ReviewModel>> = if (isInternetAvailable(context)){
        apiClient.getReview(id)
    }else{
        object: Single<MutableList<ReviewModel>>() {
            override fun subscribeActual(observer: SingleObserver<in MutableList<ReviewModel>>) {
                observer.onError(Exception("No internet connection"))
            }

        }
    }

    fun addReview(token: String, id:String,rate: String,comment: String) =if (isInternetAvailable(context)){
        apiClient.addReview("Token $token",id,rate,comment)
    }else{
        object: Single<ReviewResponse>() {
            override fun subscribeActual(observer: SingleObserver<in ReviewResponse>) {
                observer.onError(Exception("No internet connection"))
            }

        }
    }
    fun saveLocal(product: Product){
        dao.addProduct(product)
    }



    fun getUser():Single<UserProfile> = dao.getUser()
    fun cleanUser(user:UserProfile) = dao.deteteUser(user)
    fun updateUser(user:UserProfile) = dao.updateUser(user)
    fun getuserByUsername(username:String) = dao.getUserById(username)
    fun saveUser(user: UserProfile) = dao.addUser(user)
   /*   try {   dao.updateUser(user)
    }catch (e: Exception){
        e.printStackTrace()

    }*/
    fun deleteLocal(product: Product) {
        dao.deleteProduct(product)
    }

}