package com.example.test.view_model

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.test.data.ReviewModel
import com.example.test.data.UserProfile
import com.example.test.data.product_list.Product
import com.example.test.manager.DataManager
import com.example.test.utils.USERS_TOKEN_PREFS
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MyViewModel(private val repository: DataManager,private val sharedPreferences: SharedPreferences): ViewModel() {
    private val disposable= CompositeDisposable()
    val saveIt = mutableSetOf<Int>()
    val products = MutableLiveData<MutableList<Product>>()
    val userInfo = MutableLiveData<UserProfile>()
    val userIsLoggedIn = MutableLiveData<Boolean>().apply { value = false }

    val review = MutableLiveData<MutableList<ReviewModel>>()


    fun loadProducts(){
        disposable.add( repository.getProducts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                it.forEach { product ->
                    if (product.save){
                        saveIt.add(product.id)
                    }
                }
                products.postValue(it)
            },{
                it.printStackTrace()
            }))
    }
    fun register(username: String,password:String){
        disposable.add(repository.register(username,password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                userIsLoggedIn.postValue(it.success)
                sharedPreferences.edit().putString(USERS_TOKEN_PREFS,it.token).apply()
            },{
                it.printStackTrace()
            }))
    }
    fun login(username: String,password:String){
        disposable.add(repository.login(username,password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                userIsLoggedIn.postValue(it.success)
                sharedPreferences.edit().putString(USERS_TOKEN_PREFS,it.token).apply()
            },{
                it.printStackTrace()
            }))
    }

    fun getReview(id:String){
        disposable.add(repository.getReview(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                review.postValue(it)
            },{
                it.printStackTrace()
            }))
    }

    fun getUser(){

    }

    fun saveUser(){

    }
    fun cleanUser(){

    }


    override fun onCleared() {
        disposable.dispose()
        products.value?.forEach {
            saveIt.let {set->
                if(!set.contains(it.id) ){
                    repository.deleteLocal(it)
                }else{
                    repository.saveLocal(it.apply {
                        this.save = true
                    })
                }
            }

        }

        super.onCleared()
    }
}