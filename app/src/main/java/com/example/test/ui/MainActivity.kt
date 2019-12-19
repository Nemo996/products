package com.example.test.ui

import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.test.R
import com.example.test.base.BaseFragment
import com.example.test.data.product_list.Product
import com.example.test.ui.fragment.LoginBottomSheetDialogFragment
import com.example.test.ui.fragment.ProductFragment
import com.example.test.ui.fragment.ProductsFragment
import com.example.test.ui.fragment.ProfileFragment
import com.example.test.utils.USERS_TOKEN_PREFS
import com.example.test.utils.isInternetAvailable
import com.example.test.view_model.MyViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
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
                addFragment(ProductsFragment().apply {
                    onClick = {product ->
                        Log.d("product","prod $product")
                        expandProduct(product)
                    }
                },"ProductsFragment")
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
        addFragment(ProductFragment(product).apply {
            onBack = {
                onBackPressed()
            }
        },"ProductFragment")
    }

    private val prodObserver = Observer<MutableList<Product>> {
        nothing_to_show.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(!isInternetAvailable(this)){
            Toast.makeText(this,"No Internet Connection. offline mode ON",Toast.LENGTH_LONG).show()
        }
        swipe_to_update.setOnRefreshListener {
            supportFragmentManager.fragments.last()?.let {
                if(it is BaseFragment){
                    it.update()
                }

            }
            swipe_to_update.isRefreshing = false
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
                navView.menu.getItem(1).isCheckable = true
            }else{
                navView.menu.getItem(1).isCheckable = false
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

        viewModel.showPrograss.observe(this, Observer {
            swipe_to_update.isRefreshing = it
        })



        navView.selectedItemId = R.id.navigation_products
    }


    private fun openProfile() {
        addFragment(ProfileFragment().apply {
            onLogout = {it->
                if (it){
                   onBackPressed()
                    // nav_view.selectedItemId = R.id.navigation_products
                }
            }
        },"ProfileFragment")
    }

    override fun onBackPressed() {
        Log.d("OnBackPressed","loginDialog ${loginDialog}")
            if (loginDialog!=null  ){

                if (loginDialog!!.isVisible){
                    Log.d("OnBackPressed","loginDialog!!.isVisible ${loginDialog!!.isVisible}")
                    loginDialog!!.onBackPressed()

                }
                loginDialog = null
                nav_view.selectedItemId = R.id.navigation_products
            }else if(supportFragmentManager.fragments.last() !is ProductsFragment){
                Log.d("OnBackPressed","not Products")
                nav_view?.selectedItemId = R.id.navigation_products
            }else{
                finish()
            }



    }

    fun addFragment(fragment:Fragment,tag: String){
        supportFragmentManager.fragments.forEach {
            Log.d("addFragment","${it.tag} ${fragment.javaClass.simpleName} ${supportFragmentManager.fragments.size}")
            if (it.tag == fragment.javaClass.simpleName ){
                supportFragmentManager.beginTransaction().remove(it).commit()   //avoid overlap
            }
        }
        supportFragmentManager.beginTransaction().replace(fragment_container.id,fragment,tag).commit()
    }

    private var loginDialog: LoginBottomSheetDialogFragment? = null
    private fun showBottomSheetLogin() {
        loginDialog = LoginBottomSheetDialogFragment().apply {
            onDismiss = {nav_view?.selectedItemId = R.id.navigation_products}
        }
        loginDialog?.show(
            supportFragmentManager,
            null
        )
    }


}
