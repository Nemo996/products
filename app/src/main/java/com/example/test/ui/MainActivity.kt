package com.example.test.ui

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.test.R
import com.example.test.data.product_list.Product
import com.example.test.ui.fragment.LoginBottomSheetDialogFragment
import com.example.test.ui.fragment.ProductFragment
import com.example.test.ui.fragment.ProductsFragment
import com.example.test.ui.fragment.ProfileFragment
import com.example.test.utils.USERS_TOKEN_PREFS
import com.example.test.utils.isInternetAvailable
import com.example.test.view_model.MyViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_products.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity()  {

    private val viewModel: MyViewModel by viewModel()
    private val sharedPreferences :SharedPreferences by inject()
    private val BACKSTACK = "backstack"
    private var loggedIn = false


    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_products -> {
                supportFragmentManager.beginTransaction().replace(fragment_container.id,ProductsFragment().apply {
                    onClick = {product ->
                        Log.d("product","prod $product")
                        expandProduct(product)
                    }
                }).addToBackStack(BACKSTACK).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_login -> {
                if (loggedIn){
                    openProfile()
                }else{
                    showBottomSheetLogin()
                }

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    fun expandProduct(product:Product){
        supportFragmentManager.beginTransaction().replace(this@MainActivity.fragment_container.id,
            ProductFragment(product).apply {
                onBack = {
                    supportFragmentManager.popBackStack()
                }
            }
        ).addToBackStack(BACKSTACK).commit()
    }

    private val prodObserver = Observer<MutableList<Product>> {
        nothing_to_show.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)
        if(!isInternetAvailable(this)){
            Toast.makeText(this,"No Internet Connection. offline mode ON",Toast.LENGTH_LONG).show()
        }

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        if (!sharedPreferences.getString(USERS_TOKEN_PREFS,"").isNullOrEmpty()){
            viewModel.userIsLoggedIn.postValue(true)
        }
        nothing_to_show.visibility = View.VISIBLE
        viewModel.products.observe(this,prodObserver)

        viewModel.userIsLoggedIn.observe(this, Observer{
            loggedIn =it
            if(it){
                navView.menu.getItem(1).setIcon(R.drawable.ic_profile)
                navView.menu.getItem(1).setTitle(R.string.profile)

            }else{
                navView.menu.getItem(1).setIcon(R.drawable.ic_login)
                navView.menu.getItem(1).setTitle(R.string.login)
            }
        })
        viewModel.loadProducts()
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        viewModel.showAlertDialog.observe(this, Observer {

            AlertDialog.Builder(this).setIcon(android.R.drawable.stat_notify_error)
                .setMessage(it)
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                }).create().show()
        })
        val progressBar = AlertDialog.Builder(this).setView(ProgressBar(this)
        ).create()



        navView.selectedItemId = R.id.navigation_products
    }


    private fun openProfile() {
        supportFragmentManager.beginTransaction().replace(fragment_container.id,ProfileFragment().apply {
            onLogout = {it->
                if (it){
                    supportFragmentManager.popBackStack()
                   // nav_view.selectedItemId = R.id.navigation_products
                }
            }
        }).addToBackStack(BACKSTACK).commit()
    }


    private fun showBottomSheetLogin() {
        val loginDialog: LoginBottomSheetDialogFragment =
            LoginBottomSheetDialogFragment().apply {
            }
        loginDialog.show(
            supportFragmentManager,
            null
        )
    }


}
