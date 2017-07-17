package com.example.jinux.chickensoup.main

import android.graphics.Color
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
import com.example.jinux.chickensoup.network.getBranchHead
import com.example.jinux.chickensoup.network.getGithubUpdateRevision
import com.example.jinux.chickensoup.utils.getUserId
import com.example.jinux.chickensoup.utils.logD
import com.example.jinux.chickensoup.utils.updateApp
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.tintedButton
import org.jetbrains.anko.cardview.v7.cardView
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.sdk25.coroutines.onItemSelectedListener

class MainActivityUI : AnkoComponent<MainActivity> {

    lateinit var mMainPresenter: MainPresenter

    lateinit private var mSumScoreTv: TextView
    lateinit private var mSoupTv: TextView

    lateinit private var mActionSpin: Spinner

    lateinit private var mBaseScoreEt: TextView

    lateinit private var mRecords: ListView

    lateinit private var mtoast: (msgRes: Int) -> Unit

    lateinit private var mNick: EditText

    private var mNickBack: Editable? = null

    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
        mtoast = {
            longToast(it)
        }

        verticalLayout {
            padding = dip(10)
            gravity = Gravity.CENTER_HORIZONTAL

            linearLayout {
                backgroundColor = Color.GREEN
                button {
                    text = context.getString(R.string.has_update)
                    visibility = View.GONE
                    getGithubUpdateRevision { newRevision, updateRevision ->
                        logD("remote $newRevision")
                        logD("local ${BuildConfig.CURRENT_REVISION}")
                        if (!TextUtils.equals(newRevision, BuildConfig.CURRENT_REVISION)) {
                            visibility = View.VISIBLE
                        }

                        onClick {
                            updateApp(newRevision)
                        }
                    }
                }

                view {
                    backgroundColor = ctx.getColor(R.color.colorAccent)
                }.lparams {
                    weight = 1f
                    height = 0
                }
                if (BuildConfig.DEBUG) {
                    textView {
                        text = "Debug"
                    }
                }
            }

            cardView {
                mActionSpin = spinner {
                    adapter = ArrayAdapter<String>(ui.ctx, android.R.layout.simple_list_item_1,
                            resources.getStringArray(com.example.jinux.chickensoup.R.array.action))
                    onItemSelectedListener {
                        onItemSelected { parent, view, p, id ->
                            mMainPresenter.changeAction(adapter.getItem(p) as String)
                        }
                    }
                }
                cardElevation = 0f
                cardBackgroundColor = ctx.getColorStateList(R.color.colorPrimary)
                post {
                    radius = (height / 2).toFloat()
                }
            }.lparams {
                width = wrapContent
                topMargin = dip(5)
            }
            cardView {
                linearLayout {
                    padding = dip(10)
                    mNick = editText {
                        hint = "昵称"
                        isEnabled = false
                        singleLine = true
                        ellipsize = TextUtils.TruncateAt.END
                    }.lparams {
                        width = dip(100)
                    }
                    button {
                        backgroundResource = R.mipmap.icon_edit
                        backgroundTintList = ctx.getColorStateList(R.color.button_tint_selector)
                        onClick {
                            if (mNick.isEnabled) {
                                mNick.isEnabled = false
                                if (TextUtils.isEmpty(mNick.text)) {
                                    mNick.text = mNickBack
                                } else {
                                    mMainPresenter.changeNick(mNick.text.toString())
                                }
                            } else {
                                mNickBack = mNick.text
                                mNick.isEnabled = true
                            }
                        }
                    }.lparams {
                        width = ctx.resources.getDimensionPixelSize(R.dimen.common_icon_size)
                        height = ctx.resources.getDimensionPixelSize(R.dimen.common_icon_size)
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
                visibility = View.GONE
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
                            gravity = Gravity.CENTER
                            inputType = InputType.TYPE_CLASS_NUMBER
                            setSelectAllOnFocus(true)
                            textSize = 80f
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
                            textSize = 25f
                        }
                        editText {
                            hint = "新成绩"
                            gravity = Gravity.CENTER
                            setText(context.getString(com.example.jinux.chickensoup.R.string.edit_default_value))
                            textSize = 25f
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
                        textView {
                            textSize = 25f
                            text = " = "
                        }
                        mSumScoreTv = textView {
                            text = "0"
                            textSize = 25f
                            gravity = Gravity.CENTER
                        }
                    }.lparams {
                        leftMargin = dip(15)
                    }
                    linearLayout {
                        button {
                            backgroundResource = R.mipmap.icon_record
                            backgroundTintList = ctx.getColorStateList(R.color.button_tint_selector)
                            onClick {
                                mMainPresenter.onOkClick()
                            }
                        }.lparams {
                            width = ctx.resources.getDimensionPixelSize(R.dimen.common_icon_size)
                            height = ctx.resources.getDimensionPixelSize(R.dimen.common_icon_size)
                            rightMargin = dip(10)
                            topMargin = dip(10)
                        }

                        tintedButton {
                            backgroundResource = R.mipmap.icon_share
                            backgroundTintList = ctx.getColorStateList(R.color.button_tint_selector)
                            onClick {
                                mMainPresenter.onShareClick()
                            }
                        }.lparams {
                            width = ctx.resources.getDimensionPixelSize(R.dimen.common_icon_size)
                            height = ctx.resources.getDimensionPixelSize(R.dimen.common_icon_size)
                            rightMargin = dip(12)
                            topMargin = dip(10)
                        }
                    }.lparams {
                        gravity = Gravity.RIGHT or Gravity.TOP
                    }
                }
            }.lparams {
                margin = dip(10)
                width = matchParent
            }

            // records
            cardView {
                mRecords = listView {
                    divider = null
                }
                radius = dip(7).toFloat()
                setContentPadding(0, dip(11), 0, dip(11))
            }.lparams {
                height = matchParent
                width = matchParent
                margin = dip(10)
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
        mSoupTv.visibility = View.GONE
    }

    fun updateRecords(data: List<RecordItem>) {
        mRecords.adapter = RecordListAdapter(data.reversed())
    }

    fun showWarnNewScoreTooSmall() {
        mtoast(R.string.warn_new_score_too_small)
    }

    fun setNickName(name: String) {
        mNick.setText(name)
    }
}

class RecordListAdapter(val data: List<RecordItem>) : BaseAdapter() {

    override fun getView(p: Int, v: View?, parent: ViewGroup?): View {
        val item = data[p]

        var view = v
        if (view == null) {
            val resId = if (getItemViewType(p) == ITEM_SELF) R.layout.item_record_list_self else R.layout.item_record_list
            view = LayoutInflater.from(parent!!.context).inflate(resId, parent, false)
        }

        view!!.findViewById<TextView>(R.id.userName).setText(item.who)
        view.findViewById<TextView>(R.id.time).text = "at ${item.time}"

        val content = TextView(parent!!.context)
        content.setText("我做了 ${item.amount} 个 ${item.action}")
        content.setTextSize(12f)
        val container = view.findViewById<FrameLayout>(R.id.container)
        container.addView(content)

        return view
    }

    private val ITEM_SELF = 1
    private val ITEM_OTHER = 2

    override fun getItemViewType(position: Int): Int {
        val isSelf = data[position].whoId == getUserId()
        return if (isSelf) ITEM_SELF else ITEM_OTHER
    }

    override fun getItem(p: Int): Any {
        return data[p]
    }

    override fun getItemId(p: Int): Long {
        return p.toLong()
    }

    override fun getCount(): Int {
        return data?.size
    }

}