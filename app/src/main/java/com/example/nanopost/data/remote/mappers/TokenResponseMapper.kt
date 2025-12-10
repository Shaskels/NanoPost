package com.example.nanopost.data.remote.mappers

import com.example.nanopost.data.remote.network.model.TokenResponse
import com.example.nanopost.domain.entity.AuthResult

fun TokenResponse.toDomainEntity() = AuthResult(this.token, this.userId)