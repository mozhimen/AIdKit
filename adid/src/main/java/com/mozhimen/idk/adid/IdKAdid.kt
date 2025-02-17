package com.mozhimen.idk.adid

import android.content.Context
import com.google.android.gms.ads.identifier.AdvertisingIdClient


/**
 * @ClassName IdKAdid
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/2/17
 * @Version 1.0
 */
object IdKAdid {
    @JvmStatic
    fun get(context: Context): AdvertisingIdClient.Info? {
        try {
           return AdvertisingIdClient.getAdvertisingIdInfo(context)
        } /*catch (e: GooglePlayServicesAvailabilityException) {
            // Encountered a recoverable error connecting to Google Play services.
        } catch (e: GooglePlayServicesNotAvailableException) {
            // Google Play services is not available entirely.
        } catch (e: IOException) {
            // Unrecoverable error connecting to Google Play services (e.g.,
            // the old version of the service doesn't support getting AdvertisingId).
        } */ catch (e: Exception) {
            return null
        }
    }
}