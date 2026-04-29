package com.om.smartpost.postman.home.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.om.smartpost.postman.navigation.PostmanNavGraph
import com.om.smartpost.postman.presentation.PostmanViewModel
import com.om.smartpost.postman.presentation.components.PostmanBottomNavigation
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PostmanHomeScreen(
    onLogout: () -> Unit,
    viewModel: PostmanViewModel = koinViewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            PostmanBottomNavigation(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
             PostmanNavGraph(
                 navController = navController, 
                 onLogout = onLogout,
                 viewModel = viewModel
             )
        }
    }
}
