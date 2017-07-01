package com.example.jinux.chickensoup.main

import com.example.jinux.chickensoup.database.HttpDataBase
import com.example.jinux.chickensoup.utils.logD
import org.jetbrains.anko.ctx

class MainPresenter(val mContext: MainActivity) {
    private val mDatabase = HttpDataBase(mContext)
    private var mBaseScore: Int = 0
    private var mNewScore: Int = 0
    private var mSumScore: Int = 0

    fun onBaseEditChanged(baseScore: Int) {
        mBaseScore = baseScore
        updateSumScore()
    }

    private fun updateSumScore() {
        mSumScore = mBaseScore + mNewScore
        mView.updateSumScore(mSumScore)
    }


    fun onNewScoreEditChanged(newScore: Int) {
        mNewScore = newScore
        updateSumScore()
    }

    fun onShareClick() {
        mDatabase.saveTodaySum(mNewScore)

        if (mSumScore >= 100 || mNewScore >= 60) {
            mView.showChicken(com.example.jinux.chickensoup.data.CHICKEN[((Math.random() * com.example.jinux.chickensoup.data.CHICKEN.size).toInt())])
        } else {
            mView.hideChicken()
        }

        val action = mView.getAction()

        val msg = "$action: $mSumScore = $mBaseScore + $mNewScore"
        val intent = android.content.Intent(android.content.Intent.ACTION_SEND)
        intent.putExtra(android.content.Intent.EXTRA_TEXT, msg)
        intent.type = "text/plain"
        mContext.ctx.startActivity(intent)
    }

    lateinit private var mView: MainActivityUI

    fun attachView(view: MainActivityUI) {
        mView = view
        mView.setPresenter(this)

        mDatabase.getTodaySum {
            mBaseScore = it
            mView.setBaseScore(mBaseScore)
        }

        mDatabase.getTodayRecords {
            mView.updateRecords(it)
        }
    }

}