package com.example.test.ui.fragment

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.example.test.R
import com.example.test.data.product_list.Product
import com.example.test.utils.STATIC_REMOTE_URL
import kotlinx.android.synthetic.main.item_view_product.view.*

class ProductsAdapter(var productList: MutableList<Product>): RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    var loggedIn = false
    inner class ViewHolder(val item:View):RecyclerView.ViewHolder(item)

    var onClick: ((product:Product,position:Int)->Unit)? = null
    var onCheck: ((product:Product,isCheced:Boolean)->Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_view_product,parent,false).apply {

        })
    }

    override fun getItemCount(): Int {
       return productList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.item.apply {

            item_text_title.text = productList[position].title

            item_text_description.text = productList[position].text

            Glide.with(holder.item.context)
                .load(STATIC_REMOTE_URL+productList[position].img)
                .transition(withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(product_image)


            if (loggedIn){
                if (productList[position].save){
                    item_check_save.isChecked = true
                }
                item_check_save.visibility = View.VISIBLE
                item_check_save.setOnCheckedChangeListener { _, isChecked ->

                    productList[position].save = true
                    onCheck?.invoke(productList[position],isChecked)
                }
            }else{
                item_check_save.visibility = View.GONE
            }

            setOnClickListener {
                onClick?.invoke(productList[position],position)
            }
        }
    }

}