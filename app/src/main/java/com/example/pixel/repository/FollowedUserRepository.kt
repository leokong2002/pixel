package com.example.pixel.repository

import com.example.pixel.database.dao.FollowedUserDao
import com.example.pixel.database.model.FollowedUser
import com.example.pixel.repository.model.FollowUsersData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

interface FollowedUserRepository {
    val followedUsersFlow: MutableStateFlow<FollowUsersData>

    suspend fun getFollowedUsers()

    suspend fun followUser(accountId: Long)

    suspend fun unfollowUser(accountId: Long)

    suspend fun unfollowAllUsers()
}

class FollowedUserRepositoryImpl @Inject constructor(
    private val followedUserDao: FollowedUserDao
) : FollowedUserRepository {

    override val followedUsersFlow = MutableStateFlow(FollowUsersData())

    override suspend fun getFollowedUsers() {
        followedUsersFlow.emit(
            FollowUsersData(
                isLoading = false,
                followedUsers = followedUserDao.getFollowedUsers()
            )
        )
    }

    override suspend fun followUser(accountId: Long) {
        followedUserDao.followUser(
            followedUser = FollowedUser(accountId = accountId)
        )
        getFollowedUsers()
    }

    override suspend fun unfollowUser(accountId: Long) {
        followedUserDao.unfollowUser(accountId)
        getFollowedUsers()
    }

    override suspend fun unfollowAllUsers() {
        followedUserDao.unfollowAllUsers()
        getFollowedUsers()
    }
}

@Module
@InstallIn(ViewModelComponent::class)
object FollowedUserRepositoryModule {
    @Provides
    fun provideFollowedUserRepository(
        followedUserDao: FollowedUserDao
    ): FollowedUserRepository {
        return FollowedUserRepositoryImpl(followedUserDao)
    }
}
