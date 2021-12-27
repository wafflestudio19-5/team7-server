package com.wafflestudio.waflog.domain.post.repository

import com.wafflestudio.waflog.domain.post.model.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long?> {
}