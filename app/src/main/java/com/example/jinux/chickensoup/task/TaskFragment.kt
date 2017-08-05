package com.example.jinux.chickensoup.task

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jinux.chickensoup.R
import com.example.jinux.chickensoup.utils.inflate
import kotlinx.android.synthetic.main.fragment_act.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.toast

class TaskFragment : Fragment(), TaskContract.View {

    override lateinit var mPresenter: TaskContract.Presenter

    private val mNickName by lazy {
        nick_name
    }

    private val mLoadingView by lazy {
        loading
    }

    private val mContentView by lazy {
        m_content
    }

    private val mRecordBtn by lazy {
        record
    }

    private val mNewScore by lazy {
        new_score
    }

    private fun onNewScoreEditChanged(newScore: Int) {
//        mNewScore = newScore
//        updateSumScore(mNewScore + mBaseScoreEt.text.toString().toInt())
    }

    fun updateSumScore(sum: Int) {
        sum_score.text = sum.toString()
    }

    fun showChicken(s: String) {
//        mSoupTv.text = s
//        mSoupTv.visibility = View.VISIBLE
    }

    fun hideChicken() {
//        mSoupTv.visibility = View.GONE
    }

    fun showWarnNewScoreTooSmall() {
        toast(R.string.warn_new_score_too_small)
    }

    override fun setNickName(name: String) {
        hideLoadingView()
        nick_name.setText(name)
    }

    override fun setLastScore(score: Int) {
        hideLoadingView()
        base_score.text = score.toString()
    }

    override fun setTaskRecords(data: List<RecordItem>) {
        hideLoadingView()
        record_list.adapter = RecordListAdapter(data.reversed())
    }

    override fun showLoadingView() {
        mLoadingView.visibility = View.VISIBLE
        mContentView.visibility = View.INVISIBLE
    }

    override fun hideLoadingView() {
        mLoadingView.visibility = View.INVISIBLE
        mContentView.visibility = View.VISIBLE
    }

    override fun setLoadError() {
        showLoadingView()
        loading.text = "error"
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_act)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        edit_nick.onClick {
            mNickName.isEnabled = !mNickName.isEnabled
            if (!mNickName.isEnabled) {
                mPresenter.changeNick(mNickName.text.toString())
            }
        }

        mRecordBtn.onClick {
            mPresenter.commit(new_score.text.toString().toIntOrNull()?:0)
        }
    }

    override fun onStart() {
        super.onStart()
        mPresenter?.start()
    }

    companion object {

        fun newInstance(): TaskFragment {
            return TaskFragment()
        }
    }
}

