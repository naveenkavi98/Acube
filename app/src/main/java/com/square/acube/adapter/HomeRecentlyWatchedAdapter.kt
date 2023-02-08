package com.square.acube.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.square.acube.R
import com.square.acube.model.dashboard.Recently
import com.square.acube.utils.CommonUtils

class HomeRecentlyWatchedAdapter(
    val context: Context,
    val data: List<Recently>
) : RecyclerView.Adapter<HomeRecentlyWatchedAdapter.ViewHolder>() {

    var onItemClick: ((videoPosition: Int) -> Unit)? = null

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        Glide.with(context).load(CommonUtils.getImageUrl(data[position].image1))
            .into(holder.imageview)
        holder.imageview.setOnClickListener {
            onItemClick?.invoke(position)
        }
        holder.textFree.visibility = View.GONE

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return data.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageview: ImageView = itemView.findViewById(R.id.imageview)
        val textFree: TextView = itemView.findViewById(R.id.textFree)

    }

}