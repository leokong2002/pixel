package com.example.pixel.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.pixel.ui.theme.PixelTheme

@Composable
fun ErrorDialog(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    primaryButtonText: String = "",
    onPrimaryButtonClick: () -> Unit = {},
) {
    Dialog(onDismissRequest = {}) {
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .background(
                    shape = RoundedCornerShape(size = 16.dp),
                    color = Color(0xFFFFFFFF)
                )
                .clip(shape = RoundedCornerShape(size = 16.dp))
                .padding(all = 16.dp)
        ) {
            Text(
                text = title,
                fontSize = 32.sp
            )
            Text(
                text = description,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            if (primaryButtonText.isNotEmpty()) {
                Button(
                    onClick = onPrimaryButtonClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = primaryButtonText,
                        fontSize = 32.sp
                    )
                }
            }
        }
    }
}

//region Previews
@Preview
@Composable
private fun ErrorDialogPreview() {
    PixelTheme {
        ErrorDialog(
            title = "Technical Issue",
            description = "Something went wrong, please try again.",
            primaryButtonText = "Retry"
        )
    }
}
//endregion
