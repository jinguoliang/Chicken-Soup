package com.example.jinux.chickensoup.utils

import android.content.Context
import com.example.jinux.chickensoup.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by jinux on 17-7-1.
 *
 * format time just for convenience
 *
 */

fun formatToday(fmt: String): String {
    val currentTime = Date()
    val formatter = SimpleDateFormat(fmt, Locale.US) // todo
    val dateString = formatter.format(currentTime)
    return dateString
}