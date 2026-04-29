package com.om.smartpost.postman.parcels.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.om.smartpost.R

import com.om.smartpost.postman.domain.model.Shipment
import com.om.smartpost.postman.presentation.PostmanState

enum class PostmanParcelStatus { PENDING, DELIVERED, FAILED }

enum class PostmanParcelFilter(val displayName: String) {
    ALL("All"),
    PENDING("Pending"),
    DELIVERED("Delivered")
}

@Composable
fun PostmanParcelsScreen(
    state: PostmanState,
    onNavigateToDetails: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(PostmanParcelFilter.ALL) }

    val primaryBlue = Color(0xFF0D47A1)

    // Filter logic
    val filteredParcels = state.shipments.filter { parcel ->
        val matchesSearch = parcel.receiverName.contains(searchQuery, ignoreCase = true) ||
                parcel.destinationAddress.contains(searchQuery, ignoreCase = true) ||
                parcel.trackingNumber.contains(searchQuery, ignoreCase = true)
        val matchesFilter = when (selectedFilter) {
            PostmanParcelFilter.ALL -> true
            PostmanParcelFilter.PENDING -> parcel.status == PostmanParcelStatus.PENDING || parcel.status == PostmanParcelStatus.FAILED
            PostmanParcelFilter.DELIVERED -> parcel.status == PostmanParcelStatus.DELIVERED
        }
        matchesSearch && matchesFilter
    }

    val totalCount = state.shipments.size
    val pendingCount = state.shipments.count { it.status == PostmanParcelStatus.PENDING || it.status == PostmanParcelStatus.FAILED }
    val deliveredCount = state.shipments.count { it.status == PostmanParcelStatus.DELIVERED }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 1. Header
        Text(
            text = "My Parcels",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF001F3F), // Dark blue text
            modifier = Modifier.padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 16.dp)
        )

        // 2. Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search by name or address...", color = Color.Gray) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Search",
                    tint = Color.Gray
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedBorderColor = primaryBlue,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Filters
        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val filters = listOf(
                Pair(PostmanParcelFilter.ALL, "All ($totalCount)"),
                Pair(PostmanParcelFilter.PENDING, "Pending ($pendingCount)"),
                Pair(PostmanParcelFilter.DELIVERED, "Delivered ($deliveredCount)")
            )

            items(filters) { (filter, label) ->
                val isSelected = selectedFilter == filter
                val bgColor = if (isSelected) primaryBlue else Color(0xFFF5F5F5)
                val textColor = if (isSelected) Color.White else Color.Black

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(bgColor)
                        .clickable { selectedFilter = filter }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        color = textColor,
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        // Horizontal Divider
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFEEEEEE)))

        // 4. Parcel List
        LazyColumn(
            contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp, start = 24.dp, end = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredParcels) { parcel ->
                PostmanParcelCard(
                    parcel = parcel,
                    onClick = { onNavigateToDetails(parcel.id) }
                )
            }
        }
    }
}

@Composable
fun PostmanParcelCard(
    parcel: Shipment,
    onClick: () -> Unit
) {
    val statusColor = when (parcel.status) {
        PostmanParcelStatus.PENDING -> Color(0xFFE65100) // Orange
        PostmanParcelStatus.DELIVERED -> Color(0xFF4CAF50) // Green
        PostmanParcelStatus.FAILED -> Color(0xFFD32F2F) // Red
    }

    val statusIcon = when (parcel.status) {
        PostmanParcelStatus.PENDING -> ImageVector.vectorResource(R.drawable.ic_clock)
        PostmanParcelStatus.DELIVERED -> Icons.Outlined.CheckCircle
        PostmanParcelStatus.FAILED -> Icons.Default.Close
    }

    val statusText = when (parcel.status) {
        PostmanParcelStatus.PENDING -> "Pending"
        PostmanParcelStatus.DELIVERED -> "Delivered"
        PostmanParcelStatus.FAILED -> "Failed"
    }

    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = parcel.receiverName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF001F3F)
                )
                Icon(
                    imageVector = statusIcon,
                    contentDescription = statusText,
                    tint = statusColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.location_ic),
                    contentDescription = "Location",
                    tint = Color.Gray,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = parcel.destinationAddress,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_clock),
                        contentDescription = "Time",
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = parcel.timeWindow,
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                }

                // Status Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(statusColor.copy(alpha = 0.1f))
                        .border(1.dp, statusColor.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = statusText,
                        color = statusColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
