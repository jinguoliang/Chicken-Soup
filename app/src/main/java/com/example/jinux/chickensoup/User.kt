package com.example.jinux.chickensoup

import android.content.Context
import android.hardware.usb.UsbDevice.getDeviceId
import android.content.Context.TELEPHONY_SERVICE
import android.telephony.TelephonyManager



/**
 * Created by jinux on 17-6-30.
 */

fun generateUserId(context: Context): String {
    val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    val id = tm.deviceId
    logD("test", id)
    return MD5.getMD5(id)
}