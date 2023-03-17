package com.codemave.mobilecomputing

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.codemave.mobilecomputing.ui.home.HomeScreen
import com.codemave.mobilecomputing.ui.login.LoginScreen
import com.codemave.mobilecomputing.ui.maps.ReminderLocation
import com.codemave.mobilecomputing.ui.profile.ProfileScreen
import com.codemave.mobilecomputing.ui.reminders.AddReminder
import com.codemave.mobilecomputing.ui.reminders.EditReminder

@Composable
fun MainNavigation(
    appState: MobiCompAppState = rememberMobiCompAppState(),
    context: Context
) {
    NavHost(
        navController = appState.navController,
        startDestination = "login"
    ) {
        composable(route = "login") {
            LoginScreen(navController = appState.navController, context = context)
        }

        composable(route = "home") {
            HomeScreen(navController = appState.navController, context)
        }

        composable(route = "profile") {
            ProfileScreen(navController = appState.navController)
        }

        composable(route = "new_reminder") {
            AddReminder(navController = appState.navController, context)
        }

        composable(route = "map") {
            ReminderLocation(navController = appState.navController)
        }

        composable(route = "edit/{id}") {
            val id = it.arguments?.getString("id")
            id?.let {
                EditReminder(id = id.toLong(), appState.navController, context)
            }
        }
    }
}