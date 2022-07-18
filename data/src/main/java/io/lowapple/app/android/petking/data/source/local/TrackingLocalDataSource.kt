package io.lowapple.app.android.petking.data.source.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import io.lowapple.app.android.petking.data.entity.PetKingLocationEntity
import io.lowapple.app.android.petking.data.entity.PetKingTrackingEntity
import io.lowapple.app.android.petking.data.source.TrackingDataSource
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.util.*
import javax.inject.Inject

internal class TrackingLocalDataSource @Inject constructor(
    @ApplicationContext appContext: Context
) : TrackingDataSource {
    companion object {
        private val TAG = TrackingLocalDataSource::class.java.simpleName
        const val KEY_PETKING_TRACKING_ENTITY = "KEY_PETKING_TRACKING_ENTITY"
        private val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }

    private val sharedPreferences: SharedPreferences by lazy {
        appContext.getSharedPreferences("petking", Context.MODE_PRIVATE)
    }

    override fun trackingAppend(uuid: String, value: PetKingLocationEntity) {
        val data = getDataSource()
        if (data.locations[uuid] == null) {
            data.locations[uuid] = arrayListOf()
        }
        data.locations[uuid]!!.add(value)
        data.commit()
    }

    override fun getTrackingLocations(uuid: String): List<PetKingLocationEntity> {
        val source = getDataSource()
        return source.locations[uuid] ?: emptyList()
    }

    @Throws(Exception::class)
    private fun getDataSource(): PetKingTrackingEntity {
        val data = try {
            val row = sharedPreferences.getString(KEY_PETKING_TRACKING_ENTITY, "")!!
            row.toEntity()
        } catch (e: Exception) {
            Timber.tag(TAG).e("trackingAppend error: $e")
            PetKingTrackingEntity(hashMapOf())
        }
        return data
    }

    private fun String.toEntity(): PetKingTrackingEntity {
        return json.decodeFromString(this)
    }

    private fun PetKingTrackingEntity.toJson(): String {
        return json.encodeToString(this)
    }

    private fun PetKingTrackingEntity.commit() {
        sharedPreferences.edit {
            putString(KEY_PETKING_TRACKING_ENTITY, toJson())
            commit()
        }
    }
}