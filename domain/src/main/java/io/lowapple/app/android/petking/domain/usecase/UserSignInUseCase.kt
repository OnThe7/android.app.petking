package io.lowapple.app.android.petking.domain.usecase

import io.lowapple.app.android.petking.domain.UseCaseNonParam
import io.lowapple.app.android.petking.domain.repositories.GithubRepository
import io.lowapple.app.android.petking.shared.GithubAccessToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserSignInUseCase @Inject constructor(
    private val repository: GithubRepository
) : UseCaseNonParam<Flow<GithubAccessToken>>(Dispatchers.Main) {
    override suspend fun execute(): Flow<GithubAccessToken> {
        return flow {

        }
        // return repository.userSignIn()
    }
}