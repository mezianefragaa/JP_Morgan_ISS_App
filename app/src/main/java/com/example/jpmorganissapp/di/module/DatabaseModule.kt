package com.example.jpmorganissapp.di.module

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.jpmorganissapp.data.source.db.AppDatabase
import com.example.jpmorganissapp.data.source.db.entity.MAXIMUM_ISS_RECORD
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    private const val DATABASE_NAME = "issDatabase"

    /**
     * Trigger_Querry is not functional
     * since we insert every 5 seconds, we need to set a limit for our table to prevent memory leak
     * there's another option, we add a scheduler that delete the database every set of time
     */
    private const val TRIGGER_QUERY = "CREATE TRIGGER delete_old_records\n" +
            "AFTER INSERT ON IssEntity" +
            "BEGIN" +
            "    DELETE FROM IssEntity" +
            "    WHERE id NOT IN (" +
            "        SELECT id FROM IssEntity" +
            "        ORDER BY id DESC" +
            "        LIMIT $MAXIMUM_ISS_RECORD" +
            "    );" +
            "END;"

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context:Context,
        callback:RoomDatabase.Callback,
    ) : AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            DATABASE_NAME
        )
            .addCallback(callback)
//            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideDatabaseCallback():RoomDatabase.Callback = object :RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
//            Log.i("!@De123", "onCreate:run trigger ")
//            db.execSQL(TRIGGER_QUERY)
        }

//        override fun onOpen(db: SupportSQLiteDatabase) {
//            super.onOpen(db)
//            Log.i("!@De123", "onOpen database ")
//        }
    }


}