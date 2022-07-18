package io.lowapple.app.android.petking.ui.screens.mapview

import android.app.Activity
import android.os.Bundle
import android.view.ViewGroup
import io.lowapple.app.android.petking.databinding.ActivityMapviewBinding
import net.daum.mf.map.api.MapView


class MapViewActivity : Activity() {
    private lateinit var binding: ActivityMapviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapviewBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // java code
        val mapView = MapView(this)
        val mapViewContainer = binding.mapView as ViewGroup
        mapViewContainer.addView(mapView)
    }
}