package com.example.test.manager

import com.example.test.data.product_list.Product
import com.example.test.di.local_datasource_module.DaoInterface
import com.example.test.di.network_module.ApiInterface
import io.reactivex.Single

/**
 * repository
 */
class DataManager(val apiClient:ApiInterface, dao:DaoInterface) {


fun getProducts(): Single<MutableList<Product>>{



    return apiClient.products()
}

}