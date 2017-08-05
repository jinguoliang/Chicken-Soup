@file:JvmName("ExtentionsUtils")

package com.example.jinux.chickensoup.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by jinux on 17-8-5.
 */

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}