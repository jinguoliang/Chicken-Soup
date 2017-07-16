package com.example.jinux.chickensoup.utils

import android.content.Context
import android.telephony.TelephonyManager
import com.example.jinux.chickensoup.MAPP


/**
 * Created by jinux on 17-6-30.
 *
 * helper for abstracting the method of generating user id
 *
 * todo change the implement
 */
private var userId: String? = null
fun getUserId(): String {
    if (userId != null) {
        return userId as String
    }

    val context = MAPP.sInstance
    val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    val id = tm.deviceId
    context.logD("deviceId: $id")
    userId = MD5.getMD5(id)

    return userId as String
}