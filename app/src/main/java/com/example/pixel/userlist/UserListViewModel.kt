package com.example.pixel.userlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pixel.database.model.FollowedUser
import com.example.pixel.repository.FollowedUserRepository
import com.example.pixel.repository.StackOverflowRepository
import com.example.pixel.repository.model.FormattedUserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val stackOverflowRepository: StackOverflowRepository,
    private val followedUserRepository: FollowedUserRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state = combine(_state, stackOverflowRepository.userInfoListDataFlow, followedUserRepository.followedUsersFlow) { state, userInfoListData, followedUsersData ->
        state.copy(
            isLoading = userInfoListData.isLoading || followedUsersData.isLoading,
            shouldDisplayErrorDialog = userInfoListData.hasError,
            formattedUserInfoList = mapFollowedUsers(userInfoListData.formattedUserInfoList, followedUsersData.followedUsers)
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), UiState())

    fun handleEvent(event: UiEvent) {
        when (event) {
            is UiEvent.UpdateFollow -> updateFollow(event.accountId)
            UiEvent.UnfollowAll -> unfollowAll()
            UiEvent.Retry -> loadUserInfoListAndFollowUsers()
        }
    }

    init {
        loadUserInfoListAndFollowUsers()
    }

    private fun loadUserInfoListAndFollowUsers() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _state.update { it.copy(isLoading = true, shouldDisplayErrorDialog = false) }
                stackOverflowRepository.getUsers()
                followedUserRepository.getFollowedUsers()
            }
        }
    }

    private fun mapFollowedUsers(userInfoList: List<FormattedUserInfo>, followedUser: List<FollowedUser>): List<FormattedUserInfo> {
        val updatedUserInfoList = userInfoList.map { userInfo ->
            userInfo.copy(
                isFollowed = followedUser.any { it.accountId == userInfo.accountId }
            )
        }
        return updatedUserInfoList
    }

    private fun updateFollow(accountId: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val isFollowed = state.value.formattedUserInfoList.find { it.accountId == accountId }?.isFollowed ?: return@withContext
                if (isFollowed) {
                    followedUserRepository.unfollowUser(accountId)
                } else {
                    followedUserRepository.followUser(accountId)
                }
            }
        }
    }

    private fun unfollowAll() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                followedUserRepository.unfollowAllUsers()
            }
        }
    }

    sealed interface UiEvent {
        data class UpdateFollow(val accountId: Long) : UiEvent
        data object UnfollowAll : UiEvent
        data object Retry : UiEvent
    }

    data class UiState(
        val isLoading: Boolean = true,
        val shouldDisplayErrorDialog: Boolean = false,
        val formattedUserInfoList: List<FormattedUserInfo> = emptyList()
    ) {
        val usersCount = formattedUserInfoList.size
        val shouldDisplayUnfollowAll = formattedUserInfoList.count { it.isFollowed } > 0
    }
}
