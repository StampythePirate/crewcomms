package com.crewcomms.phone.ui

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.crewcomms.phone.ui.screens.CreateCrewScreen
import com.crewcomms.phone.ui.screens.CrewChatScreen
import com.crewcomms.phone.ui.screens.HomeScreen
import com.crewcomms.phone.ui.screens.JoinCrewScreen
import com.crewcomms.phone.ui.screens.SettingsScreen

private object Routes {
    const val Home = "home"
    const val Create = "create"
    const val Join = "join"
    const val Chat = "chat"
    const val Settings = "settings"
}

@Composable
fun PhoneApp(viewModel: PhoneViewModel) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showPermissionRationale by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { results ->
            showPermissionRationale = results.values.any { granted -> !granted }
        }
    )

    LaunchedEffect(Unit) {
        launcher.launch(requiredPermissions())
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            PhoneNavHost(navController = navController, viewModel = viewModel, uiState = uiState)
        }
    }

    if (showPermissionRationale) {
        AlertDialog(
            onDismissRequest = { showPermissionRationale = false },
            title = { Text("Permission needed") },
            text = {
                Text(
                    "CrewComms needs nearby, Bluetooth, and notification permissions for local crew signal and watch relay."
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showPermissionRationale = false
                    launcher.launch(requiredPermissions())
                }) { Text("Retry") }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionRationale = false }) { Text("Close") }
            },
        )
    }
}

@Composable
private fun PhoneNavHost(
    navController: NavHostController,
    viewModel: PhoneViewModel,
    uiState: PhoneUiState,
) {
    NavHost(navController = navController, startDestination = Routes.Home) {
        composable(Routes.Home) {
            HomeScreen(
                deviceName = uiState.settings.displayName,
                onCreateCrew = { navController.navigate(Routes.Create) },
                onJoinCrew = { navController.navigate(Routes.Join) },
                onOpenLastCrew = { navController.navigate(Routes.Chat) },
                onSettings = { navController.navigate(Routes.Settings) },
            )
        }

        composable(Routes.Create) {
            CreateCrewScreen(
                members = uiState.members,
                onBack = { navController.popBackStack() },
                onStartCrew = { name, pinRequired, pin ->
                    viewModel.createCrew(name, pinRequired, pin)
                    navController.navigate(Routes.Chat)
                },
            )
        }

        composable(Routes.Join) {
            JoinCrewScreen(
                crews = uiState.nearbyCrews,
                status = uiState.status,
                onBack = { navController.popBackStack() },
                onScan = viewModel::discoverNearbyCrews,
                onJoin = { crew, pin ->
                    viewModel.joinCrew(crew, pin)
                    navController.navigate(Routes.Chat)
                },
            )
        }

        composable(Routes.Chat) {
            CrewChatScreen(
                state = uiState,
                onBack = { navController.popBackStack() },
                onSendMessage = viewModel::sendMessage,
                onQuickCommand = viewModel::sendQuickCommand,
                onVoicePlaceholder = viewModel::sendVoicePlaceholder,
                onLeaveCrew = viewModel::leaveCrew,
            )
        }

        composable(Routes.Settings) {
            SettingsScreen(
                settings = uiState.settings,
                onBack = { navController.popBackStack() },
                onDisplayNameChanged = viewModel::updateDisplayName,
                onVibrationRelayChanged = viewModel::setVibrationRelay,
                onKeepAwakeChanged = viewModel::setKeepScreenAwake,
                onAutoReconnectChanged = viewModel::setAutoReconnect,
                onMockModeChanged = viewModel::setUseMock,
                onClearHistory = viewModel::clearHistory,
            )
        }
    }
}

@Composable
fun CrewTopBar(title: String, action: @Composable (() -> Unit)? = null) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = title, style = MaterialTheme.typography.titleLarge)
        action?.invoke()
    }
}

private fun requiredPermissions(): Array<String> {
    val permissions = mutableListOf(
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.BLUETOOTH_ADVERTISE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.RECORD_AUDIO,
    )

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        permissions += Manifest.permission.POST_NOTIFICATIONS
        permissions += Manifest.permission.NEARBY_WIFI_DEVICES
    }

    return permissions.toTypedArray()
}
