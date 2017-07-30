package com.example.jinux.chickensoup.task

import com.example.jinux.chickensoup.database.HttpDataBase
import com.example.jinux.chickensoup.main.RecordItem

/**
 * Created by jingu on 2017/7/30.
 *
 * MVP -- P
 */
class TaskPresenter(val mDatabase: HttpDataBase, val mView: TaskContract.View, val mTaskName: String) : TaskContract.Presenter {
    override fun share() {

    }

    override fun commit(newScore: Int) {
        mDatabase.saveTodaySum(mTaskName, newScore) {
            pullTodayRecords()
        }
    }

    override fun changeNick(nickName: String) {
        mDatabase.changeNick(nickName)
    }

    init {
        mView.mPresenter = this
    }

    override fun start() {
        mView.showLoadingView()
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
        mDatabase.getTodayRecords(mTaskName) {
            mView.setTaskRecords(it.map {
                RecordItem(it.user_id,
                        it.user.username ?: it.user.uid, it.category,
                        it.amount,
                        it.created_time)
            })
        }
    }

    private fun pullTodaySum() {
        mDatabase.getTodaySum(mTaskName) { sum ->
            mView.setLastScore(sum)
        }
    }

}