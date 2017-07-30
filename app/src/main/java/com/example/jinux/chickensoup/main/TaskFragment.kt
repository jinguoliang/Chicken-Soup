package com.example.jinux.chickensoup.main

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.jinux.chickensoup.BuildConfig
import com.example.jinux.chickensoup.R
import com.example.jinux.chickensoup.network.getGithubUpdateRevision
import com.example.jinux.chickensoup.task.TaskActivity
import com.example.jinux.chickensoup.task.TaskContract
import com.example.jinux.chickensoup.utils.logD
import com.example.jinux.chickensoup.utils.updateApp
import com.ohmerhe.kolley.request.Http
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.tintedButton
import org.jetbrains.anko.cardview.v7.cardView
import org.jetbrains.anko.sdk25.coroutines.onClick

class TaskFragment : Fragment(), TaskContract.View {

    override var mPresenter: TaskContract.Presenter? = null

    lateinit private var mSumScoreTv: TextView
    lateinit private var mSoupTv: TextView

    lateinit private var mBaseScoreEt: TextView

    lateinit private var mRecords: ListView

    lateinit private var mtoast: (msgRes: Int) -> Unit

    lateinit private var mNick: EditText

    private var mNickBack: Editable? = null

    private var mContent: LinearLayout? = null

    private var mLoading: TextView? = null

    private var mNewScore: Int = 0

    private fun onNewScoreEditChanged(newScore: Int) {
        mNewScore = newScore
        updateSumScore(mNewScore + mBaseScoreEt.text.toString().toInt())
    }

    fun updateSumScore(sum: Int) {
        mSumScoreTv.text = sum.toString()
    }

    fun showChicken(s: String) {
        mSoupTv.text = s
        mSoupTv.visibility = View.VISIBLE
    }

    fun hideChicken() {
        mSoupTv.visibility = View.GONE
    }

    fun showWarnNewScoreTooSmall() {
        mtoast(R.string.warn_new_score_too_small)
    }

    override fun setNickName(name: String) {
        hideLoadingView()
        mNick.setText(name)
    }

    override fun setLastScore(score: Int) {
        hideLoadingView()
        mBaseScoreEt.text = score.toString()
    }

    override fun setTaskRecords(data: List<RecordItem>) {
        hideLoadingView()
        mRecords.adapter = RecordListAdapter(data.reversed())
    }

    override fun showLoadingView() {
        mLoading?.visibility = View.VISIBLE
        mContent?.visibility = View.INVISIBLE
    }

    override fun hideLoadingView() {
        mLoading?.visibility = View.INVISIBLE
        mContent?.visibility = View.VISIBLE
    }

