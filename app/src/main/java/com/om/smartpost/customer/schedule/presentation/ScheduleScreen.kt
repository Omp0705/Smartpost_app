package com.om.smartpost.customer.schedule.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import com.om.smartpost.customer.parcel.presentation.components.ParcelItemCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Incoming Shipments", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
                .padding(innerPadding)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (!state.error.isNullOrEmpty()) {
                Text(
                    text = state.error!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (state.incomingShipments.isEmpty()) {
                Text(
                    text = "No incoming shipments found.",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.incomingShipments) { shipment ->
                        // Reusing ParcelItemCard from ParcelScreen
                        ParcelItemCard(
                            shipment = shipment,
                            onClick = { viewModel.onAction(ScheduleAction.OnShipmentClick(shipment)) }
                        )
                    }
                }
            }

            if (state.isBottomSheetVisible && state.selectedShipment != null) {
                UpdateTimeslotBottomSheet(
                    shipment = state.selectedShipment!!,
                    isUpdating = state.isUpdatingTimeslot,
                    onDismiss = { viewModel.onAction(ScheduleAction.OnBottomSheetDismiss) },
                    onSave = { newTimeslot -> viewModel.onAction(ScheduleAction.OnSaveTimeslot(newTimeslot)) }
                )
            }
        }
    }
}
