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
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.example.test.R
import com.example.test.base.BaseFragment
import com.example.test.data.product_list.Product
import com.example.test.utils.STATIC_REMOTE_URL
import com.example.test.utils.isInternetAvailable
import com.example.test.view_model.MyViewModel
import kotlinx.android.synthetic.main.fragment_product.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.item_view_product.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class ProductFragment(private val product: Product):BaseFragment() {
    override fun update() {
        if(context?.let { it1 -> isInternetAvailable(it1) } == false){
            group_for_registered?.visibility = View.GONE
            product_no_internet?.visibility = View.GONE
        }
        viewMogel.getReview(product.id.toString())
    }

    private val viewMogel : MyViewModel by sharedViewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_product,null)
    }
    var onBack: ((Unit) -> Unit)? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        update()


        if (viewMogel.saveIt.contains(product.id)){
            product_check_save.isChecked = true
        }
        product_description.text = product.text
        context?.let { it1 ->
            Glide.with(it1)
                .load(STATIC_REMOTE_URL +product.img)
                .transition(withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(product_image_frag)
        }
        viewMogel.review.observe(this, Observer {

            var rate = 0
            it?.forEach{
               rate += it.rate
            }
            it?.let {list->
                product_rating.rating = (rate/list.size).toFloat()
            }


            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@ProductFragment.context,LinearLayoutManager.VERTICAL,true)
                it?.let {reviews->
                    adapter = ReviewAdapter(reviews)
                }
                scrollToPosition(0)

            }
        })

        product_fragment_back.setOnClickListener {
            onBack?.invoke(Unit)
        }


        product_add_review_button.setOnClickListener {
            viewMogel.addReview(product.id.toString(),my_product_rating.rating.toInt().toString(),my_comment.text.toString())
            my_product_rating.rating = 0f
            my_comment.setText("")
        }
        product_check_save.setOnCheckedChangeListener { compoundButton, isCheced ->
            if (isCheced){
                viewMogel.saveIt.add(product.id)
                viewMogel.saveProduct(product)

            }else{
                viewMogel.saveIt.remove(product.id)
                viewMogel.deleteProduct(product)

            }
        }

        viewMogel.userIsLoggedIn.observe(this,myObserver)
    }

    private val myObserver = Observer<Boolean>{
        if (it && context?.let { it1 -> isInternetAvailable(it1) } == true){
            group_for_registered.visibility = View.VISIBLE
        }

    }

    override fun onDestroy() {



        viewMogel.review.postValue(null)
        super.onDestroy()
    }
}