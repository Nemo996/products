package com.example.test.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.test.R
import com.example.test.base.BaseFragment
import com.example.test.data.product_list.Product
import com.example.test.view_model.MyViewModel
import kotlinx.android.synthetic.main.fragment_products.*
import org.koin.android.viewmodel.ext.android.sharedViewModel



class ProductsFragment(private val isSaved: Boolean = false): BaseFragment() {

    private val viewMogel :MyViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_products,null)
    }
    var onClick: ((product: Product)->Unit)? = null

    val progressObserver = Observer<Boolean> {
        pool_to_refresh_products.isRefreshing = it
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewMogel.userIsLoggedIn.observe(this,myObserver)
        viewMogel.showPrograss.observe(this, progressObserver)

        viewMogel.products.observe(this, Observer {

            pool_to_refresh_products.isRefreshing = false
            product_list.apply {
                layoutManager = LinearLayoutManager(this.context,LinearLayoutManager.VERTICAL,false)
                adapter = ProductsAdapter(it).apply {
                     viewMogel.userIsLoggedIn.value?.let {
                        loggedIn =it
                    }
                    onClick = {product,position->

                        this@ProductsFragment.onClick?.invoke(product)
                    }
                    onCheck = {product, isCheced ->
                        if (isCheced){
                            viewMogel.saveIt.add(product.id)
                            viewMogel.saveProduct(product)

                        }else{
                            viewMogel.saveIt.remove(product.id)
                            viewMogel.deleteProduct(product)

                        }
                    }
                }
            }
        })
        pool_to_refresh_products.setOnRefreshListener {
            viewMogel.loadProducts()
        }

    }

    override fun onResume() {
        viewMogel.loadProducts()
        super.onResume()
    }
    private val myObserver = Observer<Boolean> {
            product_list?.adapter?.let {adapter ->
                (adapter as ProductsAdapter).loggedIn = it
                adapter.notifyDataSetChanged()

            }

    }

}