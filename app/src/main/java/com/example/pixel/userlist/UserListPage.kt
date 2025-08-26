package com.example.pixel.userlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pixel.repository.model.UserInfo
import com.example.pixel.ui.Spinner
import com.example.pixel.ui.UserInfoTile

@Composable
fun UserListPage(
    viewModel: UserListViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val onHandleEvent: (UserListViewModel.UiEvent) -> Unit = viewModel::handleEvent

    Scaffold(modifier = modifier) { paddingValues ->
        if (state.isLoading) {
            Spinner(modifier = modifier.padding(paddingValues))
        } else {
            UserListMainContent(userInfoList = state.userInfoList)
        }
    }
}

@Composable
fun UserListMainContent(
    userInfoList: List<UserInfo>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(all = 16.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(userInfoList) { userInfo ->
            UserInfoTile(
                userInfo = userInfo,
                onFollowClick = {}
            )
        }
    }
}
