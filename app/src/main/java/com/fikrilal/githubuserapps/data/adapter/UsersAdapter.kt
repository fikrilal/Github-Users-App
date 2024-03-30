package com.fikrilal.githubuserapps.data.adapter

import android.util.Log
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
import com.fikrilal.githubuserapps.data.response.ItemsItem

class UsersAdapter(
    private var users: List<ItemsItem>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    companion object {
        private const val TAG = "UsersAdapter"
    }

    var selectedPosition = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d(TAG, "Creating view holder for viewType: $viewType")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_list, parent, false)
        return ViewHolder(view).apply {
            Log.d(TAG, "View holder created")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "Binding view holder at position: $position")
        holder.bind(users[position], position == selectedPosition)
    }

    override fun getItemCount(): Int = users.size.apply {
        Log.d(TAG, "Getting item count: $this")
    }

    fun updateUsers(newUsers: List<ItemsItem>) {
        Log.d(TAG, "Updating users. New size: ${newUsers.size}. Users: ${newUsers.joinToString { it.login ?: "Unknown" }}")
        users = newUsers
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tv_username)
        private val tvUsername: TextView = itemView.findViewById(R.id.id_tv_username)
        private val imageView: ImageView = itemView.findViewById(R.id.img_avatar)

        init {
            itemView.setOnClickListener {
                onItemClick(users[adapterPosition].login.toString())
                if (selectedPosition != adapterPosition) {
                    notifyItemChanged(selectedPosition)
                    selectedPosition = adapterPosition
                    notifyItemChanged(selectedPosition)
                    Log.d(TAG, "Item at position $adapterPosition selected")
                }
            }
        }

        fun bind(item: ItemsItem, isSelected: Boolean) {
            Log.d(TAG, "Binding user: ${item.login}, isSelected: $isSelected")
            tvName.text = item.login
            tvUsername.text = item.url

            val shimmer = Shimmer.AlphaHighlightBuilder().setDuration(1800)
                .setBaseAlpha(0.7f).setHighlightAlpha(0.6f)
                .setDirection(Shimmer.Direction.RIGHT_TO_LEFT).setAutoStart(true)
                .build()
            val shimmerDrawable = ShimmerDrawable().apply { setShimmer(shimmer) }

            item.avatarUrl?.let { url ->
                Glide.with(itemView.context)
                    .load(url)
                    .placeholder(shimmerDrawable)
                    .error(R.drawable.default_avatar_background)
                    .into(imageView)
                Log.d(TAG, "Loading image URL for user ${item.login}")
            } ?: imageView.setImageResource(R.drawable.default_avatar_background)
        }
    }
}
