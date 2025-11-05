package com.om.smartpost.core.data.networking

import com.om.smartpost.auth.data.dto.LoginResponseDto
import com.om.smartpost.core.data.local.TokenManager
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory {
    fun create(engine: HttpClientEngine, tokenManager: TokenManager): HttpClient {
        return HttpClient(engine) {
            install(Logging) {
                logger = object :Logger{
                    override fun log (message: String) {
                        println(message)
                    }
                }
                level =LogLevel.ALL
            }

            install(ContentNegotiation){
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        val accessToken = tokenManager.getAccessToken()
                        val refreshToken = tokenManager.getRefreshToken()
                        println("Bearer: $accessToken")
                        if (accessToken != null && refreshToken != null) {
                            BearerTokens(accessToken, refreshToken)
                        } else null
                    }
                    sendWithoutRequest { request ->
                        // Skip these endpoints
                        val path = request.url.encodedPath
                        !path.contains("/auth/login") &&
                        !path.contains("/auth/register") &&
                        !path.contains("/auth/forgot-password") &&
                        !path.contains("/auth/refresh")
                    }
                    refreshTokens {
                        println("refresh part called")
                        val refreshToken =
                            tokenManager.getRefreshToken() ?: return@refreshTokens null
                        val response = client.post(constructUrl("/api/auth/refresh")) {
                                setBody(mapOf("refreshToken" to refreshToken))
                        }
                        val newToken = response.body<LoginResponseDto>().token
                        val newRefToken = response.body<LoginResponseDto>().refreshToken
                        println("new token : $newToken")
                        tokenManager.saveTokens(newToken, newRefToken)
                        BearerTokens(newToken, newRefToken)
                        }
                    }
                }

            defaultRequest {
                contentType(ContentType.Application.Json)
            }
        }
    }
}