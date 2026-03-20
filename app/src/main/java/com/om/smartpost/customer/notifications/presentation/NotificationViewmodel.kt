package com.om.smartpost.customer.notifications.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.om.smartpost.customer.notifications.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.om.smartpost.core.domain.utils.Result

class NotificationsViewModel(
    private val repository: NotificationRepository
) : ViewModel() {

    private val _state = MutableStateFlow(NotificationsState())
    val state = _state.asStateFlow()

    init { loadNotifications() }

    fun onAction(action: NotificationsAction) {
        when (action) {
            is NotificationsAction.OnDeleteClick -> deleteNotification(action.id)
            NotificationsAction.OnRefresh -> loadNotifications()
            NotificationsAction.OnBackClick -> {}
            is NotificationsAction.OnNotificationClick -> {}
        }
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            repository.getNotifications().collect { result ->
                when (result) {
                    is Result.Success -> _state.update {
                        it.copy(isLoading = false, notifications = result.data)
                    }
                    is Result.Error -> _state.update {
                        it.copy(isLoading = false, error = "Failed to load notifications")
                    }
                }
            }
        }
    }

    private fun deleteNotification(id: Long) {
        viewModelScope.launch {
            // Optimistic update: remove from UI immediately
            val currentList = _state.value.notifications
            _state.update { it.copy(notifications = currentList.filter { notif -> notif.id != id }) }

            val result = repository.deleteNotification(id)
            if (result is Result.Error) {
                // Revert on failure
                _state.update { it.copy(notifications = currentList, error = "Failed to delete") }
            }
        }
    }
}