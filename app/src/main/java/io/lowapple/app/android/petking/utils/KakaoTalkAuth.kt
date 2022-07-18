package io.lowapple.app.android.petking.utils

import android.content.Context
import com.kakao.sdk.auth.model.Prompt
import com.kakao.sdk.user.UserApiClient
import timber.log.Timber

object KakaoTalkAuth {
    private var TAG: String = KakaoTalkAuth::class.java.simpleName

    fun loginWithKakaoTalk(context: Context) {
        UserApiClient.instance.loginWithKakaoTalk(context) { kakaoToken, kakaoError ->
            if (kakaoError != null) {
                Timber.tag(TAG).e(kakaoError)
            } else if (kakaoToken != null) {
                Timber.tag(TAG).i(kakaoToken.accessToken)
            }
        }
    }

    fun loginWithBrowser(context: Context) {
        UserApiClient.instance.loginWithKakaoAccount(
            context, prompts = listOf(Prompt.LOGIN)
        ) { kakaoToken, kakaoError ->
            if (kakaoError != null) {
                Timber.tag(TAG).e(kakaoError)
            } else if (kakaoToken != null) {
                Timber.tag(TAG).i(kakaoToken.accessToken)
            }
        }
    }
}