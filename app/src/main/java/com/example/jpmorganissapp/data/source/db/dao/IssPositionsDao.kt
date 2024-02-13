package com.example.jpmorganissapp.data.source.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.jpmorganissapp.data.source.db.entity.IssEntity

@Dao
interface IssPositionsDao {
    @Query("SELECT * FROM IssEntity")
    suspend fun getAll(): List<IssEntity>

    @Insert
    suspend fun insert(issEntity: IssEntity)

}