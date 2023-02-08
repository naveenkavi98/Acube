package com.ott.ottapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.square.acube.R
import com.square.acube.model.plan.Allplane

class SubscriptionAdapter(
    val context: Context,
    val allplane: List<Allplane>
) : RecyclerView.Adapter<SubscriptionAdapter.ViewHolder>() {

    var onItemClick: ((Position: Int) -> Unit)? = null

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_plan, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.planName.text = "${allplane[position].planname} ${allplane[position].amount}"
        holder.planNoOfLogins.text = allplane[position].logins
        if (allplane[position].activated!!) {
            holder.textSubscribe.text = "Subscribed"
        }
        if (allplane[position].casting == "1") {
            holder.planCasting.text = "YES"
        } else {
            holder.planCasting.text = "NO"
        }

        if (allplane[position].containads == "1") {
            holder.planAllContent.text = "YES"
        } else {
            holder.planAllContent.text = "NO"
        }

        if (allplane[position].planname == "Silver") {
            context.let {
                Glide.with(it).load(R.drawable.icn_silver)
                    .into(holder.imageview)
            }
        }
        if (allplane[position].planname == "Gold") {
            context.let {
                Glide.with(it).load(R.drawable.icn_gold)
                    .into(holder.imageview)
            }
        }
        if (allplane[position].planname == "Platinum") {
            context.let {
                Glide.with(it).load(R.drawable.icn_diamond)
                    .into(holder.imageview)
            }
        }


        holder.planSubscribeButton.setOnClickListener {
            onItemClick?.invoke(position)
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return allplane.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        val planName: TextView = itemView.findViewById(R.id.planName)
        val planNoOfLogins: TextView = itemView.findViewById(R.id.planNoOfLogins)
        val planAllContent: TextView = itemView.findViewById(R.id.planAllContent)
        val textSubscribe: TextView = itemView.findViewById(R.id.textSubscribe)
        val planCasting: TextView = itemView.findViewById(R.id.planCasting)
        val planSubscribeButton: LinearLayout = itemView.findViewById(R.id.planSubscribeButton)
        val imageview: ImageView = itemView.findViewById(R.id.imageview)

    }
}