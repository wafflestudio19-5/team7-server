package com.wafflestudio.waflog.domain.post

import com.wafflestudio.waflog.domain.post.service.PostService
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class PostServiceTest {
    @InjectMocks
    lateinit var postService: PostService
}
