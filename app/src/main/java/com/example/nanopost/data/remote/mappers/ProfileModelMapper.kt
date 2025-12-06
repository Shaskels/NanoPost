package com.example.nanopost.data.remote.mappers

import com.example.nanopost.data.remote.model.ProfileModel
import com.example.nanopost.domain.entity.Profile

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