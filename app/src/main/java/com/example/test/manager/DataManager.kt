package com.example.test.manager

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import com.example.test.data.LoginResponce
import com.example.test.data.ReviewModel
import com.example.test.data.ReviewResponse
import com.example.test.data.product_list.Product
import com.example.test.di.local_datasource_module.DaoInterface
import com.example.test.di.network_module.ApiInterface
import io.reactivex.Single
import io.reactivex.SingleObserver
import retrofit2.http.Field
import retrofit2.http.Header
import retrofit2.http.Path


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
        apiClient.addReview(token,id,rate,comment)
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

    private fun isInternetAvailable(context: Context): Boolean {
        var result = false
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                }
            }
        } else {
            @Suppress("DEPRECATION")
            cm?.run {
                cm.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        result = true
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        result = true
                    }
                }
            }
        }
        return result
    }

}