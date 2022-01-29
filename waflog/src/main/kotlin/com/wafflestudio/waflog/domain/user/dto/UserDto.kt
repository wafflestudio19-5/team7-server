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
        val pageTitle: String,
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
            pageTitle = user.pageTitle,
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

    data class LongIntroModifyRequest(
        val longIntro: String
    )

    data class VerifyRequest(
        val email: String,
        val token: String
    )

    data class LogoutRequest(
        val token: String
    )

    data class UserSettingResponse(
        val id: Long,
        val name: String,
        val email: String,
        val image: String,
        val shortIntro: String,
        val pageTitle: String,
        val publicEmail: String,
        val githubId: String,
        val facebookId: String,
        val twitterId: String,
        val homepage: String,
        val commentNotification: Boolean
    ) {
        constructor(user: User) : this(
            id = user.id,
            name = user.name,
            image = user.image,
            email = user.email,
            shortIntro = user.shortIntro,
            pageTitle = user.pageTitle,
            publicEmail = user.publicEmail,
            githubId = user.githubId,
            facebookId = user.facebookId,
            twitterId = user.twitterId,
            homepage = user.homepage,
            commentNotification = user.commentNotification
        )
    }

    data class ImageDto(
        val image: String
    )

    data class ProfileDto(
        val name: String,
        val shortIntro: String = ""
    ) {
        constructor(user: User) : this(
            name = user.name,
            shortIntro = user.shortIntro
        )
    }

    data class TitleDto(
        val title: String
    ) {
        constructor(user: User) : this(
            title = user.pageTitle
        )
    }

    data class SocialInfoDto(
        val publicEmail: String = "",
        val githubId: String = "",
        val facebookId: String = "",
        val twitterId: String = "",
        val homepage: String = ""
    ) {
        constructor(user: User) : this(
            publicEmail = user.publicEmail,
            githubId = user.githubId,
            facebookId = user.facebookId,
            twitterId = user.twitterId,
            homepage = user.homepage
        )
    }

    data class CommentNotificationDto(
        val commentNotification: Boolean
    ) {
        constructor(user: User) : this(
            commentNotification = user.commentNotification
        )
    }
}
