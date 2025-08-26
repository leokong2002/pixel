package com.example.pixel.repository.model

import com.squareup.moshi.Json

data class Items(val items: List<UserInfo>)

data class UserInfo(
    @Json(ignore = true) val isFollowed: Boolean = false,
    @Json(name = "account_id") val id: Long,
    @Json(name = "reputation") val totalReputation: Long,
    @Json(name = "profile_image") val imageUrl: String,
    @Json(name = "display_name") val displayName: String,
)
