package com.example.shared.remote

import com.example.shared.domain.entity.AuthResult
import com.example.shared.network.data.network.model.TokenResponse

fun TokenResponse.toDomainEntity() = AuthResult(this.token, this.userId)