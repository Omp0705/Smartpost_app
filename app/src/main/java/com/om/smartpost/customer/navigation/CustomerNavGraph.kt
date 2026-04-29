package com.om.smartpost.customer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.om.smartpost.customer.home.presentation.dashboard.DashboardEvent
import com.om.smartpost.customer.home.presentation.dashboard.DashboardScreen
import com.om.smartpost.customer.home.presentation.dashboard.DashboardViewModel
import com.om.smartpost.customer.notifications.presentation.NotificationsScreen
import com.om.smartpost.customer.notifications.presentation.NotificationsViewModel
import com.om.smartpost.customer.parcel.presentation.ParcelEvent
import com.om.smartpost.customer.parcel.presentation.ParcelScreen
import com.om.smartpost.customer.parcel.presentation.ParcelViewModel
import com.om.smartpost.customer.profile.presentation.ProfileEvent
import com.om.smartpost.customer.profile.presentation.ProfileScreen
import com.om.smartpost.customer.profile.presentation.ProfileViewModel
import com.om.smartpost.customer.schedule.presentation.ScheduleScreen
import com.om.smartpost.customer.send.presentation.SendScreen
import com.om.smartpost.navigation.NavRoutes
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CustomerNavGraph(
    navController: NavHostController,
    onLogout: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.CUSTOMER_HOME
        // Logic: CustomerHomeScreen has the bottom bar, but the "Home" tab content needs to be defined.
        // Let's assume NavRoutes.CUSTOMER_HOME is the "Home" tab content.
    ) {
        composable(NavRoutes.CUSTOMER_HOME) {
            val viewModel: DashboardViewModel = koinViewModel()
            val state by viewModel.state.collectAsState()

            androidx.compose.runtime.LaunchedEffect(viewModel.events) {
                viewModel.events.collect { event ->
                    when (event) {
                        is DashboardEvent.NavigateToUpcomingDeliveries -> {
                            navController.navigate(NavRoutes.CUSTOMER_PARCEL) // Or a specific upcoming tab
                        }
                        is DashboardEvent.NavigateToPackageDetails -> {
                            navController.navigate(NavRoutes.CUSTOMER_PARCEL_DETAILS.replace("{parcelId}", event.trackingNumber))
                        }

                        is DashboardEvent.ShowError -> {}
                    }
                }
            }

            DashboardScreen(
                state = state,
                onAction = viewModel::onAction
            )
        }
        composable(NavRoutes.CUSTOMER_PARCEL) {
            val viewModel: ParcelViewModel = koinViewModel()
            val state by viewModel.state.collectAsState()

            ParcelScreen(
                state = state,
                events = viewModel.events,
                onAction = viewModel::onAction,
                onNavigateToParcelDetails = { parcelId ->
                    navController.navigate(NavRoutes.CUSTOMER_PARCEL_DETAILS.replace("{parcelId}", parcelId))
                }
            )
        }
        
        composable(NavRoutes.CUSTOMER_PARCEL_DETAILS) { backStackEntry ->
            val parcelId = backStackEntry.arguments?.getString("parcelId") ?: ""
            val viewModel = koinViewModel<com.om.smartpost.customer.parcel.presentation.details.ParcelDetailsViewModel>(
                parameters = { org.koin.core.parameter.parametersOf(parcelId) }
            )
            val state by viewModel.state.collectAsState()

            com.om.smartpost.customer.parcel.presentation.details.ParcelDetailsScreen(
                state = state,
                events = viewModel.events,
                onAction = viewModel::onAction,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(NavRoutes.CUSTOMER_SEND) {
            SendScreen()
        }
        composable(NavRoutes.CUSTOMER_SCHEDULE) {
            ScheduleScreen()
        }
        composable(route = NavRoutes.CUSTOMER_NOTIFICATIONS) {
            val viewModel = koinViewModel<NotificationsViewModel>()
            val state by viewModel.state.collectAsState()

            NotificationsScreen(
                state = state,
                onAction = viewModel::onAction,
//                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(NavRoutes.CUSTOMER_PROFILE) {
            val viewModel: ProfileViewModel = koinViewModel()
            val state by viewModel.state.collectAsState()

            ProfileScreen(
                state = state,
                events = viewModel.events,
                onAction = viewModel::onAction,
                onNavigateToLogin = onLogout,
                onNavigateToAddAddress = {
                    // Open Bottom Sheet or navigate
                }
            )
        }
    }
}
