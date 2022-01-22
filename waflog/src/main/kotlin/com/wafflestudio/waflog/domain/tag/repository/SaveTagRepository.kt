package com.wafflestudio.waflog.domain.tag.repository

import com.wafflestudio.waflog.domain.tag.model.SaveTag
import org.springframework.data.jpa.repository.JpaRepository

interface SaveTagRepository : JpaRepository<SaveTag, Long?>
