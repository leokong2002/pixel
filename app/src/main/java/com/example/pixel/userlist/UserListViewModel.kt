package com.example.pixel.userlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pixel.repository.StackOverflowRepository
import com.example.pixel.repository.model.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val stackOverflowRepository: StackOverflowRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()

    fun handleEvent(event: UiEvent) {
        when (event) {
            is UiEvent.UpdateFollow -> {}
        }
    }

    init {
        getUserInfoList()
    }

    private fun getUserInfoList() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val userInfoList = stackOverflowRepository.getUsers()
                _state.update {
                    it.copy(
                        isLoading = false,
                        hasError = userInfoList.isEmpty(),
                        userInfoList = userInfoList,
                    )
                }
            }
        }
    }

    sealed interface UiEvent {
        data class UpdateFollow(val id: Long) : UiEvent
    }

    data class UiState(
        val isLoading: Boolean = true,
        val hasError: Boolean = false,
        val userInfoList: List<UserInfo> = emptyList()
    )
}
