package com.example.jpmorganissapp.data.source.remote.dto

import com.example.jpmorganissapp.data.source.db.entity.AstrosEntity

data class AstrosDto(
    val craft: String,
    val name: String
)
fun AstrosDto.toEntity() = AstrosEntity(craft = craft, name = name)