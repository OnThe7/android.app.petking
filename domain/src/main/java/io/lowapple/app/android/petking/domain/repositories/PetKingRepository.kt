package io.lowapple.app.android.petking.domain.repositories

import io.lowapple.app.android.petking.domain.models.PetKingLocation
import kotlinx.coroutines.flow.Flow

interface PetKingRepository {
    fun getLocations(uuid: String): Flow<List<PetKingLocation>>
    fun addLocation(uuid: String, value: PetKingLocation)
}