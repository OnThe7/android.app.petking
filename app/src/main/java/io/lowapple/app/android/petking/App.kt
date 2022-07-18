package io.lowapple.app.android.petking

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "22e4701e912ad1220534187a04a2d280")
        Timber.plant(Timber.DebugTree())
    }
}