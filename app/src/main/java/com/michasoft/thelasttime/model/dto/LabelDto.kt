package com.michasoft.thelasttime.model.dto

import com.michasoft.thelasttime.model.Label

class LabelDto(
    var name: String? = null
) {
    constructor(label: Label) : this(label.name)

    fun toModel(id: String) = Label(id, name!!)
}