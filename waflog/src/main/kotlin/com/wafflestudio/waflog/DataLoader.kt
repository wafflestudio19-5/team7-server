package com.wafflestudio.waflog

import com.wafflestudio.waflog.domain.post.model.Comment
import com.wafflestudio.waflog.domain.post.model.Post
import com.wafflestudio.waflog.domain.post.repository.CommentRepository
import com.wafflestudio.waflog.domain.post.repository.PostRepository
import com.wafflestudio.waflog.domain.user.model.Series
import com.wafflestudio.waflog.domain.user.model.User
import com.wafflestudio.waflog.domain.user.repository.SeriesRepository
import com.wafflestudio.waflog.domain.user.repository.UserRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("local")
class DataLoader(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val seriesRepository: SeriesRepository
) : ApplicationRunner {
    // test data in local
    override fun run(args: ApplicationArguments?) {
        val userA = User(
            email = "waffle@nsnu.ac.kr",
            userId = "James",
            name = "Waffle",
            shortIntro = "",
            pageTitle = "Waffle's Page",
            publicEmail = "waffle@nsnu.ac.kr",
            commentNotification = false,
            updateNotification = false,
            posts = mutableListOf()
        )

        val userB = User(
            email = "studio@nsnu.ac.kr",
            userId = "Peter",
            name = "Studio",
            shortIntro = "",
            pageTitle = "Studio's Page",
            publicEmail = "studio@nsnu.ac.kr",
            commentNotification = false,
            updateNotification = false,
            posts = mutableListOf()
        )

        val userC = User(
            email = "team7@nsnu.ac.kr",
            userId = "Annie",
            name = "Team7",
            shortIntro = "",
            pageTitle = "Team7's Page",
            publicEmail = "Team7@nsnu.ac.kr",
            commentNotification = false,
            updateNotification = false,
            posts = mutableListOf()
        )

        userRepository.save(userA)
        userRepository.save(userB)
        userRepository.save(userC)

        val series1 = Series(
            name = "A_s1",
            user = userA,
            posts = mutableListOf()
        )
        val series2 = Series(
            name = "A_s2",
            user = userA,
            posts = mutableListOf()
        )

        seriesRepository.save(series1)
        seriesRepository.save(series2)
        val postA1 = Post(
            user = userA,
            title = "Waffle's Spring",
            content = "blah blah",
            likes = 0,
            thumbnail = "",
            summary = "summary of A",
            private = false,
            url = "velog/post/1",
            series = series1,
            comments = mutableListOf(),
            postTags = mutableListOf()
        )
        val postA2 = Post(
            user = userA,
            title = "Waffle's Spring",
            content = "blah blah",
            likes = 0,
            thumbnail = "",
            summary = "summary of A",
            private = false,
            url = "velog/post/1",
            series = series1,
            comments = mutableListOf(),
            postTags = mutableListOf()
        )
        val postA3 = Post(
            user = userA,
            title = "Waffle's Spring",
            content = "blah blah",
            likes = 0,
            thumbnail = "",
            summary = "summary of A",
            private = false,
            url = "velog/post/1",
            series = series2,
            comments = mutableListOf(),
            postTags = mutableListOf()
        )
        val postA4 = Post(
            user = userA,
            title = "Waffle's Spring",
            content = "blah blah",
            likes = 0,
            thumbnail = "",
            summary = "summary of A",
            private = false,
            url = "velog/post/1",
            series = series1,
            comments = mutableListOf(),
            postTags = mutableListOf()
        )

        val postB = Post(
            user = userB,
            title = "Studio's Spring",
            content = "blah blah",
            likes = 0,
            thumbnail = "",
            summary = "summary of B",
            private = true, // private post
            url = "velog/post/2",
            series = null,
            comments = mutableListOf(),
            postTags = mutableListOf()
        )
        val postC = Post(
            user = userC,
            title = "Team7's Spring",
            content = "blah blah",
            likes = 0,
            thumbnail = "",
            summary = "summary of C",
            private = false,
            url = "velog/post/3",
            series = null,
            comments = mutableListOf(),
            postTags = mutableListOf()
        )

        val postD = Post(
            user = userB,
            title = "1st Trending Spring",
            content = "This is the most trending post",
            views = 8,
            likes = 10,
            thumbnail = "",
            summary = "summary of D",
            private = false,
            url = "velog/post/4",
            series = null,
            comments = mutableListOf(),
            postTags = mutableListOf()
        )

        val postE = Post(
            user = userC,
            title = "2nd Trending Spring",
            content = "This is the second most trending post",
            views = 8,
            likes = 10,
            thumbnail = "",
            summary = "summary of E",
            private = false,
            url = "velog/post/5",
            series = null,
            comments = mutableListOf(),
            postTags = mutableListOf()
        )

        val postF = Post(
            user = userA,
            title = "3rd Trending Spring",
            content = "This is the third most trending post",
            views = 7,
            likes = 11,
            thumbnail = "",
            summary = "summary of F",
            private = false,
            url = "velog/post/6",
            series = null,
            comments = mutableListOf(),
            postTags = mutableListOf()
        )

        val postG = Post(
            user = userB,
            title = "Post with content",
            content = "content to search",
            views = 7,
            likes = 11,
            thumbnail = "",
            summary = "summary of F",
            private = false,
            url = "velog/post/7",
            series = null,
            comments = mutableListOf(),
            postTags = mutableListOf()
        )

        val postWithComments = Post(
            user = userA,
            title = "Post with comments",
            content = "content of the post",
            views = 7,
            likes = 11,
            thumbnail = "",
            summary = "summary of the post",
            private = false,
            url = "velog/post/8",
            series = null,
            comments = mutableListOf(),
            postTags = mutableListOf()
        )

        postRepository.save(postA1)
        postRepository.save(postA2)
        postRepository.save(postA3)
        postRepository.save(postA4)

        postRepository.save(postB)
        postRepository.save(postC)
        postRepository.save(postD)
        postRepository.save(postE)
        postRepository.save(postF)
        postRepository.save(postG)
        postRepository.save(postWithComments)

        val commentSample = Comment(
            user = userB,
            post = postD,
            rootComment = 1,
            depth = 0,
            content = "Sample comment"
        )

        commentRepository.save(commentSample)

        val commentA = Comment(
            user = userA,
            post = postWithComments,
            depth = 0,
            content = "aaaaa",
            rgt = 8
        )

        val commentD = Comment(
            user = userB,
            post = postWithComments,
            depth = 0,
            content = "ddddd",
            rgt = 8
        )

        val commentH = Comment(
            user = userC,
            post = postWithComments,
            depth = 0,
            content = "hhhhh"
        )

        val commentI = Comment(
            user = userB,
            post = postWithComments,
            depth = 0,
            content = "iiiii"
        )

        val rootCommentList = listOf(commentA, commentD, commentH, commentI)
        rootCommentList.forEach {
            commentRepository.save(it)
        }

        val commentB = Comment(
            user = userB,
            post = postWithComments,
            rootComment = commentA.id,
            depth = 1,
            content = "bbbbb",
            lft = 2,
            rgt = 7
        )

        val commentC = Comment(
            user = userC,
            post = postWithComments,
            rootComment = commentA.id,
            depth = 1,
            content = "ccccc",
            lft = 5,
            rgt = 6
        )

        val commentE = Comment(
            user = userA,
            post = postWithComments,
            rootComment = commentD.id,
            depth = 1,
            content = "eeeee",
            lft = 2,
            rgt = 3
        )

        val commentF = Comment(
            user = userC,
            post = postWithComments,
            rootComment = commentA.id,
            depth = 2,
            content = "fffff",
            lft = 3,
            rgt = 4
        )

        val commentG = Comment(
            user = userC,
            post = postWithComments,
            rootComment = commentD.id,
            depth = 1,
            content = "ggggg",
            lft = 4,
            rgt = 7
        )

        val commentJ = Comment(
            user = userA,
            post = postWithComments,
            rootComment = commentD.id,
            depth = 2,
            content = "jjjjj",
            lft = 5,
            rgt = 6
        )

        val commentList = listOf(
            commentB, commentC, commentE,
            commentF, commentG, commentJ
        )
        commentList.forEach {
            commentRepository.save(it)
        }
    }
}
