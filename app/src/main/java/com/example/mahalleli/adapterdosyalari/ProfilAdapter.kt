package com.example.mahalleli.adapterdosyalari

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mahalleli.databinding.ItemLogoutBinding
import com.example.mahalleli.databinding.ItemSellerOptionBinding
import com.example.mahalleli.databinding.ItemSellerReviewsBinding
import com.example.mahalleli.databinding.ItemUserActionsBinding
import com.example.mahalleli.databinding.ItemUserInfoBinding

class ProfilAdapter(
    private val profileItems: List<ProfileItem>,
    private val actionListener: (ProfileItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_USER_INFO = 0
        private const val VIEW_TYPE_USER_ACTIONS = 1
        private const val VIEW_TYPE_SELLER_REVIEWS = 2
        private const val VIEW_TYPE_SELLER_OPTION = 3
        private const val VIEW_TYPE_LOGOUT = 4
    }

    override fun getItemViewType(position: Int): Int {
        return when (profileItems[position]) {
            is ProfileItem.UserInfo -> VIEW_TYPE_USER_INFO
            is ProfileItem.UserActions -> VIEW_TYPE_USER_ACTIONS
            is ProfileItem.SellerReviews -> VIEW_TYPE_SELLER_REVIEWS
            is ProfileItem.SellerOption -> VIEW_TYPE_SELLER_OPTION
            is ProfileItem.Logout -> VIEW_TYPE_LOGOUT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_USER_INFO -> {
                val binding = ItemUserInfoBinding.inflate(inflater, parent, false)
                UserInfoViewHolder(binding)
            }
            VIEW_TYPE_USER_ACTIONS -> {
                val binding = ItemUserActionsBinding.inflate(inflater, parent, false)
                UserActionsViewHolder(binding)
            }
            VIEW_TYPE_SELLER_REVIEWS -> {
                val binding = ItemSellerReviewsBinding.inflate(inflater, parent, false)
                SellerReviewsViewHolder(binding)
            }
            VIEW_TYPE_SELLER_OPTION -> {
                val binding = ItemSellerOptionBinding.inflate(inflater, parent, false)
                SellerOptionViewHolder(binding)
            }
            VIEW_TYPE_LOGOUT -> {
                val binding = ItemLogoutBinding.inflate(inflater, parent, false)
                LogoutViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return profileItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = profileItems[position]
        when (holder) {
            is UserInfoViewHolder -> holder.bind(item as ProfileItem.UserInfo)
            is UserActionsViewHolder -> holder.bind(item as ProfileItem.UserActions, actionListener)
            is SellerReviewsViewHolder -> holder.bind(item as ProfileItem.SellerReviews)
            is SellerOptionViewHolder -> holder.bind(item as ProfileItem.SellerOption, actionListener)
            is LogoutViewHolder -> holder.bind(item as ProfileItem.Logout, actionListener)
        }
    }

    // Her bir view type için ayrı ViewHolder sınıfları
    inner class UserInfoViewHolder(val binding: ItemUserInfoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(userInfo: ProfileItem.UserInfo) {
            binding.userNickname.text = userInfo.userName
            binding.userBio.text = userInfo.bio
            Glide.with(binding.userImage.context)
                .load(userInfo.avatarUrl)
                .into(binding.userImage)
        }
    }

    inner class UserActionsViewHolder(val binding: ItemUserActionsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(userActions: ProfileItem.UserActions, actionListener: (ProfileItem) -> Unit) {
            binding.useractionText.text = userActions.actionText
            binding.root.setOnClickListener {
                actionListener(userActions)
            }
        }
    }

    inner class SellerReviewsViewHolder(val binding: ItemSellerReviewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(sellerReviews: ProfileItem.SellerReviews) {
            val allReviews = sellerReviews.reviews.joinToString ("\n")
            binding.sellerreviewsText.text = allReviews
        }
    }

    // Yeni eklenen ViewHolder sınıfları
    inner class SellerOptionViewHolder(val binding: ItemSellerOptionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(sellerOption: ProfileItem.SellerOption, actionListener: (ProfileItem) -> Unit) {
            binding.root.setOnClickListener {
                actionListener(sellerOption)
            }
        }
    }

    inner class LogoutViewHolder(val binding: ItemLogoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(logout: ProfileItem.Logout, actionListener: (ProfileItem) -> Unit) {
            binding.logoutText.text = logout.logoutText
            binding.root.setOnClickListener {
                actionListener(logout)
            }
        }
    }
}