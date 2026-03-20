package com.om.smartpost.customer.notifications.data

import com.om.smartpost.core.domain.utils.ApiError
import com.om.smartpost.core.domain.utils.Result
import com.om.smartpost.customer.notifications.data.mappers.toDomain
import com.om.smartpost.customer.notifications.domain.models.Notification
import com.om.smartpost.customer.notifications.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NotificationRepositoryImpl(
    private val remoteDataSource: RemoteNotificationDataSource
) : NotificationRepository {

    override fun getNotifications(): Flow<Result<List<Notification>, ApiError>> = flow {
        when (val result = remoteDataSource.fetchNotifications()) {
            is Result.Success -> {
                val domainNotifications = result.data.map { it.toDomain() }

                val sortedList = domainNotifications.sortedByDescending { it.id }

                emit(Result.Success(sortedList))
            }
            is Result.Error -> {
                emit(Result.Error(result.error))
            }
        }
    }

    override suspend fun deleteNotification(id: Long): Result<Unit, ApiError> {
        return remoteDataSource.deleteNotification(id)
    }
}