package com.example.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await

data class DeviceLocation(
    val latitude: Double,
    val longitude: Double,
    val distanceKm: Double,
    val isWithinRadius: Boolean
)

object LocationHelper {

    // Default Balaji Mart Store GPS Coordinates
    const val DEFAULT_STORE_LAT = 28.6139
    const val DEFAULT_STORE_LNG = 77.2090
    const val DELIVERY_RADIUS_KM = 2.0

    fun hasLocationPermission(context: Context): Boolean {
        val fineLocation = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarseLocation = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return fineLocation || coarseLocation
    }

    suspend fun getCurrentLocation(
        context: Context,
        storeLat: Double = DEFAULT_STORE_LAT,
        storeLng: Double = DEFAULT_STORE_LNG,
        radiusKm: Double = DELIVERY_RADIUS_KM
    ): DeviceLocation? {
        if (!hasLocationPermission(context)) return null

        return try {
            val fusedClient = LocationServices.getFusedLocationProviderClient(context)
            val location = fusedClient.lastLocation.await()
            if (location != null) {
                val distanceMeters = calculateDistanceMeters(storeLat, storeLng, location.latitude, location.longitude)
                val distanceKm = Math.round((distanceMeters / 1000.0) * 100.0) / 100.0
                DeviceLocation(
                    latitude = location.latitude,
                    longitude = location.longitude,
                    distanceKm = distanceKm,
                    isWithinRadius = distanceKm <= radiusKm
                )
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun calculateDistanceMeters(
        startLat: Double,
        startLng: Double,
        endLat: Double,
        endLng: Double
    ): Float {
        val results = FloatArray(1)
        Location.distanceBetween(startLat, startLng, endLat, endLng, results)
        return results[0]
    }

    fun calculateDistanceKm(
        startLat: Double,
        startLng: Double,
        endLat: Double,
        endLng: Double
    ): Double {
        val meters = calculateDistanceMeters(startLat, startLng, endLat, endLng)
        return Math.round((meters / 1000.0) * 100.0) / 100.0
    }

    fun openGoogleMapsNavigation(
        context: Context,
        destLat: Double,
        destLng: Double,
        destLabel: String = "Customer Location"
    ) {
        try {
            // Try direct turn-by-turn navigation via Google Maps app
            val navUri = Uri.parse("google.navigation:q=$destLat,$destLng")
            val mapIntent = Intent(Intent.ACTION_VIEW, navUri).apply {
                setPackage("com.google.android.apps.maps")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            if (mapIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(mapIntent)
            } else {
                // Fallback to universal web maps direction
                val webUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=$destLat,$destLng")
                val fallbackIntent = Intent(Intent.ACTION_VIEW, webUri).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(fallbackIntent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Generic geo intent
            val geoUri = Uri.parse("geo:$destLat,$destLng?q=$destLat,$destLng($destLabel)")
            val geoIntent = Intent(Intent.ACTION_VIEW, geoUri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(geoIntent)
        }
    }

    fun openPhoneDialer(context: Context, phoneNumber: String) {
        try {
            val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phoneNumber")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(dialIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun buildUpiPaymentIntent(
        merchantVpa: String = "balajimart@upi",
        merchantName: String = "Balaji Mart",
        amount: Double,
        orderId: String
    ): Intent {
        val formattedAmount = String.format("%.2f", amount)
        val uri = Uri.parse(
            "upi://pay"
        ).buildUpon()
            .appendQueryParameter("pa", merchantVpa)
            .appendQueryParameter("pn", merchantName)
            .appendQueryParameter("mc", "5411") // Grocery Stores MCC
            .appendQueryParameter("tid", orderId)
            .appendQueryParameter("tr", orderId)
            .appendQueryParameter("tn", "Balaji Mart Order $orderId")
            .appendQueryParameter("am", formattedAmount)
            .appendQueryParameter("cu", "INR")
            .build()

        return Intent(Intent.ACTION_VIEW, uri)
    }
}
