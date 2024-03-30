package com.fikrilal.githubuserapps.data.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fikrilal.githubuserapps.R
import com.fikrilal.githubuserapps.data.response.UserFollowersResponseItem
import com.facebook.shimmer.ShimmerDrawable
import com.facebook.shimmer.Shimmer

class FollowersAdapter(
    private var followers: List<UserFollowersResponseItem>,
    private val onFollowerClick: (String) -> Unit
) : RecyclerView.Adapter<FollowersAdapter.FollowerViewHolder>() {

    companion object {
        private const val TAG = "FollowersAdapter"
    }

    var selectedPosition = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerViewHolder {
        Log.d(TAG, "Creating ViewHolder")
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_list, parent, false)
        return FollowerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FollowerViewHolder, position: Int) {
        Log.d(TAG, "Binding ViewHolder at position: $position")
        holder.bind(followers[position], position == selectedPosition)
    }

    override fun getItemCount(): Int = followers.size

    fun updateFollowers(newFollowers: List<UserFollowersResponseItem>) {
        Log.d(TAG, "Updating followers list with ${newFollowers.size} items")
        followers = newFollowers
        notifyDataSetChanged()
    }

    inner class FollowerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val usernameTextView: TextView = itemView.findViewById(R.id.tv_username)
        private val avatarImageView: ImageView = itemView.findViewById(R.id.img_avatar)
        private val userUrlTextView: TextView = itemView.findViewById(R.id.id_tv_username)

        init {
            itemView.setOnClickListener {
                followers[adapterPosition].login?.let { login ->
                    onFollowerClick(login)
                    Log.d(TAG, "Follower clicked: $login")
                    if (selectedPosition != adapterPosition) {
                        notifyItemChanged(selectedPosition)
                        selectedPosition = adapterPosition
                        notifyItemChanged(selectedPosition)
                    }
                }
            }
        }

        fun bind(follower: UserFollowersResponseItem, isSelected: Boolean) {
            usernameTextView.text = follower.login
            userUrlTextView.text = follower.url

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
                .load(follower.avatarUrl)
                .placeholder(shimmerDrawable)
                .into(avatarImageView)

            Log.d(TAG, "Follower bound to ViewHolder: ${follower.login}")
        }
    }
}