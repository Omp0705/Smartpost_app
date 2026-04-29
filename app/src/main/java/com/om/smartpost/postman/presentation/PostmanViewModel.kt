package com.om.smartpost.postman.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.om.smartpost.core.domain.utils.Result
import com.om.smartpost.postman.domain.model.Beat
import com.om.smartpost.postman.domain.model.Shipment
import com.om.smartpost.postman.domain.repository.PostmanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PostmanState(
    val isLoading: Boolean = false,
    val currentBeat: Beat? = null,
    val shipments: List<Shipment> = emptyList(),
    val error: String? = null
)

class PostmanViewModel(
    private val repository: PostmanRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PostmanState())
    val state: StateFlow<PostmanState> = _state.asStateFlow()

    init {
        fetchAssignedBeats()
    }

    fun fetchAssignedBeats() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            when (val result = repository.getAssignedBeats()) {
                is Result.Success -> {
                    val beats = result.data
                    if (beats.isNotEmpty()) {
                        // Assuming the postman has at least one active assigned beat. Take the first one.
                        val firstBeat = beats.first()
                        _state.update { it.copy(currentBeat = firstBeat) }
                        // Fetch shipments for this beat
                        fetchShipmentsForBeat(firstBeat.id)
                    } else {
                        _state.update { 
                            it.copy(isLoading = false, error = "No assigned beats found.") 
                        }
                    }
                }
                is Result.Error -> {
                    _state.update { 
                        it.copy(isLoading = false, error = "Failed to load assigned beats") 
                    }
                }
            }
        }
    }

    private fun fetchShipmentsForBeat(beatId: String) {
        viewModelScope.launch {
            when (val result = repository.getShipmentsForBeat(beatId)) {
                is Result.Success -> {
                    _state.update { 
                        it.copy(
                            isLoading = false, 
                            shipments = result.data,
                            error = null
                        ) 
                    }
                }
                is Result.Error -> {
                    _state.update { 
                        it.copy(
                            isLoading = false, 
                            error = "Failed to load shipments for beat" 
                        ) 
                    }
                }
            }
        }
    }
}
