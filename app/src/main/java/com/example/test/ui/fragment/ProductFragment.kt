package com.example.test.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.test.R
import com.example.test.base.BaseFragment
import com.example.test.data.product_list.Product
import com.example.test.utils.STATIC_REMOTE_URL
import com.example.test.view_model.MyViewModel
import kotlinx.android.synthetic.main.fragment_product.*
import kotlinx.android.synthetic.main.item_view_product.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class ProductFragment(private val product: Product):BaseFragment() {

    private val viewMogel : MyViewModel by sharedViewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewMogel.getReview(product.id.toString())
        return inflater.inflate(R.layout.fragment_product,null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let { it1 ->
            Glide.with(it1)
                .load(STATIC_REMOTE_URL +product.img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(product_image_frag)
        }
        viewMogel.review.observe(this, Observer {

            var rate = 0
            it?.forEach{
               rate += it.rate
            }

            product_rating.rating = (rate/it.size).toFloat()

            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@ProductFragment.context,LinearLayoutManager.VERTICAL,false)
                adapter = ReviewAdapter(it)
            }
        })
    }


    override fun onDestroy() {
        viewMogel.review.postValue(null)
        super.onDestroy()
    }
}