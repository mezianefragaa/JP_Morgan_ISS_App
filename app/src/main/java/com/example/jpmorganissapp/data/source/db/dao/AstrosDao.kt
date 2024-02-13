package com.example.jpmorganissapp.data.source.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.jpmorganissapp.data.source.db.entity.AstrosEntity
import com.example.jpmorganissapp.data.source.db.entity.IssEntity

@Dao
interface AstrosDao {
    @Query("SELECT * FROM AstrosEntity")
    suspend fun getAll():List<AstrosEntity>?

    @Insert
    suspend fun insert(issEntity: AstrosEntity)

    @Insert
    suspend fun insertAll(issEntity: List<AstrosEntity>)
}