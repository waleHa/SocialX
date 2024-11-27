package com.fgfbrands.myapplication.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.fgfbrands.myapplication.core.consts.Routes
import com.fgfbrands.myapplication.ui.view.HomeScreen
import com.fgfbrands.myapplication.ui.view.UserDetailsScreen

/**
 * Configures navigation for the app.
 *
 * Key Highlights:
 * - Manages transitions between Home and User Details screens.
 * - Ensures type-safe and scalable navigation.
 */
@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.Home) {
        composable(Routes.Home) {
            HomeScreen(navController = navController)
        }
        composable(
            route = Routes.UserDetails,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId")
            userId?.let {
                UserDetailsScreen(userId = it, navController = navController)
            }
        }
    }
}