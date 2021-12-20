package com.wafflestudio.toy.domain.tag.model

import com.wafflestudio.toy.domain.model.BaseEntity
import com.wafflestudio.toy.domain.save.model.Save
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "save_tag")
class SaveTag (
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "save_id", referencedColumnName = "id")
    @field:NotNull
    val save: Save,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", referencedColumnName = "id")
    @field:NotNull
    val tag: Tag,
    ): BaseEntity()
