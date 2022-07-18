package io.lowapple.app.android.petking.ui.screens.overview

import android.content.ComponentName
import android.content.ServiceConnection
import android.location.Location
import android.os.IBinder
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import io.lowapple.app.android.petking.PetKingTrackerService
import io.lowapple.app.android.petking.domain.models.PetKingLocation
import timber.log.Timber

interface OverviewScreenListener {
    fun onConnectingTracker()
    fun onDisconnectTracker()
}

@Composable
fun OverviewBody(
    trackingPoints: List<PetKingLocation> = emptyList(),
    listener: OverviewScreenListener? = null
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .semantics { contentDescription = "Overview Screen" }
    ) {
        Text(text = "저장된 위치개수: ${trackingPoints.size}")

        Button(onClick = {
            listener?.onConnectingTracker()
        }) {
            Text("트래킹 시작")
        }
    }
}