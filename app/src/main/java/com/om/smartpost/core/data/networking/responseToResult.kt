package com.om.smartpost.core.data.networking

// The inline keyword is required with the reified keyword to get the type of the generic T
// AS generic type info is only available at compile time but not rutime so we need to use inline

import com.om.smartpost.core.data.networking.dto.ErrorResponse
import com.om.smartpost.core.domain.utils.ApiError
import com.om.smartpost.core.domain.utils.NetworkError
import com.om.smartpost.core.domain.utils.Result
import com.om.smartpost.core.domain.utils.ServerResponse
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.statement.*
import io.ktor.serialization.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * Generic network response parser that maps HTTP responses into Result<T, ApiError>.
 */
suspend inline fun <reified T> responseToResult(response: HttpResponse): Result<T, ApiError> {
    return when (response.status.value) {
        in 200..299 -> {
            try {
                Result.Success(response.body<T>())
            } catch (e: NoTransformationFoundException) {
                Result.Error(ApiError.Transport(NetworkError.SERIALIZATION_ERROR))
            }
        }

        // Validation / Bad Request
        400 -> Result.Error(ApiError.Transport(NetworkError.BAD_REQ))

        // Unauthorized (invalid credentials or expired token)
        401 -> {
            val serverError = parseErrorBody(response)
            if (serverError != null)
                Result.Error(ApiError.ServerMessage(ServerResponse(serverError.code,serverError.message)))
            else
                Result.Error(ApiError.Transport(NetworkError.UNAUTHORIZED))
        }

        // Conflict (username/email/mobile exists)
        409 -> {
            println("409 conflict")
            val serverError = parseErrorBody(response)
            if (serverError != null)
                Result.Error(ApiError.ServerMessage(ServerResponse(serverError.code, serverError.message)))
            else
                Result.Error(ApiError.Conflict)
        }

        // Server-side issues
        in 500..599 -> {
            val serverError = parseErrorBody(response)
            if (serverError != null)
                Result.Error(ApiError.ServerMessage(ServerResponse(serverError.code, serverError.message)))
            else
                Result.Error(ApiError.ServerError)
        }

        // Default fallback
        else -> Result.Error(ApiError.Transport(NetworkError.UNKNOWN))
    }
}


/**
 * Parses backend structured error JSON into ApiError.ServerMessage
 */
suspend fun parseErrorBody(response: HttpResponse): ErrorResponse? {
    return try {
        val text = response.bodyAsText()
        Json.decodeFromString<ErrorResponse>(text)
    } catch (e: Exception) {
        null
    }
}