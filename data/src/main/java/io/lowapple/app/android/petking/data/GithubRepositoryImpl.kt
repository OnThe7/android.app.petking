package io.lowapple.app.android.petking.data

import android.app.Activity
import android.content.Context
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.auth.OAuthProvider
import dagger.hilt.android.qualifiers.ActivityContext
import io.lowapple.app.android.petking.domain.models.UserInfoModel
import io.lowapple.app.android.petking.domain.repositories.GithubRepository
import io.lowapple.app.android.petking.shared.GithubAccessToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GithubRepositoryImpl : GithubRepository {
    override suspend fun setupToken(token: GithubAccessToken) {

    }

//    override suspend fun userSignIn(): Flow<GithubAccessToken> {
//        return withContext(CoroutineScope(Dispatchers.Main).coroutineContext) {
//            val firebaseAuth = FirebaseAuth.getInstance()
//            val provider = OAuthProvider.newBuilder("github.com")
//            provider.scopes = arrayListOf(
//                "read:user", "user:email", "repo"
//            )
//            flow {
//                val pending = firebaseAuth.pendingAuthResult
//                if (pending != null) {
//                    val res = Tasks.await(pending)
//                    emit(res.getGithubToken())
//                } else {
//                    val oauth = firebaseAuth.startActivityForSignInWithProvider(
//                        activityContext, provider.build()
//                    )
//                    val res = Tasks.await(oauth)
//                    emit(res.getGithubToken())
//                }
//            }
//        }
//    }
//
//    @Throws(Exception::class)
//    private fun AuthResult.getGithubToken(): GithubAccessToken {
//        val credential = credential as OAuthCredential
//        val token = credential.accessToken
//        if (token != null) {
//            return GithubAccessToken(token)
//        }
//        throw Exception("Github AccessToken not found")
//    }

    override suspend fun getUserInfo(): UserInfoModel {
        TODO("Not yet implemented")
    }

    override suspend fun getUserInfoFromDatabase(): UserInfoModel {
        TODO("Not yet implemented")
    }

    override suspend fun getUserInfoFromNetwork(): UserInfoModel {
        TODO("Not yet implemented")
    }
}