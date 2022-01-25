package com.wafflestudio.waflog.domain.save.model

import com.wafflestudio.waflog.domain.image.model.Image
import com.wafflestudio.waflog.domain.model.BaseTimeEntity
import com.wafflestudio.waflog.domain.user.model.User
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class Save(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @field:NotNull
    val user: User,

    @field:NotBlank
    var title: String,

    @field:NotBlank
    var content: String,

    @OneToMany(mappedBy = "save")
    var images: MutableList<Image>,

    @ElementCollection
    var saveTags: List<String>

) : BaseTimeEntity()
