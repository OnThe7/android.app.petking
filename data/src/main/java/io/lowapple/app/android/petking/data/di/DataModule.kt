package io.lowapple.app.android.petking.data.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import io.lowapple.app.android.petking.data.GithubRepositoryImpl
import io.lowapple.app.android.petking.domain.repositories.GithubRepository


@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class DataModule {
    @Binds
    abstract fun provideGithubRepository(repository: GithubRepositoryImpl): GithubRepository
}