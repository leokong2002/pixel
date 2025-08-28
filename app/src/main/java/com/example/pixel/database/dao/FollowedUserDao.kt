package com.example.pixel.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.pixel.database.model.FollowedUser

@Dao
interface FollowedUserDao {
    @Query("SELECT * FROM followed_users")
    fun getFollowedUsers(): List<FollowedUser>

    @Insert
    suspend fun followUser(followedUser: FollowedUser)

    @Query("DELETE FROM followed_users WHERE accountId = :accountId")
    suspend fun unfollowUser(accountId: Long)

    @Query("DELETE FROM followed_users")
    suspend fun unfollowAllUsers()
}
