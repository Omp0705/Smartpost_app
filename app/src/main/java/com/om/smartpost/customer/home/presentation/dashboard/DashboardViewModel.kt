package com.om.smartpost.customer.home.presentation.dashboard


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.om.smartpost.customer.home.domain.model.NotificationItem
import com.om.smartpost.customer.home.domain.model.PackageItem
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.om.smartpost.customer.parcel.domain.repository.ShipmentRepository
import com.om.smartpost.customer.parcel.domain.model.ShipmentStatus
import com.om.smartpost.core.domain.utils.Result
import java.time.format.DateTimeFormatter

class DashboardViewModel(
    private val shipmentRepository: ShipmentRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    private val _events = Channel<DashboardEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadData()
    }

    fun onAction(action: DashboardAction) {
        when (action) {
            is DashboardAction.OnViewAllUpcomingClick -> {
                viewModelScope.launch {
                    _events.send(DashboardEvent.NavigateToUpcomingDeliveries)
                }
            }
            is DashboardAction.OnPackageClick -> {
                viewModelScope.launch {
                    _events.send(DashboardEvent.NavigateToPackageDetails(action.trackingNumber))
                }
            }
            is DashboardAction.OnNotificationClick -> {
                // Handle notification click
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            // Dummy Notifications for now since Notification architecture isn't built yet
            val dummyNotifications = listOf(
                NotificationItem(
                    title = "New features coming soon!",
                    timeAgo = "1 hour ago",
                    isRead = true
                )
            )

            // Fetch Real Data
            shipmentRepository.getAllShipments().collect { result ->
                when (result) {
                    is Result.Success -> {
                        val allShipments = result.data

                        // Filter Out For Delivery: Parcels arriving today or actively marked OFD
                        val outForDelivery = allShipments.filter {
                            it.currentStatus == ShipmentStatus.OUT_FOR_DELIVERY
                        }.map { shipment ->
                            PackageItem(
                                trackingNumber = shipment.trackingNumber,
                                senderOrSource = shipment.senderName,
                                status = shipment.currentStatus.displayName,
                                timeOrDate = "Arriving Today",
                                postmanName = shipment.postmanName ?: "Assigning..."
                            )
                        }

                        // Filter Upcoming: Everything active and NOT Out For Delivery
                        val upcoming = allShipments.filter {
                            it.currentStatus in listOf(
                                ShipmentStatus.CREATED,
                                ShipmentStatus.PENDING,
                                ShipmentStatus.ACCEPTED,
                                ShipmentStatus.IN_TRANSIT,
                                ShipmentStatus.ARRIVED_AT_DESTINATION
                            )
                        }.map { shipment ->
                            PackageItem(
                                trackingNumber = shipment.trackingNumber,
                                senderOrSource = shipment.senderName,
                                status = shipment.currentStatus.displayName,
                                timeOrDate = shipment.createdAt.format(DateTimeFormatter.ofPattern("MMM d"))
                            )
                        }

                        _state.update {
                            it.copy(
                                isLoading = false,
                                upcomingDeliveries = upcoming,
                                outForDelivery = outForDelivery,
                                recentNotifications = dummyNotifications
                            )
                        }
                    }
                    is Result.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = "Failed to load dashboard data.",
                                recentNotifications = dummyNotifications
                            )
                        }
                    }
                }
            }
        }
    }
}
