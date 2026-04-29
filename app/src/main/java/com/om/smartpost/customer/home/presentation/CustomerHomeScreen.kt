package com.om.smartpost.customer.home.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.om.smartpost.customer.navigation.CustomerNavGraph
import com.om.smartpost.customer.presentation.components.CustomerBottomNavigation
import com.om.smartpost.customer.presentation.components.CustomerTopAppBar
import com.om.smartpost.dashboard.presentation.UserInfoViewModel
import com.om.smartpost.navigation.NavRoutes
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerHomeScreen(
    onLogout: () -> Unit,
    userInfoViewModel: UserInfoViewModel = koinViewModel()
) {
    val navController = rememberNavController()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
//    user info to get the user name
    val userState by userInfoViewModel.state.collectAsState()
//    val fullName = userState.username?.let { "${it.firstName} ${it.lastName}" } ?: "User"

    val scaffoldModifier = if (currentRoute == NavRoutes.CUSTOMER_HOME) {
        Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    } else {
        Modifier
    }

    Scaffold(
        modifier = scaffoldModifier,
        topBar = {
            // Conditional Top Bar: Only show on CUSTOMER_HOME (Dashboard)
            if (currentRoute == NavRoutes.CUSTOMER_HOME) {
                CustomerTopAppBar(
                    scrollBehavior = scrollBehavior,
                    userName = "Om Patil"
                )
            }
        },
        bottomBar = {
            CustomerBottomNavigation(
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
             CustomerNavGraph(navController = navController, onLogout = onLogout)
        }
    }
}
