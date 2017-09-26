package com.example.jinux.chickensoup.utils

import android.util.Log
import android.widget.Toast
import com.example.jinux.chickensoup.App

/**
 * Created by jinux on 17-6-30.
 *
 * log, just for convenience
 */

fun Any.logD(msg: String) {
    val tag = this::class.simpleName
    Log.d(tag, msg)
}

fun Any.toast(msg: String) {
    logD(msg)

    UIExecutor.post {
        Toast.makeText(App.sInstance, msg, Toast.LENGTH_LONG).show()
    }
}

