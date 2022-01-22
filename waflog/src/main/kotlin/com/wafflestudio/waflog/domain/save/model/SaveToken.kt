package com.wafflestudio.waflog.domain.save.model

import com.wafflestudio.waflog.domain.model.BaseEntity
import javax.persistence.*

@Entity
class SaveToken(

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "save_id", referencedColumnName = "id", unique = true)
    val save: Save,

    var token: String
) : BaseEntity()
