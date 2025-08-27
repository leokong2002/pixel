package com.example.pixel.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "followed_users")
data class FollowedUser(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val accountId: Long,
)
