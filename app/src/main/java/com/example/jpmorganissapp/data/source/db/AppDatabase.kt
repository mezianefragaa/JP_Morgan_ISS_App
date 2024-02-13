package com.example.jpmorganissapp.data.source.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.jpmorganissapp.data.source.db.dao.AstrosDao
import com.example.jpmorganissapp.data.source.db.dao.IssPositionsDao
import com.example.jpmorganissapp.data.source.db.entity.AstrosEntity
import com.example.jpmorganissapp.data.source.db.entity.IssEntity

@Database(entities = [IssEntity::class,AstrosEntity::class], version = 4)
abstract class AppDatabase() : RoomDatabase() {
        abstract fun issPositionsDao(): IssPositionsDao
        abstract fun astrosDao(): AstrosDao

    }
