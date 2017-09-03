package com.example.jinux.chickensoup.utils

import com.example.jinux.chickensoup.App

/**
 * Created by jingu on 2017/9/3.
 */

fun App.getVersionName(): String {
    val info = packageManager.getPackageInfo(packageName, 0)
    return info.versionName
}