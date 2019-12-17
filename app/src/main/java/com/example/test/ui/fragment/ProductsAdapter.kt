package com.example.test.ui.fragment

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.test.R
import com.example.test.data.product_list.Product
import com.example.test.utils.STATIC_REMOTE_URL
import kotlinx.android.synthetic.main.item_view_product.view.*

class ProductsAdapter(private val productList: MutableList<Product>): RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {


    inner class ViewHolder(val item:View):RecyclerView.ViewHolder(item)

    var onClick: ((product:Product,position:Int)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_view_product,parent,false).apply {

          //layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT).apply {

        //  }
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
                .into(product_image)
            setOnClickListener {
                onClick?.invoke(productList[position],position)
            }
        }
    }

}