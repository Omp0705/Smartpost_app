package com.om.smartpost.customer.profile.data.repository

import com.om.smartpost.core.domain.utils.ApiError
import com.om.smartpost.core.domain.utils.NetworkError
import com.om.smartpost.core.domain.utils.Result
import com.om.smartpost.core.domain.utils.mapError
import com.om.smartpost.customer.profile.data.dto.AddressDto
import com.om.smartpost.customer.profile.data.mapper.toDomain
import com.om.smartpost.customer.profile.data.mapper.toDto
import com.om.smartpost.customer.profile.data.mapper.toUpdateRequest
import com.om.smartpost.customer.profile.domain.model.Address
import com.om.smartpost.customer.profile.domain.model.DeliveryPreferences
import com.om.smartpost.customer.profile.domain.model.ProfileError
import com.om.smartpost.customer.profile.domain.model.UserProfile
import com.om.smartpost.customer.profile.domain.repository.ProfileRepository
import com.om.smartpost.core.data.local.TokenManager

class ProfileRepositoryImpl(
    private val remoteProfileDataSource: RemoteProfileDataSource,
    private val tokenManager: TokenManager
) : ProfileRepository {

    override suspend fun getUserProfile(): Result<UserProfile, ProfileError> {
        return when(val result = remoteProfileDataSource.getUserProfile()) {
            is Result.Success -> Result.Success(result.data.toDomain())
            is Result.Error -> {
                val profileError = when(val apiError = result.error) {
                    is ApiError.Transport -> {
                        when(apiError.networkError) {
                            NetworkError.REQUEST_TIMEOUT -> ProfileError.Network.REQUEST_TIMEOUT
                            NetworkError.TOO_MANY_REQUEST -> ProfileError.Network.TOO_MANY_REQUEST
                            NetworkError.NO_INTERNET -> ProfileError.Network.NO_INTERNET
                            NetworkError.SERVER_ERROR -> ProfileError.Network.SERVER_ERROR
                            NetworkError.SERIALIZATION_ERROR -> ProfileError.Network.SERIALIZATION_ERROR
                            NetworkError.UNKNOWN -> ProfileError.Network.UNKNOWN
                            NetworkError.UNAUTHORIZED -> ProfileError.Network.UNAUTHORIZED
                            NetworkError.BAD_REQ -> ProfileError.Network.BAD_REQ
                        }
                    }
                    is ApiError.ServerMessage -> ProfileError.Network.UNKNOWN
                    is ApiError.Conflict -> ProfileError.Network.UNKNOWN
                    is ApiError.ServerError -> ProfileError.Network.SERVER_ERROR
                }
                Result.Error(profileError)
            }
        }
    }


    override suspend fun addAddress(address: Address): Result<Unit, ProfileError> {
        return remoteProfileDataSource.addAddress(address.toDto()).mapError { mapApiErrorToProfileError(it) }
    }

    override suspend fun updateAddress(id: String, address: Address): Result<Unit, ProfileError> {
        return remoteProfileDataSource.updateAddress(id, address.toDto()).mapError { mapApiErrorToProfileError(it) }
    }

    override suspend fun deleteAddress(id: String): Result<Unit, ProfileError> {
        return remoteProfileDataSource.deleteAddress(id).mapError { mapApiErrorToProfileError(it) }
    }


    override suspend fun updateDeliveryPreference(preferences: DeliveryPreferences): Result<DeliveryPreferences, ProfileError> {
        return when (val result = remoteProfileDataSource
            .updateDeliveryPreference(preferences.toUpdateRequest())
        ){
            is Result.Success -> {
                Result.Success(data = result.data.toDomain())
            }
            is Result.Error -> {
                Result.Error(error = mapApiErrorToProfileError(result.error))
            }
        }
    }

    private fun mapApiErrorToProfileError(apiError: ApiError): ProfileError {
         return when(apiError) {
            is ApiError.Transport -> {
                when(apiError.networkError) {
                    NetworkError.NO_INTERNET -> ProfileError.Network.NO_INTERNET
                    NetworkError.REQUEST_TIMEOUT -> ProfileError.Network.REQUEST_TIMEOUT
                    else -> ProfileError.Network.UNKNOWN
                }
            }
            is ApiError.ServerError -> ProfileError.Network.SERVER_ERROR
            else -> ProfileError.Network.UNKNOWN
        }
    }

    override suspend fun logout() {
        tokenManager.clearTokens()
    }
}
