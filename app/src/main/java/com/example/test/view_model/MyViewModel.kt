package com.example.test.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.test.data.product_list.Product
import com.example.test.manager.DataManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MyViewModel(val repository: DataManager): ViewModel() {

    val products = MutableLiveData<MutableList<Product>>()

    fun loadProducts(){
        repository.getProducts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                products.postValue(it)
            },{
                it.printStackTrace()
            })
    }

}