package com.example.shared.remote

import com.example.shared.domain.entity.Profile
import com.example.shared.domain.entity.ProfileCompact
import com.example.shared.network.data.network.model.ProfileCompactModel
import com.example.shared.network.data.network.model.ProfileModel

fun ProfileModel.toDomainProfile() = Profile(
    id = this.id,
    username = this.username,
    displayName = this.displayName,
    bio = this.bio,
    avatarId = this.avatarId,
    avatarSmall = this.avatarSmall,
    avatarLarge = this.avatarLarge,
    subscribed = this.subscribed,
    subscribersCount = this.subscribersCount,
    postsCount = this.postsCount,
    imagesCount = this.imagesCount
)

fun ProfileCompactModel.toDomainProfileCompact(): ProfileCompact = ProfileCompact(
    id = this.id,
    username = this.username,
    displayName = this.displayName,
    avatarUrl = this.avatarUrl,
    subscribed = this.subscribed ?: false
)