package io.lowapple.app.android.petking.data

import io.lowapple.app.android.petking.data.entity.PetKingLocationEntity
import io.lowapple.app.android.petking.data.source.TrackingDataSource
import io.lowapple.app.android.petking.domain.models.PetKingLocation
import io.lowapple.app.android.petking.domain.repositories.PetKingRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

internal class PetKingRepositoryImpl @Inject constructor(
    private val petKingTrackingDataSource: TrackingDataSource
) : PetKingRepository {
    companion object {
        private val TAG = PetKingRepositoryImpl::class.java.simpleName
    }

    init {
        Timber.tag(TAG).i("PetKingRepositoryImpl Initialize")
    }

    // 🚧 Flow Builder Pattern 을 이용하여 데이터를 감시하도록 한다.
    override fun getLocations(uuid: String): Flow<List<PetKingLocation>> = flow {
        while (true) {
            val data = petKingTrackingDataSource.getTrackingLocations(uuid)
            emit(data.map { PetKingLocation(it.lat, it.lon) })
            delay(1000L)
        }
    }

    override fun addLocation(uuid: String, value: PetKingLocation) {
        petKingTrackingDataSource.trackingAppend(uuid, PetKingLocationEntity(value.lat, value.lon))
    }
}