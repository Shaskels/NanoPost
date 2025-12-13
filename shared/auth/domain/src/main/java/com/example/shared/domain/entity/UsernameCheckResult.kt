package com.example.shared.domain.entity

enum class UsernameCheckResult {
    TooShort,
    TooLong,
    InvalidCharacter,
    Taken,
    Free,
}