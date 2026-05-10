package com.crewcomms.watch.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import com.crewcomms.core.model.QuickCommand
import com.crewcomms.watch.ui.screens.WatchControlsScreen
import com.crewcomms.watch.ui.screens.WatchHomeScreen
import com.crewcomms.watch.ui.screens.WatchMessagesScreen
import com.crewcomms.watch.ui.screens.WatchPttPlaceholderScreen

private object Routes {
    const val Home = "home"
    const val Controls = "controls"
    const val Messages = "messages"
    const val Ptt = "ptt"
}

@Composable
fun WatchApp(viewModel: WatchViewModel) {
    val navController = rememberSwipeDismissableNavController()
    val state = viewModel.state.collectAsStateWithLifecycle().value

    WatchNavHost(
        navController = navController,
        state = state,
        onCommand = viewModel::sendCommand,
    )
}

@Composable
private fun WatchNavHost(
    navController: NavHostController,
    state: com.crewcomms.core.model.WatchState,
    onCommand: (QuickCommand) -> Unit,
) {
    SwipeDismissableNavHost(navController = navController, startDestination = Routes.Home) {
        composable(Routes.Home) {
            WatchHomeScreen(
                roomName = state.roomName,
                status = state.connectionStatus.name,
                onOpenControls = { navController.navigate(Routes.Controls) },
                onOpenMessages = { navController.navigate(Routes.Messages) },
                onOpenPtt = { navController.navigate(Routes.Ptt) },
            )
        }

        composable(Routes.Controls) {
            WatchControlsScreen(
                roomName = state.roomName,
                status = state.connectionStatus.name,
                lastMessage = state.lastMessages.firstOrNull()?.body,
                onPing = { onCommand(QuickCommand.PING) },
                onHelp = { onCommand(QuickCommand.HELP) },
                onReady = { onCommand(QuickCommand.READY) },
                onCome = { onCommand(QuickCommand.COME_HERE) },
                onWait = { onCommand(QuickCommand.WAIT) },
            )
        }

        composable(Routes.Messages) {
            WatchMessagesScreen(messages = state.lastMessages)
        }

        composable(Routes.Ptt) {
            WatchPttPlaceholderScreen()
        }
    }
}
