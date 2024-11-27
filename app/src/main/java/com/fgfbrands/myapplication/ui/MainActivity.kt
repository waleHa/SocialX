package com.fgfbrands.myapplication.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fgfbrands.myapplication.core.consts.Routes
import com.fgfbrands.myapplication.ui.nav.AppNavigation
import com.fgfbrands.myapplication.ui.theme.AssignmentTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main entry point of the app, setting up navigation and layout.
 *
 * Key Highlights:
 * - Implements a top-level Scaffold for consistent UI.
 * - Manages navigation with dynamic screen titles.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AssignmentTheme {
                val navController = rememberNavController()
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = currentBackStackEntry?.destination

                val currentRoute = currentDestination?.route
                val showBackIcon = currentDestination?.route != Routes.Home

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = when (currentRoute) {
                                        Routes.Home -> "Home"
                                        Routes.UserDetails -> "User Details"
                                        else -> "Home"
                                    },
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            },
                            navigationIcon = {
                                if (showBackIcon) {
                                    IconButton(onClick = { navController.navigateUp() }) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowBack,
                                            contentDescription = "Back",
                                            tint = MaterialTheme.colorScheme.onPrimary
                                        )
                                    }
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        AppNavigation(navController)
                    }
                }
            }
        }
    }
}




