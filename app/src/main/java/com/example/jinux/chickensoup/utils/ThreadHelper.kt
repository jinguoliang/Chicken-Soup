package com.example.jinux.chickensoup.utils

import android.os.Handler
import android.os.HandlerThread
import com.example.jinux.chickensoup.App
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by Jinux on 2017/9/25 39 周.
 */

object UIExecutor {
    private var mHandler: Handler

    init {
        mHandler = Handler(App.sInstance.mainLooper)
    }

    fun post(function: () -> Unit) {
        mHandler.post(function)
    }
}

object TaskExecutor {
    private val mHandler: Handler

    init {
        val thread = HandlerThread("tasks executor")
        thread.start()
        mHandler = Handler(thread.looper)
    }

    fun post(function: () -> Unit): Unit {
        mHandler.post(function)
    }
}

object TaskPoolExecutor {
    private val mExecutor: ExecutorService = Executors.newFixedThreadPool(8)

    fun post(function: () -> Unit) {
        mExecutor.execute(function)
    }
}

