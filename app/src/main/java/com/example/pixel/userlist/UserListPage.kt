package com.example.pixel.userlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pixel.repository.model.FormattedUserInfo
import com.example.pixel.ui.ErrorDialog
import com.example.pixel.ui.Spinner
import com.example.pixel.ui.UserInfoTile
import com.example.pixel.ui.theme.PixelTheme

@Composable
fun UserListPage(
    viewModel: UserListViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val onHandleEvent: (UserListViewModel.UiEvent) -> Unit = viewModel::handleEvent

    Scaffold(
        topBar = {
            UserListTopBar(
                shouldDisplayUnfollowAll = state.shouldDisplayUnfollowAll,
                usersCount = state.usersCount,
                onUnfollowAllClick = { onHandleEvent.invoke(UserListViewModel.UiEvent.UnfollowAll) })
        },
        modifier = modifier
            .statusBarsPadding()
            .navigationBarsPadding()
    ) { paddingValues ->
        when {
            state.isLoading -> Spinner(modifier = Modifier.padding(paddingValues))
            state.shouldDisplayErrorDialog -> {
                ErrorDialog(
                    title = "Technical Issue",
                    description = "Something went wrong. Please Try Again.",
                    primaryButtonText = "Retry",
                    onPrimaryButtonClick = { onHandleEvent.invoke(UserListViewModel.UiEvent.Retry) }
                )
            }
            else -> {
                UserListMainContent(
                    formattedUserInfoList = state.formattedUserInfoList,
                    onFollowClick = { onHandleEvent.invoke(UserListViewModel.UiEvent.UpdateFollow(it)) },
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserListTopBar(
    shouldDisplayUnfollowAll: Boolean,
    usersCount: Int,
    onUnfollowAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(text = "List of Top $usersCount Users") },
        actions = {
            if (shouldDisplayUnfollowAll) {
                Text(
                    text = "Unfollow All",
                    modifier = Modifier.clickable(onClick = onUnfollowAllClick)
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
        },
        modifier = modifier
    )
}

@Composable
private fun UserListMainContent(
    formattedUserInfoList: List<FormattedUserInfo>,
    onFollowClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(all = 16.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(formattedUserInfoList) { formattedUserInfo ->
            UserInfoTile(
                formattedUserInfo = formattedUserInfo,
                onFollowClick = onFollowClick
            )
        }
    }
}

//region Previews
data class UserListTopBarPreviewData(
    val shouldDisplayUnfollowAll: Boolean = false,
    val usersCount: Int = 20,
    val onUnfollowAllClick: () -> Unit = {},
)

class UserListTopBarProvider : PreviewParameterProvider<UserListTopBarPreviewData> {
    override val values: Sequence<UserListTopBarPreviewData> = sequenceOf(
        UserListTopBarPreviewData(shouldDisplayUnfollowAll = true, usersCount = 10),
        UserListTopBarPreviewData()
    )
}

@Preview
@Composable
private fun UserListTopBarPreview(
    @PreviewParameter(UserListTopBarProvider::class) data: UserListTopBarPreviewData
) {
    PixelTheme {
        UserListTopBar(
            shouldDisplayUnfollowAll = data.shouldDisplayUnfollowAll,
            usersCount = data.usersCount,
            onUnfollowAllClick = data.onUnfollowAllClick
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UserListMainContentPreview() {
    val formattedUserInfoList = listOf(
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
    PixelTheme {
        UserListMainContent(
            formattedUserInfoList = formattedUserInfoList,
            onFollowClick = {},
        )
    }
}
//endregion
