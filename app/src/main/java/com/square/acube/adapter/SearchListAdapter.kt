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
import com.square.acube.model.search.Video
import com.square.acube.utils.CommonUtils

class SearchListAdapter(context: Context?, video: ArrayList<Video>) :
    RecyclerView.Adapter<SearchListAdapter.ViewHolder>() {

    var onItemClick: ((videoPosition: Int) -> Unit)? = null
    var list: ArrayList<Video> = ArrayList()
    var mcontext: Context? = null

    init {
        list.addAll(video)
        mcontext = context

    }// create new views

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

//        val ItemsViewModel = mList[position]

        // sets the image to the imageview from our itemHolder class
        holder.imageView.setOnClickListener {
            onItemClick?.invoke(position)
        }
        mcontext?.let {
            Glide.with(it).load(CommonUtils.getImageUrl(list[position].image1))
                .into(holder.imageView)
        }
        if (list[position].freepaid.equals("1")) {
            holder.textPaid.visibility = View.VISIBLE
            holder.textFree.visibility = View.GONE
        } else {
            holder.textPaid.visibility = View.GONE
            holder.textFree.visibility = View.VISIBLE
        }
        // sets the text to the textview from our itemHolder class
//        holder.textView.text = ItemsViewModel.text

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return list.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val textFree: TextView = itemView.findViewById(R.id.textFree)
        val textPaid: TextView = itemView.findViewById(R.id.textPaid)
    }
}