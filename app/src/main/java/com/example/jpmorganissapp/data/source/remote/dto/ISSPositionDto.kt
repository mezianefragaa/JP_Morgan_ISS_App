package com.example.jpmorganissapp.data.source.remote.dto

import com.example.jpmorganissapp.data.source.db.entity.IssEntity

data class ISSPositionDto(
    val iss_position: IssPosition,
    val message: String,
    val timestamp: Long
)


fun ISSPositionDto.toEntity(): IssEntity {
    return IssEntity(latitude = iss_position.latitude, longitude = iss_position.longitude, time = timestamp)
}