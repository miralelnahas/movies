package com.areeb.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.areeb.data.database.movies.MovieEntity
import com.areeb.data.database.movies.MoviesDao
import com.areeb.data.database.remoteKeys.RemoteKeyEntity
import com.areeb.data.database.remoteKeys.RemoteKeysDao

@Database(
    entities = [MovieEntity::class, RemoteKeyEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieEntityDao(): MoviesDao
    abstract fun remoteKeyDao(): RemoteKeysDao
}