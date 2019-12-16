package com.example.test.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.example.test.R
import com.example.test.view_model.MyViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {
    val TAG = "SplashActivity"
    val viewModel: MyViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //viewModel.loadProducts()
      /*  viewModel.products.observe(this, Observer {
            Log.d(TAG,"products loaded $it")

        })*/
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}
