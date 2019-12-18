package com.example.test.ui.fragment

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.data.ReviewModel
import kotlinx.android.synthetic.main.item_view_review.view.*
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.text.SimpleDateFormat
import java.util.*


class ReviewAdapter(private val reviews:MutableList<ReviewModel>): RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

   private val inputDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",Locale.getDefault()).apply {
       timeZone = TimeZone.getTimeZone("UTC")
   }

    inner class ViewHolder(val item: View):RecyclerView.ViewHolder(item)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_view_review,parent,false))
    }

    override fun getItemCount(): Int {
       return reviews.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            item.name.text = reviews[position].created_by.username
            item.comment.text = reviews[position].text
            item.review_rate.rating = reviews[position].rate.toFloat()

            val date = inputDate.parse(reviews[position].created_at.substring(0,reviews[position].created_at.length-1)) //crash if you don't get substring



            item.date.text = date?.time?.let { DateUtils.getRelativeTimeSpanString(it) }

        }

    }
}