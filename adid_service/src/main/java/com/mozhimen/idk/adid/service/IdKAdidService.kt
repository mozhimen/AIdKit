package com.mozhimen.idk.adid.service

import android.content.Context
import com.mozhimen.idk.adid.service.helpers.AdvertisingConnection
import com.mozhimen.idk.adid.service.helpers.AdvertisingInterface
import com.mozhimen.kotlin.utilk.android.content.UtilKIntent
import com.mozhimen.kotlin.utilk.android.content.UtilKPackageInfo
import com.mozhimen.kotlin.utilk.android.os.UtilKLooperWrapper


/**
 * @ClassName IdKAdidService
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/2/17
 * @Version 1.0
 */
object IdKAdidService {
    /**
     * 这个方法是耗时的，不能在主线程调用
     *
     * private void initAdid() {
     *         Executors.newSingleThreadExecutor().execute(new Runnable() {
     *             @Override
     *             public void run() {
     *                 try {
     *                     String adid = AdvertisingIdClient.getGoogleAdId(getApplicationContext());
     *                     Log.e("xxx","adid:"+adid);
     *                 } catch (Exception e) {
     *                     e.printStackTrace();
     *                 }
     *             }
     *         });
     *     }
     */
    @JvmStatic
    @Throws(Exception::class)
    fun get(context: Context): String? {
        if (UtilKLooperWrapper.isLooperMain()) {
            return ""
        }
        UtilKPackageInfo.get(context, "com.android.vending", 0)
        val advertisingConnection = AdvertisingConnection()
        val intent = UtilKIntent.get("com.google.android.gms.ads.identifier.service.START")
        intent.setPackage("com.google.android.gms")
        if (context.bindService(intent, advertisingConnection, Context.BIND_AUTO_CREATE)) {
            try {
                return AdvertisingInterface(advertisingConnection.getBinder()).getId()
            } finally {
                context.unbindService(advertisingConnection)
            }
        }
        return ""
    }
}