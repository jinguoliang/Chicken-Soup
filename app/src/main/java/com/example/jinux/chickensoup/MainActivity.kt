package com.example.jinux.chickensoup

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.sdk25.coroutines.onItemSelectedListener

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ui = MainActivityUI()
        ui.setContentView(this)

        MainPresenter(this).attachView(ui)

        logD("JIN","md5 = " + generateUserId(this))
    }
}

class MainActivityUI : AnkoComponent<MainActivity> {

    lateinit var mMainPresenter: MainPresenter

    lateinit private var mSumScoreTv: TextView
    lateinit private var mSoupTv: TextView

    lateinit private var mActionSpin: Spinner

    lateinit private var mBaseScoreEt: EditText

    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
        relativeLayout {
            padding = dip(10)
            gravity = Gravity.CENTER_HORIZONTAL

            val scoreLine = linearLayout {
                id = android.R.id.text1
                mActionSpin = spinner {
                    adapter = ArrayAdapter<String>(ui.ctx, android.R.layout.simple_list_item_1,
                            resources.getStringArray(R.array.action))
                    onItemSelectedListener {
                        toast("Hello" + selectedItem)
                    }
                }
                mSumScoreTv = textView {
                    hint = "总成绩"
                    text = "0"
                    gravity = Gravity.CENTER
                }
                textView {
                    text = " = "
                }
                mBaseScoreEt = editText {
                    hint = "基数"
                    setText("0")
                    gravity = Gravity.CENTER
                    inputType = InputType.TYPE_CLASS_NUMBER
                    setSelectAllOnFocus(true)
                    addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(s: Editable?) {
                            if (s == null || s.isEmpty()) {
                                return
                            }

                            mMainPresenter.onBaseEditChanged(s.toString().toInt())
                        }

                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        }

                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        }

                    })
                }
                textView {
                    text = " + "
                }
                editText {
                    hint = "新成绩"
                    gravity = Gravity.CENTER
                    setText(context.getString(R.string.edit_default_value))
                    setSelectAllOnFocus(true)
                    inputType = InputType.TYPE_CLASS_NUMBER
                    addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(s: Editable?) {
                            if (s == null || s.isEmpty()) {
                                return
                            }

                            mMainPresenter.onNewScoreEditChanged(s.toString().toInt())
                        }

                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        }

                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        }

                    })
                }
            }.lparams {
                centerInParent()
            }

            button {
                text = "分享"
                onClick {
                    mMainPresenter.onShareClick()
                }
            }.lparams {
                below(scoreLine)
                topMargin = dip(30)
                centerHorizontally()
            }

            mSoupTv = textView {
                text = "It's well"
                textSize = 25f
                visibility = View.INVISIBLE
            }.lparams {
                above(scoreLine)
                bottomMargin = dip(30)
                centerHorizontally()
            }
        }
    }.view()

    fun updateSumScore(sum: Int) {
        mSumScoreTv.text = sum.toString()
    }

    fun setPresenter(mainPresenter: MainPresenter) {
        mMainPresenter = mainPresenter
    }

    fun getAction(): String {
        return mActionSpin.selectedItem.toString()
    }

    fun setBaseScore(base: Int) {
        mBaseScoreEt.setText(base.toString())
    }

    fun showChicken(s: String) {
        mSoupTv.text = s
        mSoupTv.visibility = View.VISIBLE
    }

    fun hideChicken() {
        mSoupTv.visibility = View.INVISIBLE
    }
}

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
            mView.showChicken(CHICKEN[((Math.random() * CHICKEN.size).toInt())])
        } else {
            mView.hideChicken();
        }

        val action = mView.getAction()

        val msg = "$action: $mSumScore = $mBaseScore + $mNewScore"
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, msg)
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
    }

}
