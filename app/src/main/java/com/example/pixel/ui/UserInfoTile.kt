package com.example.pixel.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pixel.repository.model.UserInfo
import com.example.pixel.ui.theme.PixelTheme

@Composable
fun UserInfoTile(
    userInfo: UserInfo,
    onFollowClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color(0xFFACB1B3),
                shape = RoundedCornerShape(size = 16.dp)
            )
            .clip(shape = RoundedCornerShape(size = 16.dp))
            .padding(all = 16.dp)
    ) {
        ProfileImage(model = userInfo.imageUrl)
        UserNameAndReputation(
            totalReputation = userInfo.totalReputation,
            displayName = userInfo.displayName,
            modifier = Modifier.weight(1f)
        )
        FollowButton(
            isFollowed = userInfo.isFollowed,
            onFollowClick = { onFollowClick.invoke(userInfo.id) }
        )
    }
}

@Composable
private fun UserNameAndReputation(
    totalReputation: Long,
    displayName: String,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        modifier = modifier
    ) {
        Text(text = displayName)
        Text(text = "Total reputation: $totalReputation")
    }
}

@Composable
private fun FollowButton(
    isFollowed: Boolean,
    onFollowClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onFollowClick,
        contentPadding = PaddingValues(all = 8.dp),
        modifier = modifier
    ) {
        Text(text = if (isFollowed) "Unfollow" else "Follow")
    }
}

//region Previews
@Preview(showBackground = true)
@Composable
fun UserInfoTilePreview() {
    PixelTheme {
        UserInfoTile(
            userInfo = UserInfo(
                isFollowed = true,
                id = 1234567,
                totalReputation = 12345,
                imageUrl = "https://www.example.com/image.jpg",
                displayName = "User Name"
            ),
            onFollowClick = {}
        )
    }
}
//endregion
