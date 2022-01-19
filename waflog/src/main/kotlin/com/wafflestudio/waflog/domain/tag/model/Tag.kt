package com.wafflestudio.waflog.domain.tag.model

import com.wafflestudio.waflog.domain.model.BaseEntity
import org.hibernate.annotations.Formula
import javax.persistence.Column
import javax.persistence.Entity
import javax.validation.constraints.NotBlank

@Entity
class Tag(

    @field:NotBlank
    val name: String,

    @Column(unique = true)
    @field:NotBlank
    val url: String,

    @Formula(value = "(SELECT count(1) FROM post_tag pt WHERE pt.tag_id = id)")
    val trending: Int = 0,

) : BaseEntity()
