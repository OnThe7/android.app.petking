package io.lowapple.app.android.petking.ui.screens.signin

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity.getSignInClient
import com.google.android.gms.tasks.Tasks
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import io.lowapple.app.android.petking.ui.components.button.OAuthButton
import io.lowapple.app.android.petking.utils.KakaoTalkAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

private const val TAG: String = "SignIn"
const val SIGN_IN = 0x01

@Composable
fun SignInView() {
    Column(modifier = Modifier.fillMaxWidth()) {
        GoogleSignInView()
        KakaoSignInView()
    }
}

@Composable
fun KakaoSignInView(
) {
    val context = LocalContext.current
    OAuthButton(
        id = io.lowapple.app.android.petking.R.drawable.ic_kakao,
        title = "카카로 로그인",
        titleColor = Color.Black,
        backgroundColor = Color(0xFFFAE000)
    ) {
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { _, error ->
                if (error != null) {
                    if (error is KakaoSdkError && error.isInvalidTokenError()) {
                        //로그인 필요
                        KakaoTalkAuth.loginWithKakaoTalk(context)
                    } else {
                        //기타 에러
                    }
                } else {
                    //토큰 유효성 체크 성공(필요 시 토큰 갱신됨)
                }
            }
        } else {
            //로그인 필요
            KakaoTalkAuth.loginWithKakaoTalk(context)
        }
    }
}

@Composable
fun GoogleSignInView(
    senderReq: ActivityResultLauncher<IntentSenderRequest>? = null
) {
    val context = LocalContext.current
    OAuthButton(
        id = io.lowapple.app.android.petking.R.drawable.ic_google,
        title = "구글 로그인",
        titleColor = Color.White,
        backgroundColor = Color.DarkGray
    ) {
        val googleSignInCli = getSignInClient(context)
        val googleSignInReq: BeginSignInRequest =
            BeginSignInRequest.builder().setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder().setSupported(true)
                    .setServerClientId(context.getString(io.lowapple.app.android.petking.R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false).build()
            ).setAutoSelectEnabled(false).build()
        googleSignInCli.beginSignIn(googleSignInReq).addOnSuccessListener { signResult ->
            senderReq?.launch(
                IntentSenderRequest.Builder(signResult.pendingIntent).build()
            )
        }.addOnFailureListener {
            Timber.tag(TAG).e(it)
        }.addOnCompleteListener {
            CoroutineScope(Dispatchers.IO).launch {
                val res = Tasks.await(it)

                Timber.tag(TAG).i(res.toString())
            }
        }
    }
}

@Preview
@Composable
fun SignInViewPreview() {
    SignInView()
}