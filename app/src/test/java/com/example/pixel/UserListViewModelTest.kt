package com.example.pixel

import com.example.pixel.database.model.FollowedUser
import com.example.pixel.repository.FollowedUserRepository
import com.example.pixel.repository.StackOverflowRepository
import com.example.pixel.repository.model.FollowUsersData
import com.example.pixel.repository.model.FormattedUserInfo
import com.example.pixel.repository.model.UserInfoListData
import com.example.pixel.userlist.UserListViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

class UserListViewModelTest {

    private val expectedListWithFollowedUser = listOf(
        FormattedUserInfo(
            isFollowed = false,
            accountId = 123453,
            totalReputation = 22,
            imageUrl = "www.image.com/image",
            formattedDisplayName = "Elo",
        ),
        FormattedUserInfo(
            isFollowed = false,
            accountId = 123452,
            totalReputation = 1,
            imageUrl = "www.image.com/image",
            formattedDisplayName = "Ole",
        ),
        FormattedUserInfo(
            isFollowed = true,
            accountId = 123451,
            totalReputation = 9999999,
            imageUrl = "www.image.com/image",
            formattedDisplayName = "Leó",
        )
    )

    private val expectedListWithNoFollowedUsers = listOf(
        FormattedUserInfo(
            isFollowed = false,
            accountId = 123453,
            totalReputation = 22,
            imageUrl = "www.image.com/image",
            formattedDisplayName = "Elo",
        ),
        FormattedUserInfo(
            isFollowed = false,
            accountId = 123452,
            totalReputation = 1,
            imageUrl = "www.image.com/image",
            formattedDisplayName = "Ole",
        ),
        FormattedUserInfo(
            isFollowed = false,
            accountId = 123451,
            totalReputation = 9999999,
            imageUrl = "www.image.com/image",
            formattedDisplayName = "Leó",
        )
    )

    private val userInfoListData = UserInfoListData(
        isLoading = false,
        hasError = false,
        formattedUserInfoList = listOf(
            FormattedUserInfo(
                isFollowed = false,
                accountId = 123453,
                totalReputation = 22,
                imageUrl = "www.image.com/image",
                formattedDisplayName = "Elo",
            ),
            FormattedUserInfo(
                isFollowed = false,
                accountId = 123452,
                totalReputation = 1,
                imageUrl = "www.image.com/image",
                formattedDisplayName = "Ole",
            ),
            FormattedUserInfo(
                isFollowed = true,
                accountId = 123451,
                totalReputation = 9999999,
                imageUrl = "www.image.com/image",
                formattedDisplayName = "Leó",
            )
        )
    )

    private val followUsersData = FollowUsersData(isLoading = false, followedUsers = listOf(FollowedUser(id = 0, accountId = 123451)))

    object FakeStackOverflowRepository : StackOverflowRepository {
        override val userInfoListDataFlow: MutableStateFlow<UserInfoListData> = MutableStateFlow(UserInfoListData())
        override suspend fun getUsers() {}
        suspend fun emit(value: UserInfoListData) = userInfoListDataFlow.emit(value)
    }

    object FakeFollowedUserRepository : FollowedUserRepository {
        override val followedUsersFlow: MutableStateFlow<FollowUsersData> = MutableStateFlow(FollowUsersData())
        suspend fun emit(value: FollowUsersData) = followedUsersFlow.emit(value)
        override suspend fun getFollowedUsers() {}
        override suspend fun followUser(accountId: Long) {}
        override suspend fun unfollowUser(accountId: Long) {}
        override suspend fun unfollowAllUsers() {}
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `VM should map user info list correctly where there are no users and no followed users`() = runTest {
        val userListViewModel = UserListViewModel(stackOverflowRepository = FakeStackOverflowRepository, followedUserRepository = FakeFollowedUserRepository)
        val results = Channel<UserListViewModel.UiState>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            userListViewModel.state.collect {
                results.send(it)
            }
        }
        FakeStackOverflowRepository.emit(UserInfoListData(isLoading = false, hasError = true))
        FakeFollowedUserRepository.emit(FollowUsersData(isLoading = false))

        val result1 = results.receive()
        val result2 = results.receive()
        assertEquals(emptyList<FormattedUserInfo>(), result1.formattedUserInfoList)
        assertEquals(emptyList<FormattedUserInfo>(), result2.formattedUserInfoList)
        assertEquals(0, result2.usersCount)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `VM should map user info list correctly where there are users but no followed users`() = runTest {
        val userListViewModel = UserListViewModel(stackOverflowRepository = FakeStackOverflowRepository, followedUserRepository = FakeFollowedUserRepository)
        val results = Channel<UserListViewModel.UiState>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            userListViewModel.state.collect {
                results.send(it)
            }
        }
        FakeStackOverflowRepository.emit(userInfoListData)
        FakeFollowedUserRepository.emit(FollowUsersData(isLoading = false))

        val result1 = results.receive()
        val result2 = results.receive()
        assertEquals(emptyList<FormattedUserInfo>(), result1.formattedUserInfoList)
        assertEquals(expectedListWithNoFollowedUsers, result2.formattedUserInfoList)
        assertEquals(3, result2.usersCount)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `VM should map user info list correctly where there are users and followed users`() = runTest {
        val userListViewModel = UserListViewModel(stackOverflowRepository = FakeStackOverflowRepository, followedUserRepository = FakeFollowedUserRepository)
        val results = Channel<UserListViewModel.UiState>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            userListViewModel.state.collect {
                results.send(it)
            }
        }
        FakeStackOverflowRepository.emit(userInfoListData)
        FakeFollowedUserRepository.emit(followUsersData)

        val result1 = results.receive()
        val result2 = results.receive()
        assertEquals(emptyList<FormattedUserInfo>(), result1.formattedUserInfoList)
        assertEquals(expectedListWithFollowedUser, result2.formattedUserInfoList)
        assertEquals(3, result2.usersCount)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `VM should map user info list correctly where there are no users but followed users`() = runTest {
        val userListViewModel = UserListViewModel(stackOverflowRepository = FakeStackOverflowRepository, followedUserRepository = FakeFollowedUserRepository)
        val results = Channel<UserListViewModel.UiState>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            userListViewModel.state.collect {
                results.send(it)
            }
        }
        FakeStackOverflowRepository.emit(UserInfoListData(isLoading = false, hasError = true))
        FakeFollowedUserRepository.emit(followUsersData)

        val result1 = results.receive()
        val result2 = results.receive()
        assertEquals(emptyList<FormattedUserInfo>(), result1.formattedUserInfoList)
        assertEquals(emptyList<FormattedUserInfo>(), result2.formattedUserInfoList)
        assertEquals(0, result2.usersCount)
    }
}
