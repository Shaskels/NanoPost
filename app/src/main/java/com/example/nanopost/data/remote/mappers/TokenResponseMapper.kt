package com.example.nanopost.data.remote.mappers

import com.example.nanopost.domain.entity.AuthResult
import com.example.shared.network.data.network.model.TokenResponse


fun TokenResponse.toDomainEntity() = AuthResult(this.token, this.userId)