    override fun setLoadError() {
        showLoadingView()
        mLoading?.text = "error"
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return with(this) {
            mtoast = {
                longToast(it)
            }
            frameLayout {
                mContent = verticalLayout {
                    padding = dip(10)
                    gravity = android.view.Gravity.CENTER_HORIZONTAL

                    linearLayout {
                        backgroundColor = android.graphics.Color.GREEN
                        button {
                            text = context.getString(com.example.jinux.chickensoup.R.string.has_update)
                            visibility = android.view.View.GONE
                            com.example.jinux.chickensoup.network.getGithubUpdateRevision { newRevision, updateRevision ->
                                logD("remote $newRevision")
                                logD("local ${com.example.jinux.chickensoup.BuildConfig.CURRENT_REVISION}")
                                if (!android.text.TextUtils.equals(newRevision, com.example.jinux.chickensoup.BuildConfig.CURRENT_REVISION)) {
                                    visibility = android.view.View.VISIBLE
                                }

                                onClick {
                                    com.example.jinux.chickensoup.utils.updateApp(newRevision)
                                }
                            }
                        }

                        view {
                            backgroundColor = ctx.getColor(com.example.jinux.chickensoup.R.color.colorAccent)
                        }.lparams {
                            weight = 1f
                            height = 0
                        }
                        if (com.example.jinux.chickensoup.BuildConfig.DEBUG) {
                            textView {
                                text = "Debug"
                            }
                        }
                    }

                    cardView {
                        linearLayout {
                            padding = dip(10)
                            mNick = editText {
                                hint = "昵称"
                                isEnabled = false
                                singleLine = true
                                ellipsize = android.text.TextUtils.TruncateAt.END
                            }.lparams {
                                width = dip(100)
                            }
                            button {
                                backgroundResource = com.example.jinux.chickensoup.R.mipmap.icon_edit
                                backgroundTintList = ctx.getColorStateList(com.example.jinux.chickensoup.R.color.button_tint_selector)
                                onClick {
                                    if (mNick.isEnabled) {
                                        mNick.isEnabled = false
                                        if (android.text.TextUtils.isEmpty(mNick.text)) {
                                            mNick.text = mNickBack
                                        } else {
                                            mPresenter?.changeNick(mNick.text.toString())
                                        }
                                    } else {
                                        mNickBack = mNick.text
                                        mNick.isEnabled = true
                                    }
                                }
                            }.lparams {
                                width = ctx.resources.getDimensionPixelSize(com.example.jinux.chickensoup.R.dimen.common_icon_size)
                                height = ctx.resources.getDimensionPixelSize(com.example.jinux.chickensoup.R.dimen.common_icon_size)
                                leftMargin = dip(8)
                            }
                            view {
                            }.lparams {
                                weight = 1f
                                height = 0
                            }

                        }
                    }.lparams {
                        margin = dip(10)
                        bottomMargin = 0
                    }
                    mSoupTv = textView {
                        textSize = 25f
                        visibility = android.view.View.GONE
                    }.lparams {
                        bottomMargin = dip(30)
                    }

                    cardView {
                        frameLayout {
                            val scoreLine = linearLayout {
                                id = android.R.id.text1
                                padding = dip(4)
                                mBaseScoreEt = textView {
                                    text = "0"
                                    gravity = android.view.Gravity.CENTER
                                    inputType = android.text.InputType.TYPE_CLASS_NUMBER
                                    setSelectAllOnFocus(true)
                                    textSize = 80f
                                    addTextChangedListener(object : TextWatcher {
                                        override fun afterTextChanged(s: Editable?) {
                                            if (s == null || s.isEmpty()) {
                                                return
                                            }

//                                    mMainPresenter.onBaseEditChanged(s.toString().toInt())
                                        }

                                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                                        }

                                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                                        }

                                    })
                                }
                                textView {
                                    text = " + "
                                    textSize = 25f
                                }
                                editText {
                                    hint = "新成绩"
                                    gravity = android.view.Gravity.CENTER
                                    setText(context.getString(com.example.jinux.chickensoup.R.string.edit_default_value))
                                    textSize = 25f
                                    setSelectAllOnFocus(true)
                                    inputType = android.text.InputType.TYPE_CLASS_NUMBER
                                    addTextChangedListener(object : TextWatcher {
                                        override fun afterTextChanged(s: Editable?) {
                                            if (s == null || s.isEmpty()) {
                                                return
                                            }

                                            onNewScoreEditChanged(s.toString().toInt())
                                        }

                                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                                        }

                                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                                        }

                                    })
                                }
                                textView {
                                    textSize = 25f
                                    text = " = "
                                }
                                mSumScoreTv = textView {
                                    text = "0"
                                    textSize = 25f
                                    gravity = android.view.Gravity.CENTER
                                }
                            }.lparams {
                                leftMargin = dip(15)
                            }
                            linearLayout {
                                button {
                                    backgroundResource = com.example.jinux.chickensoup.R.mipmap.icon_record
                                    backgroundTintList = ctx.getColorStateList(com.example.jinux.chickensoup.R.color.button_tint_selector)
                                    onClick {
                                        mPresenter?.commit(mNewScore)
                                    }
                                }.lparams {
                                    width = ctx.resources.getDimensionPixelSize(com.example.jinux.chickensoup.R.dimen.common_icon_size)
                                    height = ctx.resources.getDimensionPixelSize(com.example.jinux.chickensoup.R.dimen.common_icon_size)
                                    rightMargin = dip(10)
                                    topMargin = dip(10)
                                }

                                tintedButton {
                                    backgroundResource = com.example.jinux.chickensoup.R.mipmap.icon_share
                                    backgroundTintList = ctx.getColorStateList(com.example.jinux.chickensoup.R.color.button_tint_selector)
                                    onClick {
                                        mPresenter?.share()
                                    }
                                }.lparams {
                                    width = ctx.resources.getDimensionPixelSize(com.example.jinux.chickensoup.R.dimen.common_icon_size)
                                    height = ctx.resources.getDimensionPixelSize(com.example.jinux.chickensoup.R.dimen.common_icon_size)
                                    rightMargin = dip(12)
                                    topMargin = dip(10)
                                }
                            }.lparams {
                                gravity = android.view.Gravity.RIGHT or android.view.Gravity.TOP
                            }
                        }
                    }.lparams {
                        margin = dip(10)
                        width = org.jetbrains.anko.matchParent
                    }

                    // records
                    cardView {
                        mRecords = listView {
                            divider = null
                        }
                        radius = dip(7).toFloat()
                        setContentPadding(0, dip(11), 0, dip(11))
                    }.lparams {
                        height = org.jetbrains.anko.matchParent
                        width = org.jetbrains.anko.matchParent
                        margin = dip(10)
                    }

                }
                // loading
                mLoading = textView {
                    text = "Loading..."
                    visibility = android.view.View.INVISIBLE
                }.lparams {
                    gravity = android.view.Gravity.CENTER
                }
            }

        }.view()
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

