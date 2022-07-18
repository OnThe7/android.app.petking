package io.lowapple.app.android.petking.ui.screens.signin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.identity.Identity.getSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import io.lowapple.app.android.petking.ui.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class SignInActivity : ComponentActivity() {
    companion object {
        var TAG: String = SignInActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val googleSignInCli = getSignInClient(this)
        val googleSignInReq =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
                Timber.tag(TAG).d(it.data.toString())
                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        googleSignInCli.getSignInCredentialFromIntent(it.data)?.googleIdToken?.apply {
                            val credential = GoogleAuthProvider.getCredential(this, null)
                            // Firebase Auth 등록 처리
                            FirebaseAuth
                                .getInstance()
                                .signInWithCredential(credential)
                                .addOnSuccessListener { authResult ->
                                    Timber.tag(TAG).d(authResult.toString())
                                }
                                .addOnFailureListener { authException ->
                                    Timber.tag(TAG).e(authException)
                                }
                        }
                    } catch (e: Exception) {
                        // 모든 에러에 대해서 처리한다.
                    }
                }
            }

        setContent {
            var isSignInButton by remember {
                mutableStateOf(FirebaseAuth.getInstance().currentUser == null)
            }
            isSignInButton = true
            HomeScreen(isSignInButton, googleSignInReq)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun HomeScreen(
        signInButtonEnable: Boolean,
        googleSignInReq: ActivityResultLauncher<IntentSenderRequest>? = null
    ) {
        AppTheme {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(horizontal = 18.dp)
            ) {
                Scaffold(
                    topBar = {
                        Spacer(modifier = Modifier.height(24.dp))
                    },
                    content = { padding ->
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Surface(
                                modifier = Modifier
                                    .wrapContentSize(),
                                shape = RoundedCornerShape(24.dp),
                            ) {
                                Text(
                                    text = "금융자격증\n합격앱",
                                    fontSize = 42.sp,
                                    lineHeight = 48.sp,
                                    color = Color.White,
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .align(Alignment.CenterHorizontally)
                                        .padding(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(42.dp))
//                            Image(
//                                painter = painterResource(id = R.drawable.undraw_studying),
//                                contentDescription = "",
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .wrapContentHeight()
//                                    .align(Alignment.CenterHorizontally)
//                            )
                        }
                    },
                    bottomBar = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentSize(Alignment.Center)
                                .padding(bottom = 48.dp)
                        ) {
                            if (signInButtonEnable) {
                                GoogleSignInView(googleSignInReq)
                                KakaoSignInView()

//                                Surface(modifier = Modifier.padding(bottom = 18.dp)) {
//                                    // 로그인&회원가입
//                                    GoogleSignInView(googleSignInReq)
//                                    KakaoSignInView()
//                                }
                                // 개인정보 취급 동의
                                // PrivacyPolicyView()
                            } else {
                                // CourseLoading()
                            }
                        }
                    }
                )
            }
        }
    }
}