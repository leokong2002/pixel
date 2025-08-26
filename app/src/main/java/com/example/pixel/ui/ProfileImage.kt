package com.example.pixel.ui

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.pixel.ui.theme.PixelTheme

@Composable
fun ProfileImage(
    model: Any?,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = model,
        contentDescription = "User Profile Image",
        modifier = modifier
            .size(56.dp)
            .clip(shape = CircleShape)
    )
}

//region Preview
@Preview
@Composable
private fun ProfileImagePreview() {
    PixelTheme {
        ProfileImage(
            model = "https://www.example.com/image.jpg"
        )
    }
}
//endregion
