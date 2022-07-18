package io.lowapple.app.android.petking.ui.components.map

import android.os.Bundle
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import net.daum.android.map.MapView

@Composable
fun KakaoMapView(

) {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context)
    }

    LaunchedEffect(Unit) {

    }
}