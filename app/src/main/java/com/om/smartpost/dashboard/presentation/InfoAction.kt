package com.om.smartpost.dashboard.presentation

sealed interface InfoAction{
    object onProtectedAccess: InfoAction
}