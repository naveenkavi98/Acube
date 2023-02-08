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
import com.square.acube.model.viewall.VideoViewAll
import com.square.acube.utils.CommonUtils

class ViewAllAdapter(
    val context: Context,
    val videoViewAll: List<VideoViewAll>
) : RecyclerView.Adapter<ViewAllAdapter.ViewHolder>() {

    var onItemClick: ((videoPosition: Int) -> Unit)? = null

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design_for_grid, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.imageView.setOnClickListener {
            onItemClick?.invoke(position)
        }

        context?.let {
            Glide.with(it).load(CommonUtils.getImageUrl(videoViewAll[position].image1))
                .into(holder.imageView)
        }
        if (videoViewAll[position].freepaid.equals("1")) {
            holder.textPaid.visibility = View.VISIBLE
            holder.textFree.visibility = View.GONE
        } else {
            holder.textPaid.visibility = View.GONE
            holder.textFree.visibility = View.VISIBLE
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return videoViewAll.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textFree: TextView = itemView.findViewById(R.id.textFree)
        val textPaid: TextView = itemView.findViewById(R.id.textPaid)
    }
}