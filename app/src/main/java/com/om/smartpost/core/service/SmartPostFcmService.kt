package com.om.smartpost.core.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.om.smartpost.auth.domain.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SmartPostFcmService : FirebaseMessagingService() {

    private val authRepository: AuthRepository by inject()
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New Token Generated: $token")
        
        // Automatically sync to backend if we are already authenticated
        scope.launch {
            val isAuthenticated = authRepository.validateSession()
            if (isAuthenticated) {
                authRepository.updateFcmToken(token)
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        // Can be routed to UI or Local Notifications later
        Log.d("FCM", "Message received from: ${message.from}")
        
        message.notification?.let {
            Log.d("FCM", "Notification Title: ${it.title}")
            Log.d("FCM", "Notification Body: ${it.body}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
