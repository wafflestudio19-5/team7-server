package com.wafflestudio.waflog.domain.save.repository

import com.wafflestudio.waflog.domain.save.model.Save
import org.springframework.data.jpa.repository.JpaRepository

interface SaveRepository : JpaRepository<Save, Long?>
