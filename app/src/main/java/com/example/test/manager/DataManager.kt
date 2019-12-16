package com.example.test.manager

import com.example.test.data.product_list.Product
import com.example.test.di.network_module.ApiInterface
import io.reactivex.Single

/**
 * repository
 */
class DataManager(val apiClient:ApiInterface) {


fun getProducts(): Single<MutableList<Product>>{
    return apiClient.products()
}

}