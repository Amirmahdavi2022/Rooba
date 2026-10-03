package app.rooba.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.rooba.RoobaApp
import app.rooba.ui.screens.AccountScreen
import app.rooba.ui.screens.HomeScreen
import app.rooba.ui.screens.LoginScreen
import app.rooba.ui.screens.LogsScreen
import app.rooba.ui.screens.ServerListScreen
import app.rooba.ui.screens.SettingsScreen
import app.rooba.ui.screens.SignupScreen
import app.rooba.ui.screens.SplashScreen
import app.rooba.ui.theme.ThemeController

object RoobaRoutes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val HOME = "home"
    const val SERVERS = "servers"
    const val SETTINGS = "settings"
    const val ACCOUNT = "account"
    const val LOGS = "logs"
}

@Composable
fun RoobaNavGraph(
    navController: NavHostController = rememberNavController(),
    app: RoobaApp,
    themeController: ThemeController,
    onRequestConnect: () -> Unit,
    onDisconnect: () -> Unit,
) {
    NavHost(navController = navController, startDestination = RoobaRoutes.SPLASH) {
        composable(RoobaRoutes.SPLASH) {
            SplashScreen(
                authRepository = app.authRepository,
                onSignedIn = {
                    navController.navigate(RoobaRoutes.HOME) { popUpTo(RoobaRoutes.SPLASH) { inclusive = true } }
                },
                onNeedsLogin = {
                    navController.navigate(RoobaRoutes.LOGIN) { popUpTo(RoobaRoutes.SPLASH) { inclusive = true } }
                },
            )
        }
        composable(RoobaRoutes.LOGIN) {
            LoginScreen(
                authRepository = app.authRepository,
                onSignedIn = {
                    navController.navigate(RoobaRoutes.HOME) { popUpTo(RoobaRoutes.LOGIN) { inclusive = true } }
                },
                onCreateAccount = { navController.navigate(RoobaRoutes.SIGNUP) },
            )
        }
        composable(RoobaRoutes.SIGNUP) {
            SignupScreen(
                onDone = { navController.popBackStack() },
                onBack = { navController.popBackStack() },
            )
        }
        composable(RoobaRoutes.HOME) {
            HomeScreen(
                app = app,
                themeController = themeController,
                onRequestConnect = onRequestConnect,
                onDisconnect = onDisconnect,
                onOpenServers = { navController.navigate(RoobaRoutes.SERVERS) },
                onOpenSettings = { navController.navigate(RoobaRoutes.SETTINGS) },
            )
        }
        composable(RoobaRoutes.SERVERS) {
            ServerListScreen(
                serverListClient = app.serverListClient,
                proxyStateStore = app.proxyStateStore,
                onServerSelected = { navController.popBackStack() },
                onBack = { navController.popBackStack() },
            )
        }
        composable(RoobaRoutes.SETTINGS) {
            SettingsScreen(
                settingsStore = app.settingsStore,
                onOpenLogs = { navController.navigate(RoobaRoutes.LOGS) },
                onOpenAccount = { navController.navigate(RoobaRoutes.ACCOUNT) },
                onSignOut = {
                    onDisconnect()
                    app.tokenStore.clear()
                    navController.navigate(RoobaRoutes.LOGIN) { popUpTo(0) { inclusive = true } }
                },
                onBack = { navController.popBackStack() },
            )
        }
        composable(RoobaRoutes.ACCOUNT) {
            AccountScreen(
                authRepository = app.authRepository,
                onBack = { navController.popBackStack() },
            )
        }
        composable(RoobaRoutes.LOGS) {
            LogsScreen(onBack = { navController.popBackStack() })
        }
    }
}
