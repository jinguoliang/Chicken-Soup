package com.example.jinux.chickensoup.main

import com.example.jinux.chickensoup.database.HttpDataBase
import org.jetbrains.anko.ctx

class MainPresenter(val mContext: MainActivity) {

    private val  ACTION_PUSH_UP = "pop-up"

    private val mDatabase = HttpDataBase(mContext)
    private var mBaseScore: Int = 0
    private var mNewScore: Int = 0
    private var mSumScore: Int = 0
    private var mAction: String = ACTION_PUSH_UP

    fun onBaseEditChanged(baseScore: Int) {
        mBaseScore = baseScore
        updateSumScore()
    }

    private fun updateSumScore() {
        mSumScore = mBaseScore + mNewScore
        mView.updateSumScore(mSumScore)
    }

    fun changeAction(action: String): Unit {
        mAction = action
        pullTodaySum()
        pullTodayRecords()
    }

    fun onNewScoreEditChanged(newScore: Int) {
        mNewScore = newScore
        updateSumScore()
    }

    fun onShareClick() {
        mDatabase.saveTodaySum(mAction, mNewScore) {
            pullTodayRecords()
        }

        if (mSumScore >= 100 || mNewScore >= 60) {
            mView.showChicken(com.example.jinux.chickensoup.data.CHICKEN[((Math.random() * com.example.jinux.chickensoup.data.CHICKEN.size).toInt())])
        } else {
            mView.hideChicken()
        }

        val msg = "$mAction: $mSumScore = $mBaseScore + $mNewScore"
        val intent = android.content.Intent(android.content.Intent.ACTION_SEND)
        intent.putExtra(android.content.Intent.EXTRA_TEXT, msg)
        intent.type = "text/plain"
        mContext.ctx.startActivity(intent)
    }

    lateinit private var mView: MainActivityUI

    fun attachView(view: MainActivityUI) {
        mView = view
        mView.setPresenter(this)

        pullTodaySum()
        pullTodayRecords()
    }

    private fun pullTodayRecords() {
        mDatabase.getTodayRecords(mAction) {
            mView.updateRecords(it.map { RecordItem(it.user_id, it.category, it.amount, it.created_time) })
        }
    }

    private fun pullTodaySum() {
        mDatabase.getTodaySum(mAction) {
            mBaseScore = it
            mView.setBaseScore(mBaseScore)
        }
    }

}

data class RecordItem(val who: String, val action: String, val amount: Int, val time: String)