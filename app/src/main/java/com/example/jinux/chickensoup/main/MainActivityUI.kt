package com.example.jinux.chickensoup.main

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.jinux.chickensoup.R
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.sdk25.coroutines.onItemSelectedListener

class MainActivityUI : AnkoComponent<MainActivity> {

    lateinit var mMainPresenter: MainPresenter

    lateinit private var mSumScoreTv: TextView
    lateinit private var mSoupTv: TextView

    lateinit private var mActionSpin: Spinner

    lateinit private var mBaseScoreEt: TextView

    lateinit private var  mRecords: ListView

    lateinit private var mtoast: (msgRes: Int) -> Unit

    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
        mtoast = {
            longToast(it)
        }

        verticalLayout {
            padding = dip(10)
            gravity = Gravity.CENTER_HORIZONTAL


            mSoupTv = textView {
                textSize = 25f
                visibility = View.INVISIBLE
            }.lparams {
                bottomMargin = dip(30)
            }

            val scoreLine = linearLayout {
                id = android.R.id.text1
                mActionSpin = spinner {
                    adapter = ArrayAdapter<String>(ui.ctx, android.R.layout.simple_list_item_1,
                            resources.getStringArray(com.example.jinux.chickensoup.R.array.action))
                    onItemSelectedListener {
                        onItemSelected { parent, view, p, id ->
                            mMainPresenter.changeAction(adapter.getItem(p) as String)
                        }
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
                mBaseScoreEt = textView {
                    hint = "基数"
                    text = "0"
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
                    setText(context.getString(com.example.jinux.chickensoup.R.string.edit_default_value))
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
            }

            linearLayout {
                button {
                    text = context.getString(R.string.ok)
                    onClick {
                        mMainPresenter.onOkClick()
                    }
                }

                button {
                    text = context.getString(R.string.share)
                    onClick {
                        mMainPresenter.onShareClick()
                    }
                }
            }.lparams {
                topMargin = dip(30)
            }


            // records
            mRecords = listView {
            }.lparams {
                height = matchParent
                margin = dip(20)
            }

        }
    }.view()

    fun updateSumScore(sum: Int) {
        mSumScoreTv.text = sum.toString()
    }

    fun setPresenter(mainPresenter: MainPresenter) {
        mMainPresenter = mainPresenter
    }

    fun setBaseScore(base: Int) {
        mBaseScoreEt.text = base.toString()
    }

    fun showChicken(s: String) {
        mSoupTv.text = s
        mSoupTv.visibility = View.VISIBLE
    }

    fun hideChicken() {
        mSoupTv.visibility = View.INVISIBLE
    }

    fun updateRecords(data: List<RecordItem>) {
        mRecords.adapter = RecordListAdapter(data)
    }

    fun showWarnNewScoreTooSmall() {
        mtoast(R.string.warn_new_score_too_small)
    }
}

class RecordListAdapter(val data: List<RecordItem>) : BaseAdapter() {
    override fun getView(p: Int, v: View?, parent: ViewGroup?): View {
        val item = data[p]

        var view = v
        if (view == null) {
            view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_record_list, parent, false)
        }

        view!!.findViewById<TextView>(R.id.userName).setText(item.who)
        view.findViewById<TextView>(R.id.time).text = "at ${item.time}"
        view.findViewById<TextView>(R.id.action).text = item.action
        view.findViewById<TextView>(R.id.amount).text = ": ${item.amount}"

        return view
    }

    override fun getItem(p: Int): Any {
        return data[p]
    }

    override fun getItemId(p: Int): Long {
        return p.toLong()
    }

    override fun getCount(): Int {
        return data.size ?: 0
    }

}