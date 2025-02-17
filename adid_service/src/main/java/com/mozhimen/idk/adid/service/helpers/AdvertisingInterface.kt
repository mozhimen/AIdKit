package com.mozhimen.idk.adid.service.helpers

import android.os.IBinder
import android.os.IInterface
import android.os.Parcel
import android.os.RemoteException


/**
 * @ClassName AdvertisingInterface
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/2/17
 * @Version 1.0
 */
class AdvertisingInterface(private val binder: IBinder) : IInterface {
    override fun asBinder(): IBinder {
        return binder
    }

    @Throws(RemoteException::class)
    fun getId(): String? {
        val data = Parcel.obtain()
        val reply = Parcel.obtain()
        val id: String?
        try {
            data.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService")
            binder.transact(1, data, reply, 0)
            reply.readException()
            id = reply.readString()
        } finally {
            reply.recycle()
            data.recycle()
        }
        return id
    }

    @Throws(RemoteException::class)
    fun isLimitAdTrackingEnabled(paramBoolean: Boolean): Boolean {
        val data = Parcel.obtain()
        val reply = Parcel.obtain()
        val limitAdTracking: Boolean
        try {
            data.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService")
            data.writeInt(if (paramBoolean) 1 else 0)
            binder.transact(2, data, reply, 0)
            reply.readException()
            limitAdTracking = 0 != reply.readInt()
        } finally {
            reply.recycle()
            data.recycle()
        }
        return limitAdTracking
    }
}
