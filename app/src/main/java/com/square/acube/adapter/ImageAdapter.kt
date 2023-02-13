package com.square.acube.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.square.acube.DetailsActivity
import com.square.acube.R
import com.square.acube.model.dashboard.Banner
import com.square.acube.network.RestConstants
import com.square.acube.utils.Constants


class ImageAdapter(
    private val context: Context,
    private val banner: ArrayList<Banner>,
    private val viewPager2: ViewPager2
) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.pager_row, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        Glide.with(context).load(RestConstants.BANNER_PATH + banner[position].image)
            .into(holder.imageView)
        holder.imageView.setOnClickListener {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra(Constants.ID, banner[position].videoid)
            context.startActivity(intent)
        }
        if (position == banner.size-1){
            viewPager2.post(runnable)
        }
    }

    override fun getItemCount(): Int {
        return banner.size
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView);
    }

    private val runnable = Runnable {
        banner.addAll(banner)
        notifyDataSetChanged()
    }
}