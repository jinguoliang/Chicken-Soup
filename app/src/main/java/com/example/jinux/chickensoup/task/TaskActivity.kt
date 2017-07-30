package com.example.jinux.chickensoup.task

import android.os.Bundle
import com.example.jinux.chickensoup.BaseActivity
import com.example.jinux.chickensoup.database.HttpDataBase
import com.example.jinux.chickensoup.main.MainActivityUI
import com.example.jinux.chickensoup.main.RecordItem
import com.ohmerhe.kolley.request.Http
import org.jetbrains.anko.setContentView

/**
 * Created by jingu on 2017/7/30.
 *
 * Activity 负责整合
 */

class TaskActivity() : BaseActivity() {

    private var ui: MainActivityUI? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Http.init(this)

        ui = MainActivityUI()
        ui?.setContentView(this)

        TaskPresenter(HttpDataBase(this), ui!!, "俯卧撑")
    }

    override fun onStart() {
        super.onStart()
        ui?.mPresenter?.start()
    }
}

