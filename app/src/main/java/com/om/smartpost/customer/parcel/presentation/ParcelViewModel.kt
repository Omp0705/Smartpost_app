package com.om.smartpost.customer.parcel.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.om.smartpost.core.domain.utils.Result
import com.om.smartpost.customer.parcel.domain.repository.ShipmentRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ParcelViewModel(
    private val shipmentRepository: ShipmentRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ParcelState())
    val state = _state.asStateFlow()

    private val _events = Channel<ParcelEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadShipments()
    }

    fun onAction(action: ParcelAction) {
        when (action) {
            is ParcelAction.OnParcelClick -> {
                viewModelScope.launch {
                    _events.send(ParcelEvent.NavigateToParcelDetails(action.parcelId))
                }
            }
            is ParcelAction.OnSearchQueryChange -> {
                _state.update { it.copy(searchQuery = action.query) }
                applyFilters()
            }
            is ParcelAction.OnStatusFilterSelected -> {
                _state.update { it.copy(selectedStatus = action.filter) }
                applyFilters()
            }
        }
    }

    private fun loadShipments() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            shipmentRepository.getAllShipments().collect { result ->
                when(result) {
                    is Result.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                shipments = result.data
                            )
                        }
                        applyFilters()
                    }
                    is Result.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = "Could not connect to the server. Please try again."
                            )
                        }
                    }
                }
            }
        }
    }

    private fun applyFilters() {
        val query = _state.value.searchQuery.trim().lowercase()
        val filterCondition = _state.value.selectedStatus
        val allParcels = _state.value.shipments

        var filtered = allParcels

        // Status Filtering
        if (filterCondition != com.om.smartpost.customer.parcel.presentation.components.ParcelFilter.ALL) {
            filtered = filtered.filter { shipment ->
                when (filterCondition) {
                    com.om.smartpost.customer.parcel.presentation.components.ParcelFilter.DELIVERED -> {
                        shipment.currentStatus == com.om.smartpost.customer.parcel.domain.model.ShipmentStatus.DELIVERED
                    }
                    com.om.smartpost.customer.parcel.presentation.components.ParcelFilter.INCOMING -> {
                        shipment.currentStatus in listOf(
                            com.om.smartpost.customer.parcel.domain.model.ShipmentStatus.IN_TRANSIT,
                            com.om.smartpost.customer.parcel.domain.model.ShipmentStatus.OUT_FOR_DELIVERY,
                            com.om.smartpost.customer.parcel.domain.model.ShipmentStatus.ARRIVED_AT_DESTINATION
                        )
                    }
                    com.om.smartpost.customer.parcel.presentation.components.ParcelFilter.SENT -> {
                        shipment.currentStatus in listOf(
                            com.om.smartpost.customer.parcel.domain.model.ShipmentStatus.CREATED,
                            com.om.smartpost.customer.parcel.domain.model.ShipmentStatus.PENDING,
                            com.om.smartpost.customer.parcel.domain.model.ShipmentStatus.ACCEPTED
                        )
                    }
                    else -> true
                }
            }
        }

        // Text query
        if (query.isNotEmpty()) {
            filtered = filtered.filter {
                it.trackingNumber.lowercase().contains(query) ||
                it.receiverName.lowercase().contains(query)
            }
        }

        _state.update { it.copy(filteredShipments = filtered) }
    }
}
