package io.lowapple.app.android.petking.data.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.components.SingletonComponent
import io.lowapple.app.android.petking.data.PetKingRepositoryImpl
import io.lowapple.app.android.petking.data.source.TrackingDataSource
import io.lowapple.app.android.petking.data.source.local.TrackingLocalDataSource
import io.lowapple.app.android.petking.domain.repositories.GithubRepository
import io.lowapple.app.android.petking.domain.repositories.PetKingRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal abstract class RepositoryModule {
    @Binds
    abstract fun providePetKingDataSourceFromLocal(dataSource: TrackingLocalDataSource): TrackingDataSource

    @Binds
    abstract fun providePetKingRepository(repository: PetKingRepositoryImpl): PetKingRepository
}

