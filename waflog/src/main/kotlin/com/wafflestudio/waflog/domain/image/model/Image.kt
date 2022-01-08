package com.wafflestudio.waflog.domain.image.model

import com.wafflestudio.waflog.domain.model.BaseTimeEntity
import javax.persistence.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["email", "token"])])
class Image(

    val email: String,

    val token: String,

    val originalName: String

) : BaseTimeEntity()
