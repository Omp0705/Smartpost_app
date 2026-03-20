package com.om.smartpost.customer.profile.domain.repository

import com.om.smartpost.core.domain.utils.Result
import com.om.smartpost.core.domain.utils.NetworkError
import com.om.smartpost.customer.profile.domain.model.ProfileError
import com.om.smartpost.customer.profile.domain.model.Address
import com.om.smartpost.customer.profile.domain.model.DeliveryPreferences
import com.om.smartpost.customer.profile.domain.model.DeliverySlot
import com.om.smartpost.customer.profile.domain.model.UserProfile

interface ProfileRepository {
    suspend fun getUserProfile(): Result<UserProfile, ProfileError>
    suspend fun addAddress(address: Address): Result<Unit, ProfileError>
    suspend fun updateAddress(id: String, address: Address): Result<Unit, ProfileError>
    suspend fun deleteAddress(id: String): Result<Unit, ProfileError>
    suspend fun updateDeliveryPreference(preferences: DeliveryPreferences): Result<DeliveryPreferences, ProfileError>
    suspend fun logout()
}
