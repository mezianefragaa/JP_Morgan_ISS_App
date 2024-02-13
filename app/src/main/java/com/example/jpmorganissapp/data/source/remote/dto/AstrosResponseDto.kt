package com.example.jpmorganissapp.data.source.remote.dto

import com.example.jpmorganissapp.data.source.db.entity.AstrosEntity

data class AstrosResponseDto(
    val message: String,
    val number: Int,
    val people: List<AstrosDto>
)

fun AstrosResponseDto.toEntity():List<AstrosEntity>{
    return this.people.map { it.toEntity() }
}