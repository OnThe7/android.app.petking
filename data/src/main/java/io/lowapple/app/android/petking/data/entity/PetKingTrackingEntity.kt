package io.lowapple.app.android.petking.data.entity


@kotlinx.serialization.Serializable
data class PetKingTrackingEntity(
    val locations: HashMap<String, ArrayList<PetKingLocationEntity>>
)