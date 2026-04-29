package com.om.smartpost.postman.parcels.presentation.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.om.smartpost.customer.parcel.presentation.components.AddressAndSenderCard
import com.om.smartpost.customer.parcel.presentation.components.PostmanAndSlotRow
import com.om.smartpost.postman.domain.model.Shipment
import com.om.smartpost.postman.parcels.presentation.PostmanParcelStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostmanShipmentDetailsScreen(
    shipment: Shipment,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Shipment Details", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Header Info
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Tracking ID: ${shipment.trackingNumber}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                val statusText = when(shipment.status) {
                    PostmanParcelStatus.PENDING -> "Pending Delivery"
                    PostmanParcelStatus.DELIVERED -> "Delivered"
                    PostmanParcelStatus.FAILED -> "Failed / Cancelled"
                }
                Text(
                    text = "Status: $statusText",
                    style = MaterialTheme.typography.bodyMedium,
                    color = when(shipment.status) {
                        PostmanParcelStatus.DELIVERED -> Color(0xFF4CAF50)
                        PostmanParcelStatus.FAILED -> Color(0xFFD32F2F)
                        else -> Color(0xFFE65100)
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Receiver Details
            AddressAndSenderCard(
                addressText = shipment.destinationAddress,
                senderName = shipment.receiverName // Mocking as sender for UI layout purposes
            )

            // Time Window
            PostmanAndSlotRow(
                postmanName = "You",
                postmanPhone = null,
                timeRange = shipment.timeWindow
            )

            // Further details could go here... (Timeline etc.)
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
