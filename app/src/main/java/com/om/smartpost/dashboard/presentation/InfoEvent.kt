package com.om.smartpost.dashboard.presentation

sealed class InfoEvent {
    data class ShowMessage(val message: String) : InfoEvent()
}