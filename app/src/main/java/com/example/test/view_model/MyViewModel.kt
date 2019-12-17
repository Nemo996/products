package com.example.test.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.test.data.product_list.Product
import com.example.test.manager.DataManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MyViewModel(private val repository: DataManager): ViewModel() {
    private val disposable= CompositeDisposable()
    val products = MutableLiveData<MutableList<Product>>()

    fun loadProducts(){
        disposable.add( repository.getProducts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                products.postValue(it)
            },{
                it.printStackTrace()
            }))
    }
    fun register(username: String,password:String){

    }
    fun login(username: String,password:String){

    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}