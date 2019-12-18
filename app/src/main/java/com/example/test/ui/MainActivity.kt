package com.example.test.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.test.R
import com.example.test.data.product_list.Product
import com.example.test.ui.fragment.LoginBottomSheetDialogFragment
import com.example.test.ui.fragment.ProductFragment
import com.example.test.ui.fragment.ProductsFragment
import com.example.test.view_model.MyViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_products.*
import org.koin.android.viewmodel.ext.android.viewModel



class MainActivity : AppCompatActivity() {

   val viewModel: MyViewModel by viewModel()
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_saved -> {
                supportFragmentManager.beginTransaction().replace(fragment_container.id,ProductsFragment(isSaved = true)).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_products -> {
                supportFragmentManager.beginTransaction().replace(fragment_container.id,ProductsFragment().apply {
                    onClick = {product ->
                        Log.d("product","prod $product")
                        expandProduct(product)
                    }
                }).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_login -> {
                showBottomSheetLogin()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    fun expandProduct(product:Product){
        supportFragmentManager.beginTransaction().replace(this@MainActivity.fragment_container.id,
            ProductFragment(product)
        ).commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        viewModel.loadProducts()
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        navView.menu.getItem(2).setIcon(R.drawable.ic_profile)
        navView.menu.getItem(2).setTitle(R.string.profile)
        navView.selectedItemId = R.id.navigation_products
    }


    fun showBottomSheetLogin() {
        val loginDialog: LoginBottomSheetDialogFragment =
            LoginBottomSheetDialogFragment().apply {
            }
        loginDialog.show(
            supportFragmentManager,
            null
        )
    }
}
