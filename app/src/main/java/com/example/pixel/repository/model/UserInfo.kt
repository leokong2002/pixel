package com.example.pixel.repository.model

import com.example.pixel.database.model.FollowedUser
import com.squareup.moshi.Json

data class Items(val items: List<UserInfo>)

data class UserInfo(
    @Json(name = "account_id") val accountId: Long,
    @Json(name = "reputation") val totalReputation: Long,
    @Json(name = "profile_image") val imageUrl: String,
    @Json(name = "display_name") val displayName: String,
)

data class UserInfoListData(
    val isLoading: Boolean = true,
    val hasError: Boolean = false,
    val formattedUserInfoList: List<FormattedUserInfo> = emptyList()
)

data class FollowUsersData(
    val isLoading: Boolean = true,
    val followedUsers: List<FollowedUser> = emptyList()
)

data class FormattedUserInfo(
    val isFollowed: Boolean = false,
    val accountId: Long = 0,
    val totalReputation: Long = 0,
    val imageUrl: String = "",
    val formattedDisplayName: String = "",
)
