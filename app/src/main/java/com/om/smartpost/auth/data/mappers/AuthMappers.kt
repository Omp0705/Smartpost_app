// auth/data/mappers/RegisterMappers.kt
package com.om.smartpost.auth.data.mappers

import com.om.smartpost.auth.data.dto.LoginRequestDto
import com.om.smartpost.auth.data.dto.LoginResponseDto
import com.om.smartpost.auth.data.dto.SignUpRequestDto
import com.om.smartpost.auth.domain.AuthResult
import com.om.smartpost.auth.domain.LoginUser
import com.om.smartpost.auth.domain.RegisterUser


fun RegisterUser.toDto(): SignUpRequestDto {
    return SignUpRequestDto(
        fullName = fullName,
        username = username,
        email = email,
        mobileNo = mobileNo,
        password = password,
        role = role
    )
}

fun LoginUser.toDto(): LoginRequestDto {
    return LoginRequestDto(
        identifier = identifier,
        password = password
    )
}

fun LoginResponseDto.toDomain(): AuthResult {
    return AuthResult(
        token = token,
        refreshToken = refreshToken,
        username = username,
        role = role
    )
}