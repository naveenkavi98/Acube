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
import com.square.acube.model.recentlywatched.RecentData
import com.square.acube.utils.CommonUtils

class RecentlyWaatchedAdapter(
    val context: Context,
    val data: List<RecentData>
) : RecyclerView.Adapter<RecentlyWaatchedAdapter.ViewHolder>() {

    var onItemClick: ((videoPosition: Int) -> Unit)? = null
    var onCloseClick: ((position: Int) -> Unit)? = null

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_favourite, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.filmValue.text = data[position].title
        holder.playValue.text=data[position].language!!.language_name

        context?.let {
            Glide.with(it).load(CommonUtils.getImageUrl(data[position].image1))
                .into(holder.imageview)
        }
        holder.play.setOnClickListener {
            onItemClick?.invoke(position)
        }
        holder.close.setOnClickListener {
            onCloseClick?.invoke(position)
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return data.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageview: ImageView = itemView.findViewById(R.id.imageview)
        val play: ImageView = itemView.findViewById(R.id.play)
        val close: ImageView = itemView.findViewById(R.id.close)
        val filmValue: TextView = itemView.findViewById(R.id.filmValue)
        val playValue: TextView = itemView.findViewById(R.id.playValue)

    }

}