package com.crewcomms.watch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.crewcomms.watch.ui.WatchApp
import com.crewcomms.watch.ui.WatchViewModel
import com.crewcomms.watch.ui.theme.CrewCommsWatchTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CrewCommsWatchTheme {
                WatchRoot()
            }
        }
    }
}

@Composable
private fun WatchRoot(viewModel: WatchViewModel = hiltViewModel()) {
    WatchApp(viewModel)
}
