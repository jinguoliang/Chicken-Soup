package com.example.jinux.chickensoup

import android.content.Context
import android.content.SharedPreferences
import java.util.concurrent.TimeUnit

/**
 * Created by jinux on 17-6-27.
 */

class FakeDataBase(val context: Context) {
    val preference: SharedPreferences =
            context.getSharedPreferences("database", Context.MODE_PRIVATE)

    fun saveTodaySum(sum: Int) {
        val key = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis())
        put(key.toString(), sum)
    }

    fun getTodaySum(): Int {
        val key = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis())
        return preference.getInt(key.toString(), 0)
    }

    fun put(key:String, value:Any) {
        val editor = preference.edit()
        when (value) {
            is Int -> editor.putInt(key, value)
            else -> throw RuntimeException("no such type")
        }
        editor.apply()
    }
}