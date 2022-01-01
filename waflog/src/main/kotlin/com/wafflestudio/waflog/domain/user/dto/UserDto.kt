package com.wafflestudio.waflog.domain.user.dto

import com.wafflestudio.waflog.domain.user.model.User

class UserDto {
    data class SimpleResponse(
        val email: String
    ) {
        constructor(user: User) : this(
            email = user.email
        )
    }

    data class UserInPostDetailResponse(
        val id: Long,
        val name: String,
        val username: String,
        val pageTitle: String?,
        val image: String?,
        val intro: String,
        val shortIntro: String?,
        val publicEmail: String?,
        val githubId: String?,
        val facebookId: String?,
        val twitterId: String?,
        val homepage: String?,
    ) {
        constructor(user: User) : this(
            id = user.id,
            name = user.userId,
            username = user.userName,
            pageTitle = user.pageTitle,
            image = user.image,
            intro = user.intro,
            shortIntro = user.shortIntro,
            publicEmail = user.publicEmail,
            githubId = user.githubId,
            facebookId = user.facebookId,
            twitterId = user.twitterId,
            homepage = user.homepage
        )
    }

    data class SignUpEmailRequest(
        val email: String
    )

    data class SignUpRequest(
        val email: String,
        val username: String,
        val userid: String,
        val intro: String
    )
}
