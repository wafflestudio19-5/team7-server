package com.wafflestudio.waflog.domain.tag.api

import com.wafflestudio.waflog.domain.post.dto.PostDto
import com.wafflestudio.waflog.domain.tag.dto.TagDto
import com.wafflestudio.waflog.domain.tag.service.TagService
import com.wafflestudio.waflog.domain.user.model.User
import com.wafflestudio.waflog.global.auth.CurrentUser
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/tag")
class TagController(
    private val tagService: TagService
) {
    @GetMapping("")
    fun getTrendingTags(
        @PageableDefault(
            size = 20, sort = ["trending"], direction = Sort.Direction.DESC
        ) pageable: Pageable
    ): Page<TagDto.TagResponse> {
        return tagService.getTags(pageable)
    }

    @GetMapping("/{tag_url}")
    fun getPostsWithTag(
        @PageableDefault(
            size = 30
        ) pageable: Pageable,
        @PathVariable("tag_url") tagUrl: String,
        @CurrentUser user: User?
    ): Page<PostDto.SearchResultResponse> {
        return tagService.getPostsWithTag(pageable, tagUrl, user)
    }
}
