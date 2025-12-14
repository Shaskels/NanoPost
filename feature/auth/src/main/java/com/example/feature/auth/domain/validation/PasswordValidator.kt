package com.example.feature.auth.domain.validation

import com.example.shared.domain.entity.PasswordCheckResult
import javax.inject.Inject

class PasswordValidator @Inject constructor() {

    companion object {
        private const val passwordMinLen = 8
    }

    fun validatePassword(password: String, confirmPassword: String): PasswordCheckResult {
        return if (password.length < passwordMinLen) {
            PasswordCheckResult.TooShort
        } else if (password != confirmPassword) {
            PasswordCheckResult.ConfirmFailed
        } else {
            PasswordCheckResult.Ok
        }
    }
}