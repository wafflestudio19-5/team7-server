package com.wafflestudio.waflog.domain.user.dto

import com.wafflestudio.waflog.domain.user.model.User

class UserDto {
    data class SimpleResponse(
        val id: Long,
        val userId: String,
        val image: String
    ) {
        constructor(user: User) : this(
            id = user.id,
            userId = user.userId,
            image = user.image
        )
    }

    data class UserInPostDetailResponse(
        val id: Long,
        val name: String,
        val userId: String,
        val pageTitle: String,
        val image: String,
        val shortIntro: String,
        val publicEmail: String,
        val githubId: String,
        val facebookId: String,
        val twitterId: String,
        val homepage: String,
    ) {
        constructor(user: User) : this(
            id = user.id,
            name = user.name,
            userId = user.userId,
            pageTitle = user.pageTitle,
            image = user.image,
            shortIntro = user.shortIntro,
            publicEmail = user.publicEmail,
            githubId = user.githubId,
            facebookId = user.facebookId,
            twitterId = user.twitterId,
            homepage = user.homepage
        )
    }

    data class JoinEmailRequest(
        val email: String
    )

    data class SignUpRequest(
        val email: String,
        val name: String,
        val userId: String,
        val shortIntro: String,
        val token: String
    )

    data class UserDetailResponse(
        val id: Long,
        val name: String,
        val image: String,
        val shortIntro: String,
        val publicEmail: String,
        val githubId: String,
        val facebookId: String,
        val twitterId: String,
        val homepage: String
    ) {
        constructor(user: User) : this(
            id = user.id,
            name = user.name,
            image = user.image,
            shortIntro = user.shortIntro,
            publicEmail = user.publicEmail,
            githubId = user.githubId,
            facebookId = user.facebookId,
            twitterId = user.twitterId,
            homepage = user.homepage
        )
    }

    data class UserLongIntroResponse(
        val id: Long,
        val longIntro: String
    ) {
        constructor(user: User) : this(
            id = user.id,
            longIntro = user.longIntro
        )
    }
}
