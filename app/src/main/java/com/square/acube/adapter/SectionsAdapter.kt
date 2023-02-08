package com.square.acube.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.square.acube.R
import com.square.acube.model.dashboard.Section

class SectionsAdapter(context: Context?, sections: ArrayList<Section>) :
    RecyclerView.Adapter<SectionsAdapter.ViewHolder>() {

    var onItemClick: ((position: Int, videoPosition: Int) -> Unit)? = null
    var onViewAllClick: ((position: Int) -> Unit)? = null
    var list: ArrayList<Section> = ArrayList()
    var mcontext: Context? = null

    init {
        list.addAll(sections)
        mcontext = context
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sections_itemview, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val section = list[position]

        holder.label.text = section.title
        holder.viewAll.setOnClickListener {
            onViewAllClick?.invoke(position)
        }
        val listAdapter = ListAdapter(mcontext, section.video)
        listAdapter.onItemClick = { videoPosition ->
            onItemClick?.invoke(position, videoPosition)
        }
        holder.videosRcv.adapter = listAdapter

        // sets the text to the textview from our itemHolder class
//        holder.textView.text = ItemsViewModel.text

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return list.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val viewAll: TextView = itemView.findViewById(R.id.viewAll)
        val label: TextView = itemView.findViewById(R.id.label)
        val videosRcv: RecyclerView = itemView.findViewById(R.id.videosRcv)
    }
}