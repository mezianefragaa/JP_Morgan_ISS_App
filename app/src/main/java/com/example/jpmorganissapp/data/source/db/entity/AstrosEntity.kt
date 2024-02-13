package com.example.jpmorganissapp.data.source.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.jpmorganissapp.data.source.remote.dto.AstrosDto
import com.example.jpmorganissapp.domain.model.AstrosModel

@Entity
data class AstrosEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo (name = "craft")val craft: String,
    @ColumnInfo (name = "name")val name: String
)

fun AstrosEntity.toModel():AstrosModel{
    return AstrosModel(id,craft, name)
}