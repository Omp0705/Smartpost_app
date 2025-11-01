package com.om.smartpost.auth.data.mappers

import com.om.smartpost.auth.data.dto.SignUpRequestDto
import com.om.smartpost.auth.data.dto.SignupResponseDto
import com.om.smartpost.auth.domain.AuthResult
import com.om.smartpost.auth.domain.RegisterUser

// to dto
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

// to domain
fun SignupResponseDto.toDomain(): AuthResult