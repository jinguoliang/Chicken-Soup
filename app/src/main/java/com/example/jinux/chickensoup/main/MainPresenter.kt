package com.example.jinux.chickensoup.main

import android.content.Context
import com.example.jinux.chickensoup.data.CHICKEN
import com.example.jinux.chickensoup.database.HttpDataBase
import org.jetbrains.anko.ctx

class MainPresenter(val mContext: Context) {

    private val ACTION_PUSH_UP = "pop-up"

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


    fun onOkClick() {
        if (mNewScore < 10) {
            mView.showWarnNewScoreTooSmall()
            return
        }
        commitNewScore()
        giveSomeChicken()
    }

    fun onShareClick() {
        onOkClick()

        val msg = "$mAction: $mSumScore = $mBaseScore + $mNewScore"
        val intent = android.content.Intent(android.content.Intent.ACTION_SEND)
        intent.putExtra(android.content.Intent.EXTRA_TEXT, msg)
        intent.type = "text/plain"
        mContext.ctx.startActivity(intent)
    }

    lateinit private var mView: TaskFragment

    fun attachView(view: TaskFragment) {
        mView = view

        pullNickName()
        pullTodaySum()
        pullTodayRecords()
    }

    private fun pullNickName() {
        mDatabase.getNickName {
            mView.setNickName(it)
        }
    }

    private fun pullTodayRecords() {
        mDatabase.getTodayRecords(mAction) {
            mView.setTaskRecords(it.map {
                RecordItem(it.user_id,
                        it.user.username ?: it.user.uid, it.category,
                        it.amount,
                        it.created_time)
            })
        }
    }

    private fun pullTodaySum() {
        mDatabase.getTodaySum(mAction) { sum ->
            mBaseScore = sum
            mView.setLastScore(mBaseScore)
        }
    }

    private fun commitNewScore() {
        mBaseScore = mSumScore
        mView.setLastScore(mBaseScore)
        mDatabase.saveTodaySum(mAction, mNewScore) {
            pullTodayRecords()
        }
    }

    private fun giveSomeChicken() {
        if (mSumScore >= 100 || mNewScore >= 60) {
            mView.showChicken(CHICKEN[((Math.random() * CHICKEN.size).toInt())])
        } else {
            mView.hideChicken()
        }
    }

    fun changeNick(nick: String) {
        mDatabase.changeNick(nick)
    }
}

data class RecordItem(val whoId: String, val who: String, val action: String, val amount: Int, val time: String)