package com.example.jinux.chickensoup.main

import com.example.jinux.chickensoup.network.getCommitId
import com.ohmerhe.kolley.request.Http
import org.jetbrains.anko.*

class MainActivity : android.support.v7.app.AppCompatActivity() {

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        Http.init(this)

        val ui = MainActivityUI()
        ui.setContentView(this)

        MainPresenter(this).attachView(ui)
    }
}

