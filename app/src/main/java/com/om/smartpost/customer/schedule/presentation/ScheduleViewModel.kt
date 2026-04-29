package com.om.smartpost.customer.schedule.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.om.smartpost.core.domain.utils.Result
import com.om.smartpost.customer.parcel.domain.model.Shipment
import com.om.smartpost.customer.parcel.domain.model.ShipmentStatus
import com.om.smartpost.customer.parcel.domain.repository.ShipmentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ScheduleState(
    val isLoading: Boolean = false,
    val incomingShipments: List<Shipment> = emptyList(),
    val error: String? = null,
    val selectedShipment: Shipment? = null,
    val isUpdatingTimeslot: Boolean = false,
    val isBottomSheetVisible: Boolean = false
)

sealed interface ScheduleAction {
    data class OnShipmentClick(val shipment: Shipment) : ScheduleAction
    data object OnBottomSheetDismiss : ScheduleAction
    data class OnSaveTimeslot(val newTimeslot: String) : ScheduleAction
}

class ScheduleViewModel(
    private val shipmentRepository: ShipmentRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ScheduleState())
    val state = _state.asStateFlow()

    init {
        loadIncomingShipments()
    }

    fun onAction(action: ScheduleAction) {
        when (action) {
            is ScheduleAction.OnShipmentClick -> {
                _state.update { 
                    it.copy(
                        selectedShipment = action.shipment,
                        isBottomSheetVisible = true
                    ) 
                }
            }
            is ScheduleAction.OnBottomSheetDismiss -> {
                _state.update { 
                    it.copy(
                        selectedShipment = null,
                        isBottomSheetVisible = false
                    ) 
                }
            }
            is ScheduleAction.OnSaveTimeslot -> {
                updateTimeslot(action.newTimeslot)
            }
        }
    }

    private fun loadIncomingShipments() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            shipmentRepository.getAllShipments().collect { result ->
                when (result) {
                    is Result.Success -> {
                        val incoming = result.data.filter { 
                            it.currentStatus in listOf(
                                ShipmentStatus.IN_TRANSIT,
                                ShipmentStatus.OUT_FOR_DELIVERY,
                                ShipmentStatus.ARRIVED_AT_DESTINATION,
                                ShipmentStatus.PENDING,
                                ShipmentStatus.ACCEPTED,
                                ShipmentStatus.CREATED
                            ) 
                        }
                        _state.update { 
                            it.copy(
                                isLoading = false,
                                incomingShipments = incoming
                            ) 
                        }
                    }
                    is Result.Error -> {
                        _state.update { 
                            it.copy(
                                isLoading = false,
                                error = "Failed to load incoming shipments"
                            ) 
                        }
                    }
                }
            }
        }
    }

    private fun updateTimeslot(newTimeslot: String) {
        val shipmentId = _state.value.selectedShipment?.id ?: return
        
        viewModelScope.launch {
            _state.update { it.copy(isUpdatingTimeslot = true) }
            
            val result = shipmentRepository.updateShipmentTimeslot(shipmentId, newTimeslot)
            
            when (result) {
                is Result.Success -> {
                    // Update locally or reload
                    val updatedList = _state.value.incomingShipments.map { 
                        if (it.id == shipmentId) it.copy(predictedSlot = newTimeslot) else it
                    }
                    
                    _state.update { 
                        it.copy(
                            incomingShipments = updatedList,
                            isUpdatingTimeslot = false,
                            isBottomSheetVisible = false,
                            selectedShipment = null
                        ) 
                    }
                }
                is Result.Error -> {
                    _state.update { 
                        it.copy(
                            isUpdatingTimeslot = false,
                            error = "Failed to update timeslot."
                        ) 
                    }
                }
            }
        }
    }
}
