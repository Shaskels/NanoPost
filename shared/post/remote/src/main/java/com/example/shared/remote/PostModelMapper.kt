package com.example.shared.remote

import com.example.shared.domain.entity.Image
import com.example.shared.domain.entity.ImageSize
import com.example.shared.domain.entity.Likes
import com.example.shared.domain.entity.Post
import com.example.shared.network.data.network.model.ImageModel
import com.example.shared.network.data.network.model.ImageSizeModel
import com.example.shared.network.data.network.model.LikesModel
import com.example.shared.network.data.network.model.PostModel

fun PostModel.toDomainPost(): Post = Post(
    id = this.id,
    owner = this.owner.toDomainProfileCompact(),
    dateCreated = this.dateCreated ,
    text = this.text,
    images = this.images.map { it.toDomainImage() },
    likes = this.likes.toDomainLikes()
)

fun ImageModel.toDomainImage(): Image = Image(
    id = this.id,
    owner = this.owner.toDomainProfileCompact(),
    dateCreated = this.dateCreated,
    sizes = this.sizes.map { it.toDomainImageSize() }
)

fun ImageSizeModel.toDomainImageSize(): ImageSize = ImageSize(
    width = this.width,
    height = this.height,
    url = this.url
)

fun LikesModel.toDomainLikes(): Likes = Likes(
    liked = this.liked,
    likesCount = this.likesCount
)