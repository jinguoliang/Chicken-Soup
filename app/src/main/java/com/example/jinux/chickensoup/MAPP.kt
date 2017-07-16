package com.example.jinux.chickensoup

import android.app.Application

/**
 * Created by Jinux on 2017/7/16.
 */
class MAPP : Application() {
    companion object {
        lateinit var sInstance: MAPP
    }

    override fun onCreate() {
        super.onCreate()
        sInstance = this
    }
}