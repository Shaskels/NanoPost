package com.example.nanopost.domain.entity

data class ProfileCompact(
    val id: String,
    val username: String,
    val displayName: String?,
    val avatarUrl: String?,
    val subscribed: Boolean,
)