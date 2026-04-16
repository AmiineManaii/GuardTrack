package com.example.guardiantrack.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.guardiantrack.ui.dashboard.DashboardScreen
import com.example.guardiantrack.ui.dashboard.DashboardViewModel
import com.example.guardiantrack.ui.history.HistoryScreen
import com.example.guardiantrack.ui.history.HistoryViewModel
import com.example.guardiantrack.ui.settings.SettingsScreen
import com.example.guardiantrack.ui.settings.SettingsViewModel
import com.example.guardiantrack.ui.theme.GtBgSurface
import com.example.guardiantrack.ui.theme.GtCyan
import com.example.guardiantrack.ui.theme.GtTextSecondary
import com.example.guardiantrack.util.ExportHelper
import androidx.lifecycle.viewmodel.compose.viewModel

sealed class Screen(val route: String, val title: String) {
    data object Dashboard : Screen("dashboard", "Radar")
    data object History : Screen("history", "Journal")
    data object Settings : Screen("settings", "Config")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuardianTrackApp(
    dashboardViewModel: DashboardViewModel = viewModel(),
    historyViewModel: HistoryViewModel = viewModel(),
    settingsViewModel: SettingsViewModel = viewModel()
) {
    val context = LocalContext.current
    val exportHelper = remember { ExportHelper(context) }
    val navController = rememberNavController()
    val screens = listOf(Screen.Dashboard, Screen.History, Screen.Settings)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                screens.forEach { screen ->
                    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

                    NavigationBarItem(
                        icon = {
                            Text(
                                text = when (screen) {
                                    Screen.Dashboard -> "◎"
                                    Screen.History -> "☰"
                                    Screen.Settings -> "⚙"
                                },
                                style = MaterialTheme.typography.titleLarge,
                                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        label = { Text(screen.title) },
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = {
                fadeIn(animationSpec = tween(300)) +
                slideInHorizontally(animationSpec = tween(300)) { it / 4 }
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300)) +
                slideOutHorizontally(animationSpec = tween(300)) { -it / 4 }
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(300)) +
                slideInHorizontally(animationSpec = tween(300)) { -it / 4 }
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(300)) +
                slideOutHorizontally(animationSpec = tween(300)) { it / 4 }
            }
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    viewModel = dashboardViewModel,
                    onAlertClick = { 
                        dashboardViewModel.triggerManualAlert() 
                    }
                )
            }
            composable(Screen.History.route) {
                val uiState by historyViewModel.uiState.collectAsState()
                HistoryScreen(
                    viewModel = historyViewModel,
                    onExportCsv = { 
                        val filename = exportHelper.exportToCsv(uiState.incidents)
                        if (filename != null) {
                            android.widget.Toast.makeText(context, "Export CSV réussi : $filename", android.widget.Toast.LENGTH_SHORT).show()
                        }
                    },
                    onExportTxt = { 
                        val filename = exportHelper.exportToTxt(uiState.incidents)
                        if (filename != null) {
                            android.widget.Toast.makeText(context, "Export TXT réussi : $filename", android.widget.Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
            composable(Screen.Settings.route) {
                SettingsScreen(viewModel = settingsViewModel)
            }
        }
    }
}
