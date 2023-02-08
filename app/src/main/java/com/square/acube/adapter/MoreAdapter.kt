package com.square.acube.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.square.acube.R
import com.square.acube.network.RestConstants
import com.square.acube.model.dashboard.Category

class MoreAdapter(val context: Context?, val category: ArrayList<Category>) :
    RecyclerView.Adapter<MoreAdapter.ViewHolder>() {

    var onItemClick: ((position: Int) -> Unit)? = null

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_category, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.textCategory.text = category[position].title
        holder.imageCategory.setOnClickListener {
            onItemClick?.invoke(position)
        }

        Glide.with(context!!).load(RestConstants.CAT_PATH + category[position].icon)
            .into(holder.imageCategory)
        Log.e("TAG", "onBindViewHolder: ${RestConstants.CAT_PATH + category[position].icon}")

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return category.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textCategory: TextView = itemView.findViewById(R.id.textCategory)
        val imageCategory: ImageView = itemView.findViewById(R.id.imageCategory)
    }
}