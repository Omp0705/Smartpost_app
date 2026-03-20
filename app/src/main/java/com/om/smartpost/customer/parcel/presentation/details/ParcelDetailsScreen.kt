package com.om.smartpost.customer.parcel.presentation.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.om.smartpost.customer.parcel.domain.model.ShipmentStatus
import com.om.smartpost.customer.parcel.domain.model.TrackingEvent
import com.om.smartpost.customer.parcel.presentation.components.AddressAndSenderCard
import com.om.smartpost.customer.parcel.presentation.components.DeliveryInstructionsCard
import com.om.smartpost.customer.parcel.presentation.components.PostmanAndSlotRow
import com.om.smartpost.customer.parcel.presentation.components.TrackingTimeline
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParcelDetailsScreen(
    state: ParcelDetailsState,
    events: Flow<ParcelDetailsEvent>,
    onAction: (ParcelDetailsAction) -> Unit,
    onNavigateBack: () -> Unit
) {
    LaunchedEffect(events) {
        events.collect { event ->
            when (event) {
                is ParcelDetailsEvent.NavigateBack -> onNavigateBack()
                is ParcelDetailsEvent.ShowSnackbar -> {
                    // Handle snackbar
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Order Details",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            ),
                            color = Color.Black
                        )
                        if (state.parcel != null) {
                            Text(
                                text = state.parcel.trackingNumber,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(ParcelDetailsAction.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                windowInsets = WindowInsets(0.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFBF9F6) // Beige background to match mock
                )
            )
        },
        containerColor = Color(0xFFFBF9F6),
        contentWindowInsets = WindowInsets(0.dp)
    ) { paddingValues ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF6A1B31))
            }
        } else if (state.error != null) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text(text = state.error, color = Color.Red)
            }
        } else if (state.parcel != null) {
            val parcel = state.parcel
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // Top Header Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            // Arriving labels
                            Text(
                                text = "Arriving Soon",
                                color = Color(0xFFD97706),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        // Tracking and Article Info
                        Text(
                            text = "Tracking No: ${parcel.trackingNumber}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = Color.Black
                        )
                        if (!parcel.articleBarcode.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Article Barcode: ${parcel.articleBarcode}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "From: ${parcel.senderName}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.DarkGray
                        )
                    }
                }

                // Tracking Timeline from Backend
                TrackingTimeline(
                    events = parcel.trackingHistory,
                    currentStatus = parcel.currentStatus
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Detail Cards
                val fullAddress = "${parcel.receiverAddressLine1}" + (if (parcel.receiverAddressLine2 != null) ", ${parcel.receiverAddressLine2}" else "") + ", ${parcel.destinationPincode}"
                
                AddressAndSenderCard(
                    addressText = fullAddress,
                    senderName = parcel.senderName
                )

                // Notice we check if we have the phone on the domain; assuming it will be added, if not we fake it for UI or pass empty.
                PostmanAndSlotRow(
                    postmanName = parcel.postmanName ?: "",
                    postmanPhone = "+91 9876543210", // Placeholder until backend adds it to Shipment
                    timeRange = parcel.predictedSlot ?: "4:00 PM - 6:00 PM"
                )

                DeliveryInstructionsCard(
                    instructions = "" // Deprecated in Shipment.kt
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons
                Button(
                    onClick = { onAction(ParcelDetailsAction.OnRescheduleClick) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B31)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Reschedule Delivery", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { onAction(ParcelDetailsAction.OnContactSupportClick) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(0xFFF6F0ED),
                        contentColor = Color(0xFF6A1B31)
                    ),
                    border = null // Removed explicit border for clean tan button
                ) {
                    Text("Contact Support", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
