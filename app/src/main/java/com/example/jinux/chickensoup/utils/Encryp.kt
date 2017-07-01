package com.example.jinux.chickensoup.utils

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


/**
 * Created by jinux on 17-6-30.
 *
 * help to md5 a string
 */

object MD5 {

    @Throws(NoSuchAlgorithmException::class)
    fun getMD5(v: String): String {
        val md5 = MessageDigest.getInstance("SHA1")
        md5.update(v.toByteArray())
        val m = md5.digest()//加密
        return getString(m)
    }

    private fun getString(array: ByteArray): String {
        val sb = StringBuilder()
        array.map { 0xff and it.toInt() }
                .map { Integer.toHexString(it) }
                .forEach {
                    if (it.length == 1) {
                        sb.append("0$it")
                    } else {
                        sb.append(it)
                    }
                }
        return sb.toString()
    }
}