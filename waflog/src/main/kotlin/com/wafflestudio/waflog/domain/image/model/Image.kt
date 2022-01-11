package com.wafflestudio.waflog.domain.image.model

import com.wafflestudio.waflog.domain.model.BaseTimeEntity
import javax.validation.constraints.NotNull
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["userId", "token"])])
class Image(
    @field:NotNull
    val userId: String,

    @field:NotBlank
    val token: String,

    val originalName: String

) : BaseTimeEntity()
