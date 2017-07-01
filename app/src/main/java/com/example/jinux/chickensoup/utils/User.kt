package com.example.jinux.chickensoup.utils

import android.content.Context
import android.telephony.TelephonyManager


/**
 * Created by jinux on 17-6-30.
 *
 * helper for abstracting the method of generating user id
 *
 * todo change the implement
 */
fun generateUserId(context: Context): String {
    val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    val id = tm.deviceId
    context.logD("deviceId: $id")
    return MD5.getMD5(id)
}