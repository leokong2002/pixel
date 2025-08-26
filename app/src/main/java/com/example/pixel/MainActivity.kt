package com.example.pixel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pixel.ui.theme.PixelTheme
import com.example.pixel.userlist.UserListPage
import com.example.pixel.userlist.UserListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PixelTheme {
                UserListPage(viewModel = hiltViewModel<UserListViewModel>())
            }
        }
    }
}
