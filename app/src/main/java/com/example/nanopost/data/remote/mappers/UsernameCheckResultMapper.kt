package com.example.nanopost.data.remote.mappers

import com.example.nanopost.data.remote.model.UsernameCheckResultModel
import com.example.nanopost.domain.entity.UsernameCheckResult

fun UsernameCheckResultModel.toDomainEntity() = when(this) {
    UsernameCheckResultModel.TooShort -> UsernameCheckResult.TooShort
    UsernameCheckResultModel.TooLong -> UsernameCheckResult.TooLong
    UsernameCheckResultModel.InvalidCharacter -> UsernameCheckResult.InvalidCharacter
    UsernameCheckResultModel.Taken -> UsernameCheckResult.Taken
    UsernameCheckResultModel.Free -> UsernameCheckResult.Free
}