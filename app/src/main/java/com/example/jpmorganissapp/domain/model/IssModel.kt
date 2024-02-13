package com.example.jpmorganissapp.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class IssModel(
val id: Long,
val latitude: Double,
val longitude: Double,
val time: Long?
)

