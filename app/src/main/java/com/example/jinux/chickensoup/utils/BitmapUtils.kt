package com.example.jinux.chickensoup.utils

import android.graphics.Bitmap
import android.graphics.Matrix

/**
 * Created by Jinux on 2017/9/26 39 周.
 */

fun scaleBitmap(b: Bitmap, scale: Float): Bitmap {
    val w = b.width
    val h = b.height

    val matrix = Matrix()
    matrix.postScale(scale, scale)

    val new = Bitmap.createBitmap(b, 0, 0, w, h, matrix, true)
    b.recycle()

    return new
}