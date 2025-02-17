package com.mozhimen.idk.adid.service.helpers

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import java.util.concurrent.LinkedBlockingQueue


/**
 * @ClassName AdvertisingConnection
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/2/17
 * @Version 1.0
 */
class AdvertisingConnection : ServiceConnection {
    var retrieved: Boolean = false
    private val queue = LinkedBlockingQueue<IBinder>(1)

    ////////////////////////////////////////////////////////////

    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        try {
            queue.put(service)
        } catch (localInterruptedException: InterruptedException) {
        }
    }

    override fun onServiceDisconnected(name: ComponentName) {
    }

    ////////////////////////////////////////////////////////////

    @Throws(InterruptedException::class)
    fun getBinder():IBinder{
        check(!this.retrieved)
        this.retrieved = true
        return queue.take() as IBinder
    }
}
