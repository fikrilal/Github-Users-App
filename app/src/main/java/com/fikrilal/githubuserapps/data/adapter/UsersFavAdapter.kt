package com.fikrilal.githubuserapps.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.fikrilal.githubuserapps.R
import com.fikrilal.githubuserapps.data.response.UsersFav
import com.google.android.material.card.MaterialCardView

class UsersFavAdapter(private var users: List<UsersFav>, private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<UsersFavAdapter.ViewHolder>() {
    inner class ViewHolder (itemmView: View) : RecyclerView.ViewHolder(itemmView) {
        fun bind(usersFav: UsersFav) {
            itemView.findViewById<MaterialCardView>(R.id.id_card_layout).setOnClickListener {
                onItemClick(usersFav.username)
            }
            val tvUsername = itemView.findViewById<TextView>(R.id.tv_username)
            tvUsername.text = usersFav.username
            val imageView = itemView.findViewById<ImageView>(R.id.img_avatar)
            val shimmer = Shimmer.AlphaHighlightBuilder()
                .setDuration(1800)
                .setBaseAlpha(0.7f)
                .setHighlightAlpha(0.6f)
                .setDirection(Shimmer.Direction.RIGHT_TO_LEFT)
                .setAutoStart(true)
                .build()
            val shimmerDrawable = ShimmerDrawable().apply {
                setShimmer(shimmer)
            }
            Glide.with(itemView.context)
                .load(usersFav.avatarUrl)
                .placeholder(shimmerDrawable)
                .into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size

    fun updateUsers(newUsers: List<UsersFav>) {
        this.users = newUsers
        notifyDataSetChanged()
    }
}