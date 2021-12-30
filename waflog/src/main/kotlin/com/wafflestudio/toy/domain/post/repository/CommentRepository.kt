package com.wafflestudio.toy.domain.post.repository

import com.wafflestudio.toy.domain.post.model.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long?>
