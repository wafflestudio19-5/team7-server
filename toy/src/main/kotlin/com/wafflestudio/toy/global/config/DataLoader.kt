package com.wafflestudio.toy.global.config

import com.wafflestudio.toy.domain.post.model.Post
import com.wafflestudio.toy.domain.post.repository.PostRepository
import com.wafflestudio.toy.domain.user.model.User
import com.wafflestudio.toy.domain.user.repository.UserRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("local")
class DataLoader(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository
) : ApplicationRunner {
    //test data in local
    override fun run(args: ApplicationArguments?) {
        val userA = User(
            email = "waffle@nsnu.ac.kr",
            name = "James",
            username = "Waffle",
            password = "password",
            intro = "",
            image = "",
            pageTitle = "Waffle's Page",
            publicEmail = "waffle@nsnu.ac.kr",
            githubId = "",
            facebookId = "",
            twitterId = "",
            homepage = "",
            commentNotification = false,
            updateNotification = false
        )

        val userB = User(
            email = "studio@nsnu.ac.kr",
            name = "Peter",
            username = "Studio",
            password = "password",
            intro = "",
            image = "",
            pageTitle = "Studio's Page",
            publicEmail = "studio@nsnu.ac.kr",
            githubId = "",
            facebookId = "",
            twitterId = "",
            homepage = "",
            commentNotification = false,
            updateNotification = false
        )

        val userC = User(
            email = "team7@nsnu.ac.kr",
            name = "Annie",
            username = "Team7",
            password = "password",
            intro = "",
            image = "",
            pageTitle = "Team7's Page",
            publicEmail = "Team7@nsnu.ac.kr",
            githubId = "",
            facebookId = "",
            twitterId = "",
            homepage = "",
            commentNotification = false,
            updateNotification = false
        )

        userRepository.save(userA)
        userRepository.save(userB)
        userRepository.save(userC)

        val postA = Post(
            user = userA,
            title = "Waffle's Spring",
            content = "blah blah",
            likes = 0,
            thumbnail = "",
            summary = "summary of A",
            private = false,
            url = "velog/post/1",
            series = null,
            comments = mutableListOf()
        )
        val postB = Post(
            user = userB,
            title = "Studio's Spring",
            content = "blah blah",
            likes = 0,
            thumbnail = "",
            summary = "summary of A",
            private = true,             //private post
            url = "velog/post/1",
            series = null,
            comments = mutableListOf()
        )
        val postC = Post(
            user = userC,
            title = "Team7's Spring",
            content = "blah blah",
            likes = 0,
            thumbnail = "",
            summary = "summary of A",
            private = false,
            url = "velog/post/1",
            series = null,
            comments = mutableListOf()
        )

        postRepository.save(postA)
        postRepository.save(postB)
        postRepository.save(postC)


    }
}