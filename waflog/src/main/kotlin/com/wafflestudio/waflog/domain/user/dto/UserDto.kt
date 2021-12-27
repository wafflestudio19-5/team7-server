package com.wafflestudio.waflog.domain.user.dto

import com.wafflestudio.waflog.domain.user.model.User


class UserDto {
    data class SimpleResponse(
        val id: Long,
        val username: String,
        val image: String
    ) {
        constructor(user: User) : this(
            id = user.id,
            username = user.username,
            image = user.image
        )
    }

    data class UserInPostDetailResponse(
        val id: Long,
        val name: String,
        val username: String,
        val image: String,
        val intro: String,
        val publicEmail: String,
        val githubId: String,
        val facebookId: String,
        val twitterId: String,
        val homepage: String,
    ){
        constructor(user: User) : this(
            id = user.id,
            name = user.name,
            username = user.username,
            image = user.image,
            intro = user.intro,
            publicEmail = user.publicEmail,
            githubId = user.githubId,
            facebookId = user.facebookId,
            twitterId = user.twitterId,
            homepage = user.homepage
        )
    }
}
