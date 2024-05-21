package com.areeb.data.di

import com.areeb.data.datasources.MoviesDataSourceImpl
import com.areeb.data.managers.ConnectionManagerImpl
import com.areeb.data.managers.ConnectionManager
import com.areeb.data.repositories.MoviesDataSource
import com.areeb.data.repositories.MoviesRepository
import com.areeb.data.repositories.MoviesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface CoreComponents {

    @Binds
    fun moviesRepository(moviesRepositoryImpl: MoviesRepositoryImpl): MoviesRepository

    @Binds
    fun moviesRemoteDs(moviesDataSourceImpl: MoviesDataSourceImpl): MoviesDataSource

    @Binds
    fun connectionManager(connectionManagerImpl: ConnectionManagerImpl): ConnectionManager
}