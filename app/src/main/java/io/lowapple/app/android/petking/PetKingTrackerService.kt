package io.lowapple.app.android.petking

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import io.lowapple.app.android.petking.domain.models.PetKingLocation
import io.lowapple.app.android.petking.domain.repositories.PetKingRepository
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.pow
import kotlin.math.sqrt

@AndroidEntryPoint
class PetKingTrackerService : Service(), SensorEventListener {
    @Inject
    lateinit var repository: PetKingRepository

    companion object {
        private val TAG = PetKingTrackerService::class.java.simpleName
        private const val SMOOTHING_WINDOW_SIZE = 20.0f
        private const val SMALLEST_DISPLACEMENT_100_METERS = 10F
        private const val INTERVAL_TIME = 10
        private const val FASTEST_INTERVAL_TIME = 5
        private const val NOTIFICATION_CHANNEL_ID = "channel_01"
        private const val NOTIFICATION_ID = 1011
        private const val EXTRA_STARTED_FROM_NOTIFICATION = "started_from_notification"
    }

    inner class PetKingTrackerBinder : Binder() {
        internal val service: PetKingTrackerService
            get() = this@PetKingTrackerService
    }

    private val binder = PetKingTrackerBinder()

    private var currentTrackingId: String? = null

    // Notification Manager
    private val nm by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    // Sensor Manager
    private val sm by lazy {
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    private val sensorStep by lazy {
        sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    }
    private val sensorAcc by lazy {
        sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }
    private var valueStepCounter = 0.0f
    private var valueStepCounterAndroid = 0.0f
    private var valueAcc = arrayOf(0.0f, 0.0f, 0.0f)
    private var valueAccHistories = arrayOf(
        arrayOf(SMOOTHING_WINDOW_SIZE),
        arrayOf(SMOOTHING_WINDOW_SIZE),
        arrayOf(SMOOTHING_WINDOW_SIZE)
    )
    private var valueAccTotals = arrayOf(0.0f, 0.0f, 0.0f)
    private var valueAccAvg = arrayOf(0.0f, 0.0f, 0.0f)
    private var valueMagLast = 0.0f
    private var valueMagAvg = 0.0f
    private var valueMagNet = 0.0f
    private var curReadIndex = 0


    private lateinit var location: Location

    // 서비스 파라미터용 데이터 오브젝트
    @SuppressLint("VisibleForTests")
    private val locationRequest = LocationRequest().apply {
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        smallestDisplacement = SMALLEST_DISPLACEMENT_100_METERS // 100 meters
        interval = TimeUnit.SECONDS.toMillis(INTERVAL_TIME.toLong())
        fastestInterval = TimeUnit.SECONDS.toMillis(FASTEST_INTERVAL_TIME.toLong())
    }

    // FusedLocationProvider
    private val locationClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    // Callback for changes in location.
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(res: LocationResult) {
            super.onLocationResult(res)
            locationUpdateFromNew(res.lastLocation)
        }
    }

