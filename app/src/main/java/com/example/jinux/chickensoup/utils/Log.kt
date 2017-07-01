package com.example.jinux.chickensoup.utils

import android.util.Log

/**
 * Created by jinux on 17-6-30.
 *
 * log, just for convenience
 */

fun Any.logD(msg: String): Unit {
    val tag = this::class.simpleName
    Log.d(tag, msg)
}