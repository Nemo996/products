package com.example.test.view_model

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.test.data.ReviewModel
import com.example.test.data.UserProfile
import com.example.test.data.product_list.Product
import com.example.test.manager.DataManager
import com.example.test.utils.SET_TO_SAVE
import com.example.test.utils.SingleLiveEvent
import com.example.test.utils.USERS_TOKEN_PREFS
import com.example.test.utils.USER_ID
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MyViewModel(private val repository: DataManager,private val sharedPreferences: SharedPreferences): ViewModel() {
    private val disposable= CompositeDisposable()
    val saveIt = mutableSetOf<Int>()
    val products = MutableLiveData<MutableList<Product>>()
    val userInfo = MutableLiveData<UserProfile>()
    val userIsLoggedIn = MutableLiveData<Boolean>().apply { value = false }
    val showPrograss = MutableLiveData<Boolean>()
    val showAlertDialog = SingleLiveEvent<String?>()

    val review = MutableLiveData<MutableList<ReviewModel>>()


    fun loadProducts(){
        showPrograss.postValue(true)
        disposable.add( repository.getProducts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                sharedPreferences.getStringSet(SET_TO_SAVE,null)?.let {
                    setToInt(it)?.let { it1 -> saveIt.addAll(it1) }

                }
                it.forEach { product ->
                    Log.d("myViewModel","save ref $saveIt")
                    if (saveIt.contains(product.id)){
                        product.save = true
                    }

                    if (product.save){
                        saveIt.add(product.id)
                    }
                }
                showPrograss.postValue(false)
                products.postValue(it)
            },{
                showAlertDialog.callWithValue(it.message)
                showPrograss.postValue(false)
                it.printStackTrace()
            }))
    }
    fun register(username: String,password:String,name:String){

        showPrograss.postValue(true)
        disposable.add(repository.register(username,password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                if (!it.success){
                    showAlertDialog.callWithValue(it.message)
                }
                showPrograss.postValue(false)
                userIsLoggedIn.postValue(it.success)
                saveUser(UserProfile(USER_ID,username = username,name = name))
                sharedPreferences.edit().putString(USERS_TOKEN_PREFS,it.token).apply()
            },{
                showAlertDialog.callWithValue(it.message)
                showPrograss.postValue(false)
                it.printStackTrace()
            }))
    }
    fun login(username: String,password:String){

        showPrograss.postValue(true)
        disposable.add(repository.login(username,password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                if (!it.success){
                    showAlertDialog.callWithValue(it.message)
                }
                showPrograss.postValue(false)
                userIsLoggedIn.postValue(it.success)
                saveUser(UserProfile(USER_ID,username = username))
                sharedPreferences.edit().putString(USERS_TOKEN_PREFS,it.token).apply()
            },{
                showAlertDialog.callWithValue(it.message)
                showPrograss.postValue(false)
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
    fun addReview(id:String,rate:String,comment:String){

        showPrograss.postValue(true)
        sharedPreferences.getString(USERS_TOKEN_PREFS,"")?.let {
            disposable.add(
            repository.addReview(it,id,rate,comment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    showPrograss.postValue(false)
                   getReview(id)
                },{
                    showAlertDialog.callWithValue(it.message)
                    showPrograss.postValue(false)
                    it.printStackTrace()
                })
       ) }
    }

    fun logOut(){
        userIsLoggedIn.postValue(false)
        sharedPreferences.edit().putString(USERS_TOKEN_PREFS,"").apply()
    }

    fun getUser(){
        disposable.add(repository.getUser().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                userInfo.postValue(it)
            },{
                it.printStackTrace()
            }))
    }

    fun saveProduct(product: Product){
        repository.saveLocal(product.apply {
            this.save = true
        })
        sharedPreferences.edit().putStringSet(SET_TO_SAVE, setToString(saveIt)).apply()
    }

    fun deleteProduct(product: Product){
        repository.deleteLocal(product.apply {

        })
        sharedPreferences.edit().putStringSet(SET_TO_SAVE, setToString(saveIt)).apply()
    }
    fun saveUser(user:UserProfile){
        repository.saveUser(user)
    }
    fun cleanUser(user:UserProfile){
        repository.cleanUser(user)
    }


    override fun onCleared() {
        Log.d("myViewModel","onCleared")
        disposable.dispose()

        sharedPreferences.edit().putStringSet(SET_TO_SAVE,  setToString(saveIt)).apply()
        products.value?.forEach {
            saveIt.let {set->
                Log.d("myViewModel","save $set")
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

    fun setToString(setOfInt:MutableSet<Int>):MutableSet<String>?{
        var set = mutableSetOf<String>()
        setOfInt.forEach{
            set.add(it.toString())
        }
        return set
    }
    fun setToInt(setOfInt:MutableSet<String>):MutableSet<Int>?{
        var set = mutableSetOf<Int>()
        setOfInt.forEach{
            set.add(it.toInt())
        }
        return set
    }
}