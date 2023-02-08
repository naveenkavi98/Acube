package com.square.acube.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.square.acube.R
import com.square.acube.model.search.RecentSearchData
import com.square.acube.utils.CommonUtils

class RecentSearchAdapter(context: Context?, recentSearchData: ArrayList<RecentSearchData>) :
    RecyclerView.Adapter<RecentSearchAdapter.ViewHolder>() {

    var onItemClick: ((videoPosition: Int) -> Unit)? = null
    var list: ArrayList<RecentSearchData> = ArrayList()
    var mcontext: Context? = null

    init {
        list.addAll(recentSearchData)
        mcontext = context

    }// create new views

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.simple_movie_list_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.layoutMovieList.setOnClickListener {
            onItemClick?.invoke(position)
        }
        mcontext?.let {
            Glide.with(it).load(CommonUtils.getImageUrl(list[position].image1))
                .into(holder.imageView)
        }
        holder.textRecentName.text = list[position].title
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
        val textRecentName: TextView = itemView.findViewById(R.id.textRecentName)
        val layoutMovieList: ConstraintLayout = itemView.findViewById(R.id.layoutMovieList)
    }
}