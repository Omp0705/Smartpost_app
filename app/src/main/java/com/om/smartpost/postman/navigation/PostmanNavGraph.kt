package com.om.smartpost.postman.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.om.smartpost.customer.profile.presentation.ProfileScreen
import com.om.smartpost.customer.profile.presentation.ProfileViewModel
import com.om.smartpost.navigation.NavRoutes
import com.om.smartpost.postman.home.presentation.dashboard.PostmanDashboardScreen
import com.om.smartpost.postman.presentation.PostmanViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PostmanNavGraph(
    navController: NavHostController,
    onLogout: () -> Unit,
    viewModel: PostmanViewModel
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.POSTMAN_HOME
    ) {
        composable(NavRoutes.POSTMAN_HOME) {
            val state by viewModel.state.collectAsState()
            PostmanDashboardScreen(
                state = state,
                onNavigateToParcels = { navController.navigate(NavRoutes.POSTMAN_PARCELS) },
                onNavigateToRoute = { navController.navigate(NavRoutes.POSTMAN_ROUTE_TAB) }
            )
        }
        composable(NavRoutes.POSTMAN_PARCELS) {
            val state by viewModel.state.collectAsState()
            com.om.smartpost.postman.parcels.presentation.PostmanParcelsScreen(
                state = state,
                onNavigateToDetails = { parcelId -> 
                    val route = NavRoutes.POSTMAN_PARCEL_DETAILS.replace("{parcelId}", parcelId)
                    navController.navigate(route)
                }
            )
        }
        composable(NavRoutes.POSTMAN_PARCEL_DETAILS) { backStackEntry ->
            val parcelId = backStackEntry.arguments?.getString("parcelId") ?: ""
            val state by viewModel.state.collectAsState()
            val shipment = state.shipments.find { it.id == parcelId }

            if (shipment != null) {
                com.om.smartpost.postman.parcels.presentation.details.PostmanShipmentDetailsScreen(
                    shipment = shipment,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
        composable(NavRoutes.POSTMAN_ROUTE_TAB) {
            // Placeholder for Route tab
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Postman Route Tab")
            }
        }
        composable(NavRoutes.POSTMAN_PROFILE) {
            val viewModel: ProfileViewModel = koinViewModel()
            val state by viewModel.state.collectAsState()

            ProfileScreen(
                state = state,
                events = viewModel.events,
                onAction = viewModel::onAction,
                onNavigateToLogin = onLogout,
                onNavigateToAddAddress = { },
                showAddress = false,
                showPreferences = false
            )
        }
    }
}
