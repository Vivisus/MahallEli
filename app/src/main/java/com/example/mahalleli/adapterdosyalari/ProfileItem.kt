package com.example.mahalleli.adapterdosyalari

sealed class ProfileItem {

    data class UserInfo(val avatarUrl: String, val userName: String, val bio: String) : ProfileItem()
    data class UserActions(val actionText: String) : ProfileItem()
    data class SellerReviews(val reviews: List<String>) : ProfileItem()
    data class SellerOption(val isSeller: Boolean) : ProfileItem()
    data class Logout(val logoutText: String) : ProfileItem()
}