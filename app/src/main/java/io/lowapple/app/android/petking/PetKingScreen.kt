package io.lowapple.app.android.petking

import androidx.compose.runtime.Composable
import io.lowapple.app.android.petking.ui.screens.overview.OverviewBody

enum class PetKingScreen(
    val path: String,
    val body: @Composable ((String) -> Unit) -> Unit
) {
    Overview(
        path = "홈!",
        body = { OverviewBody() }
    );

    @Composable
    fun Content(onScreenChange: (String) -> Unit) {
        body(onScreenChange)
    }

    companion object {
        fun fromRoute(route: String?): PetKingScreen =
            when (route?.substringBefore("/")) {
                Overview.name -> Overview
                null -> Overview
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}