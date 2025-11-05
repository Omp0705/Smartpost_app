package com.om.smartpost.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.om.smartpost.core.domain.utils.Result
import com.om.smartpost.dashboard.domain.InfoRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class UserInfoViewModel(
    private val infoRepository: InfoRepository
) : ViewModel() {

    private val _state = MutableStateFlow(InfoUiState())
    val state = _state.asStateFlow()

    private val _events = Channel<InfoEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: InfoAction) {
        when (action) {
            InfoAction.onProtectedAccess -> getProtectedData()
        }
    }

    private fun getProtectedData() {
        viewModelScope.launch {
            _state.update { it.copy(
                username = "",
                role = ""
            ) }
            val res = infoRepository.getProtectedData()
            when (res) {
                is Result.Success -> {
                    _state.update { it.copy(
                        username = res.data.username,
                        role = res.data.role
                    ) }
                }
                is Result.Error -> {
                    _events.send(InfoEvent.ShowMessage("Error Occurred"))
                }
            }
        }
    }


}