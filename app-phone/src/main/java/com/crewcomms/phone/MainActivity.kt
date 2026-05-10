package com.crewcomms.phone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.crewcomms.phone.ui.PhoneApp
import com.crewcomms.phone.ui.PhoneViewModel
import com.crewcomms.phone.ui.theme.CrewCommsPhoneTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CrewCommsPhoneTheme {
                PhoneRoot()
            }
        }
    }
}

@Composable
private fun PhoneRoot(viewModel: PhoneViewModel = hiltViewModel()) {
    PhoneApp(viewModel = viewModel)
}
