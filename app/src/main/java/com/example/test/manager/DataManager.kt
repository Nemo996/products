package com.example.test.manager

import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkInfo
import androidx.core.content.ContextCompat
import com.example.test.data.product_list.Product
import com.example.test.di.local_datasource_module.DaoInterface
import com.example.test.di.network_module.ApiInterface
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

/**
 * repository
 */
class DataManager(private val context: Context,private val apiClient:ApiInterface, private val dao:DaoInterface):
    ConnectivityManager.NetworkCallback() {
    private var connected: Boolean = false

    override fun onAvailable(network: Network) {
        connected = true
        super.onAvailable(network)
    }

    override fun onUnavailable() {
        connected = false
        super.onUnavailable()
    }

    fun getProducts() = if (connected){
            apiClient.products()
        }else{
            dao.getAllSavedProducts()
        }

    fun register(username: String,password:String) = if (connected){
        apiClient.register(username,password)
    }else{
        Single.just(Exception("No internet connection"))
    }

    fun login(username: String,password:String) = if (connected){
        apiClient.register(username,password).map {
            it.token
        }.doOnSuccess {

        }
    }else{
        Single.just(Exception("No internet connection"))
    }


}