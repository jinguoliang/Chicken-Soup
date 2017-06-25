package com.example.jinux.chickensoup

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.TextView
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ui = MainActivityUI()
        ui.setContentView(this)
    }
}

class MainActivityUI : AnkoComponent<MainActivity> {
    lateinit var sumScore: TextView
    lateinit var base: TextView
    lateinit var newScore: TextView

    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
        relativeLayout {
            padding = dip(10)
            gravity = Gravity.CENTER_HORIZONTAL

            val scoreLine = linearLayout {
                id = android.R.id.text1
                textView {
                    text = resources.getString(R.string.push_up)
                }
                sumScore = textView {
                    hint = "总成绩"
                    text = "0"
                    gravity = Gravity.CENTER
                }
                textView {
                    text = " = "
                }
                base = editText {
                    hint = "基数"
                    gravity = Gravity.CENTER
                    inputType = InputType.TYPE_CLASS_NUMBER
                    setText(context.getString(R.string.edit_default_value))
                    addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(s: Editable?) {
                            if (s == null || s.isEmpty()) return

                            val base = base.text.toString().toInt()
                            val new = newScore.text.toString().toInt()
                            sumScore.text = (base + new).toString()
                        }

                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        }

                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        }

                    })                }
                textView {
                    text = " + "
                }
                newScore = editText {
                    hint = "新成绩"
                    gravity = Gravity.CENTER
                    setText(context.getString(R.string.edit_default_value))
                    inputType = InputType.TYPE_CLASS_NUMBER
                    addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(s: Editable?) {
                            if (s == null || s.isEmpty()) return

                            val base = base.text.toString().toInt()
                            val new = newScore.text.toString().toInt()
                            sumScore.text = (base + new).toString()
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
                    val sum = sumScore.text.toString().toInt()
                    val base = base.text.toString().toInt()
                    val new = newScore.text.toString().toInt()

                    val msg = "俯卧撑: $sum = $base + $new"
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.putExtra(Intent.EXTRA_TEXT, msg)
                    intent.setType("text/plain")
                    ui.ctx.startActivity(intent)
                }
            }.lparams {
                below(scoreLine)
                topMargin = dip(30)
                centerHorizontally()
            }
        }
    }.view()

}
