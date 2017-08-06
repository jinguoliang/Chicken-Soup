package com.example.jinux.chickensoup.task

import com.example.jinux.chickensoup.BasePresenter
import com.example.jinux.chickensoup.BaseView

/**
 * Created by jingu on 2017/7/30.
 *
 * 要做的活动, MVP 里的 约定接口类
 */
interface TaskContract {
    interface View : BaseView<Presenter> {
        fun setLastScore(score: Int)
        fun setTaskRecords(data: List<RecordItem>)
        fun showLoadingView()
        fun hideLoadingView()
        fun setLoadError()
    }

    interface Presenter : BasePresenter {
        fun changeNick(nickName: String)
        fun commit(newScore: Int)
        fun share()
    }
}
