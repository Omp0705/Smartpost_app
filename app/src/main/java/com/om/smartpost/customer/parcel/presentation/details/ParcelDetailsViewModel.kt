package com.om.smartpost.customer.parcel.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.om.smartpost.customer.parcel.domain.repository.ShipmentRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ParcelDetailsViewModel(
    private val parcelId: String,
    private val shipmentRepository: ShipmentRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ParcelDetailsState())
    val state = _state.asStateFlow()

    private val _events = Channel<ParcelDetailsEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadParcel()
    }

    fun onAction(action: ParcelDetailsAction) {
        when (action) {
            is ParcelDetailsAction.OnBackClick -> {
                viewModelScope.launch { _events.send(ParcelDetailsEvent.NavigateBack) }
            }
            is ParcelDetailsAction.OnRescheduleClick -> {
                viewModelScope.launch { _events.send(ParcelDetailsEvent.ShowSnackbar("Mock: Reschedule Clicked")) }
            }
            is ParcelDetailsAction.OnContactSupportClick -> {
                viewModelScope.launch { _events.send(ParcelDetailsEvent.ShowSnackbar("Mock: Contact Support Clicked")) }
            }
        }
    }

    private fun loadParcel() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            shipmentRepository.getShipmentById(parcelId)
                .collect { result ->
                    when(result) {
                        is com.om.smartpost.core.domain.utils.Result.Success -> {
                            _state.update { it.copy(isLoading = false, parcel = result.data) }
                        }
                        is com.om.smartpost.core.domain.utils.Result.Error -> {
                            val errorMsg = result.error.toString()
                            _state.update { it.copy(isLoading = false, error = errorMsg) }
                            _events.send(ParcelDetailsEvent.ShowSnackbar(errorMsg))
                        }
                    }
                }
        }
    }
}
