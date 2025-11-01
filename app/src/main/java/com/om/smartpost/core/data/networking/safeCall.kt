package com.om.smartpost.core.data.networking

import com.om.smartpost.core.domain.utils.NetworkError
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext
import com.om.smartpost.core.domain.utils.Result


suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse
): Result<T, NetworkError>{
    val response = try{
        execute()
    }
    catch (e: UnresolvedAddressException){
        return Result.Error(NetworkError.NO_INTERNET)
    }
    catch (e: NoTransformationFoundException){
        return Result.Error(NetworkError.SERIALIZATION_ERROR)
    }
    catch (e: Exception){
        coroutineContext.ensureActive()
        return Result.Error(NetworkError.UNKNOWN)
    }
    return responseToResult(response)
}