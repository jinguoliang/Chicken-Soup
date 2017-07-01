package com.example.jinux.chickensoup.main

import org.jetbrains.anko.*

class MainActivity : android.support.v7.app.AppCompatActivity() {

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)

        val ui = MainActivityUI()
        ui.setContentView(this)

        MainPresenter(this).attachView(ui)
    }
}

