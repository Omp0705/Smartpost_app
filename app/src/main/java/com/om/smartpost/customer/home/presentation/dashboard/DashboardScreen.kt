package com.om.smartpost.customer.home.presentation.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.om.smartpost.customer.home.presentation.dashboard.components.NotificationCard
import com.om.smartpost.customer.home.presentation.dashboard.components.OutForDeliveryCard
import com.om.smartpost.customer.home.presentation.dashboard.components.PackageCard
import com.om.smartpost.customer.home.presentation.dashboard.components.SectionHeader

@Composable
fun DashboardScreen(
    state: DashboardState,
    onAction: (DashboardAction) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
//                contentPadding = PaddingValues(bottom = 100.dp, top = 16.dp), // Clear the FAB and BottomBar
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                // 1. Upcoming Deliveries Section
                item {
                    SectionHeader(
                        title = "Upcoming Deliveries",
                        onViewAllClick = { onAction(DashboardAction.OnViewAllUpcomingClick) }
                    )
                }
                
                if (state.upcomingDeliveries.isEmpty()) {
                    item {
                        Text(
                            text = "No upcoming deliveries",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                } else {
                    // Vertical List as requested
                    items(state.upcomingDeliveries) { item ->
                        PackageCard(
                            item = item, 
//                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }

                // 2. Out for Delivery Section
                item { SectionHeader(title = "Out for Delivery Today") }
                
                if (state.outForDelivery.isEmpty()) {
                     item {
                        Text(
                            text = "No packages out for delivery",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                } else {
                    items(state.outForDelivery) { item ->
                        OutForDeliveryCard(
                            item = item, 
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }

                // 3. Recent Notifications Section
                item { SectionHeader(title = "Recent Notifications") }
                
                if (state.recentNotifications.isEmpty()) {
                     item {
                        Text(
                            text = "No new notifications",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                } else {
                    items(state.recentNotifications) { item ->
                        NotificationCard(
                            item = item, 
                            modifier = Modifier.padding(horizontal = 16.dp) // Added padding modifier support to card
                        )
                    }
                }
            }
        }
        
        if (state.error != null) {
            Text(
                text = state.error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
