package com.loswachabaches.bachewatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.loswachabaches.bachewatch.ui.navigation.AppNavigation
import com.loswachabaches.bachewatch.ui.theme.BacheWatchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BacheWatchTheme {
                AppNavigation()
            }
        }
    }
}