    override fun onCreate() {
        super.onCreate()
        // locationUpdateFromLast()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val chan = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT
            )
            nm.createNotificationChannel(chan)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.tag(TAG).i("LocationService started")
        return super.onStartCommand(intent, flags, START_NOT_STICKY)
    }

    override fun onBind(intent: Intent): IBinder {
        Timber.tag(TAG).i("in onBind()")
//        stopForeground(true)
        return binder
    }

    override fun onRebind(intent: Intent?) {
        Timber.tag(TAG).i("in onReBind()")
//        stopForeground(true)
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Timber.tag(TAG).i("Last client unbound from service")
        Timber.tag(TAG).i("Starting foreground service")
//        startForeground(NOTIFICATION_ID, getNotification())
        return true
    }

    @Throws(Exception::class)
    fun currentTrackingId(): String {
        return this.currentTrackingId ?: throw Exception("not called requestUpdates()")
    }

    fun requestUpdates() {
        Timber.tag(TAG).i("Requesting location updates")
        startService(Intent(applicationContext, PetKingTrackerService::class.java))
        startForeground(NOTIFICATION_ID, getNotification())
        currentTrackingId = UUID.randomUUID().toString()
        locationUpdateFromLast()

        try {
            locationClient.requestLocationUpdates(
                locationRequest, locationCallback, Looper.myLooper()
            )
        } catch (e: SecurityException) {
            Timber.tag(TAG).e("Lost location permission. Could not request updates. $e")
        }
    }

    fun removeUpdates() {
        Timber.tag(TAG).i("Removing location updates")
        try {
            locationClient.removeLocationUpdates(locationCallback)
            stopSelf()
        } catch (e: SecurityException) {
            Timber.tag(TAG).e("Lost location permission. Could not request updates. $e")
        }
    }

    // 마지막 위치기준으로 업데이트
    private fun locationUpdateFromLast() {
        try {
            locationClient.lastLocation.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    location = task.result!!
                    Timber.tag(TAG).i("Last location: $location")

                    if (currentTrackingId == null) {
                        throw Exception("not called requestUpdates()")
                    }
                    repository.addLocation(
                        currentTrackingId!!,
                        PetKingLocation(location.latitude, location.longitude)
                    )
                } else {
                    Timber.tag(TAG).w("Failed to get location")
                }
            }
        } catch (e: SecurityException) {
            Timber.tag(TAG).e("Lost location permission.${e}")
        } catch (e: Exception) {
            Timber.tag(TAG).e(e)
        }
    }

    // 새로운 위치기준으로 업데이트
    private fun locationUpdateFromNew(location: Location) {
        Timber.tag(TAG).i("New location: $location")
        this.location = location
        if (currentTrackingId == null) {
            throw Exception("not called requestUpdates()")
        }
        this.repository.addLocation(
            currentTrackingId!!,
            PetKingLocation(location.latitude, location.longitude)
        )

        nm.notify(NOTIFICATION_ID, getNotification())
    }

    /**
     * Returns the [NotificationCompat] used as part of the foreground service.
     */
    private fun getNotification(): Notification {
        val intent = Intent(this, PetKingTrackerService::class.java)
        // Extra to help us figure out if we arrived in onStartCommand via the notification or not.
        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true)
        // The PendingIntent that leads to a call to onStartCommand() in this service.
//        val servicePendingIntent = PendingIntent.getService(
//            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
//        )
//        // The PendingIntent to launch activity.
//        val activityPendingIntent = PendingIntent.getActivity(
//            this, 0, Intent(this, MainActivity::class.java), 0
//        )

        val builder =
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID).setContentTitle("서비스 제목")
                .setContentText("서비스 내용")
//            .addAction(0, getString(R.string.notification_action_launch), activityPendingIntent)
//            .addAction(0, getString(R.string.notification_action_stop), servicePendingIntent)
//            .setContentTitle(getString(R.string.notification_content_title))
//            .setContentText(getString(R.string.notification_content_text))
                .setOngoing(true).setPriority(1) // Notification.PRIORITY_HIGH
                .setSmallIcon(R.mipmap.ic_launcher).setWhen(System.currentTimeMillis())

        // Set the Channel ID for Android O.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(NOTIFICATION_CHANNEL_ID) // Channel ID
        }

        return builder.build()
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_STEP_COUNTER -> {
                valueStepCounter = event.values[0]
            }
            Sensor.TYPE_ACCELEROMETER -> {
                for (i in 0 until 3) {
                    valueAcc[i] = event.values[i]
                }
                valueMagLast = sqrt(
                    valueAcc[0].pow(2) + valueAcc[1].pow(2) + valueAcc[2].pow(2)
                )
                //Source: https://github.com/jonfroehlich/CSE590Sp2018
                for (i in 0 until 3) {
                    valueAccTotals[i] = valueAccTotals[i] - valueAccHistories[i][curReadIndex]
                    valueAccHistories[i][curReadIndex] = valueAcc[i]
                    valueAccTotals[i] = valueAccTotals[i] + valueAccHistories[i][curReadIndex]
                    valueAccAvg[i] = valueAccTotals[i] / SMOOTHING_WINDOW_SIZE
                }
                curReadIndex++
                if (curReadIndex >= SMOOTHING_WINDOW_SIZE) {
                    curReadIndex = 0
                }
                valueMagAvg = sqrt(
                    valueAccAvg[0].pow(2) + valueAcc[1].pow(2) + valueAcc[2].pow(2)
                )
                valueMagNet = valueMagLast - valueMagAvg
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) = Unit
}