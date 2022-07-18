package io.lowapple.app.android.petking.data.source

import io.lowapple.app.android.petking.data.entity.PetKingLocationEntity

interface TrackingDataSource {
    fun trackingAppend(uuid: String, value: PetKingLocationEntity)
    fun getTrackingLocations(uuid: String): List<PetKingLocationEntity>
}