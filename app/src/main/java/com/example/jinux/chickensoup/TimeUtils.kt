package com.example.jinux.chickensoup

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by jinux on 17-7-1.
 */

fun formatToday(): String {
    val currentTime = Date()
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    val dateString = formatter.format(currentTime)
    return dateString
}