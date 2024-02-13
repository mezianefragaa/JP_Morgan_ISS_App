package com.example.jpmorganissapp.data.source.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.jpmorganissapp.domain.model.IssModel

@Entity
data class IssEntity(
@PrimaryKey(autoGenerate = true)
val id: Long = 0,
@ColumnInfo(name = "latitude") val latitude: Double?,
@ColumnInfo(name = "longitude") val longitude: Double?,
@ColumnInfo(name = "time") val time: Long?

)
const val MAXIMUM_ISS_RECORD = 10
fun IssEntity.toModel():IssModel = IssModel(id, latitude?:0.0, longitude?:0.0, time)

