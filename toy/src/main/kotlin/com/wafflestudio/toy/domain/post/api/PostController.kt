package com.wafflestudio.toy.domain.post.api

import com.wafflestudio.toy.domain.post.dto.PostDto
import com.wafflestudio.toy.domain.post.service.PostService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/post")
class PostController(
    private val postService: PostService
) {
    @GetMapping("/recent/")
    @ResponseStatus(HttpStatus.OK)
    fun getRecentPost(@PageableDefault(size = 30, sort = ["createdAt"], direction = Sort.Direction.DESC) pageable : Pageable): List<PostDto.MainPageResponse> {
        return postService.getRecentPosts(pageable)
    }

    @GetMapping("/trend/")
    @ResponseStatus(HttpStatus.OK)
    fun getTrendingPost(@PageableDefault(size = 30, sort = ["trending"], direction = Sort.Direction.DESC) pageable: Pageable,
                        @RequestParam("date", required = false, defaultValue = "7") date: Int
    ):
            List<PostDto.MainPageResponse>{
        return postService.getTrendingPosts(pageable, date)
    }
}
