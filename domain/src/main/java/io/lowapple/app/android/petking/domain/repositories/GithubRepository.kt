package io.lowapple.app.android.petking.domain.repositories

import io.lowapple.app.android.petking.domain.models.UserInfoModel
import io.lowapple.app.android.petking.shared.GithubAccessToken
import kotlinx.coroutines.flow.Flow

interface GithubRepository {
    suspend fun setupToken(token: GithubAccessToken)
    suspend fun getUserInfo(): UserInfoModel
    suspend fun getUserInfoFromDatabase(): UserInfoModel
    suspend fun getUserInfoFromNetwork(): UserInfoModel
}