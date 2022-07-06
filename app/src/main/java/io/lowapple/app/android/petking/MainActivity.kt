package io.lowapple.app.android.petking

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import io.lowapple.app.android.petking.ui.components.PetKingTabRow
import io.lowapple.app.android.petking.ui.screens.overview.OverviewBody
import io.lowapple.app.android.petking.ui.screens.overview.OverviewScreenListener
import io.lowapple.app.android.petking.ui.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    // PetKing Tracker Service provider
    private lateinit var trackerService: PetKingTrackerService

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            Timber.d("ServiceConnection: onServiceConnected")
            val binder = service as PetKingTrackerService.PetKingTrackerBinder
            trackerService = binder.service
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            Timber.d("ServiceConnection: onServiceDisconnected")
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val screens = PetKingScreen.values().toList()
                var currentScreen by rememberSaveable { mutableStateOf(PetKingScreen.Overview) }
                Scaffold(topBar = {
                    PetKingTabRow(
                        screens = screens,
                        onTabSelected = { screen -> currentScreen = screen },
                        currentScreen = currentScreen
                    )
                }) { innerPadding ->
                    Box(Modifier.padding(innerPadding)) {
                        when (currentScreen) {
                            PetKingScreen.Overview -> {
                                OverviewBody(listener = object : OverviewScreenListener {
                                    override fun onConnectingTracker() {
                                        trackerService.requestUpdates()
                                    }

                                    override fun onDisconnectTracker() {
                                        trackerService.removeUpdates()
                                    }
                                })
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        bindService(
            Intent(applicationContext, PetKingTrackerService::class.java),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onStop() {
        unbindService(serviceConnection)
        super.onStop()
    }
}