package com.example.jinux.chickensoup

import android.app.Application
import com.ohmerhe.kolley.request.Http

/**
 * Created by Jinux on 2017/7/16.
 */
class App : Application() {
    companion object {
        lateinit var sInstance: App
    }

    override fun onCreate() {
        super.onCreate()
        sInstance = this

        Http.init(this)
    }
}