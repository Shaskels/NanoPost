package com.example.nanopost.domain.entity

enum class UsernameCheckResult() {
    TooShort,
    TooLong,
    InvalidCharacter,
    Taken,
    Free,
}