package com.crewcomms.phone.ui

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.crewcomms.core.model.ConnectionStatus
import com.crewcomms.phone.ui.components.SignalLevel
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

    Surface {
        PhoneNavHost(navController = navController, viewModel = viewModel, uiState = uiState)
    }

    if (showPermissionRationale) {
        AlertDialog(
            onDismissRequest = { showPermissionRationale = false },
            title = { Text("Permissions needed") },
            text = {
                Text("CrewComms needs nearby, Bluetooth, and notification permissions for local crew communication.")
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
                watchLinked = uiState.settings.vibrationRelayToWatch,
                signalLevel = homeSignalLevel(uiState.status),
                signalText = homeSignalText(uiState.status),
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

private fun homeSignalLevel(status: ConnectionStatus): SignalLevel = when (status) {
    ConnectionStatus.CONNECTED -> SignalLevel.ONLINE
    ConnectionStatus.CONNECTING, ConnectionStatus.SYNCING, ConnectionStatus.ADVERTISING, ConnectionStatus.DISCOVERING -> SignalLevel.RECONNECTING
    ConnectionStatus.ERROR -> SignalLevel.LOST
    ConnectionStatus.DISCONNECTED -> SignalLevel.IDLE
}

private fun homeSignalText(status: ConnectionStatus): String = when (status) {
    ConnectionStatus.CONNECTED -> "Signal Online"
    ConnectionStatus.CONNECTING, ConnectionStatus.SYNCING, ConnectionStatus.ADVERTISING, ConnectionStatus.DISCOVERING -> "Signal Tracking"
    ConnectionStatus.ERROR -> "Signal Lost"
    ConnectionStatus.DISCONNECTED -> "Signal Idle"
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
