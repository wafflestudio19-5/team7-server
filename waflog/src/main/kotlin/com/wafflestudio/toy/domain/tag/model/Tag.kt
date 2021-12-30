package com.wafflestudio.toy.domain.tag.model

import com.wafflestudio.toy.domain.model.BaseEntity
import javax.persistence.Entity
import javax.validation.constraints.NotBlank

@Entity
class Tag(

    @field:NotBlank
    val name: String,

    val count: Int,

) : BaseEntity()
