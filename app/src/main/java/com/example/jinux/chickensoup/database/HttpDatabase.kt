package com.example.jinux.chickensoup.database

import android.content.Context
import com.example.jinux.chickensoup.R
import com.example.jinux.chickensoup.data.NewScore
import com.example.jinux.chickensoup.data.TodaySum
import com.example.jinux.chickensoup.utils.formatToday
import com.example.jinux.chickensoup.utils.generateUserId
import com.example.jinux.chickensoup.utils.logD
import com.google.gson.Gson
import com.ohmerhe.kolley.request.Http

/**
 * Created by jinux on 17-7-1.
 *
 * it is a database with web server as backend
 */
class HttpDataBase(val context: Context) {
    init {
        Http.init(context)
    }

    val HOST = "118.89.39.72"
    val POST_AMOUNT_OF_USER = "/soup/api/categories/%s/users/%s/amounts/"
    val GET_AMOUNT_AT_SOMEDAY = "/soup/api/categories/%s/amounts/%s/"

    fun saveTodaySum(newScore: Int) {
        val urlStr = String.format("http://" + HOST + POST_AMOUNT_OF_USER, "pop-up", generateUserId(context))
        logD("saveTodaySum: " + urlStr)
        Http.post {
            url = urlStr
            raw = Gson().toJson(NewScore(newScore))
            onSuccess {
                logD("post result = ${String(it)}")
            }
            onFail {
                logD("post result = $it")
            }
        }
    }

    fun getTodaySum(callBack: (sum: Int) -> Unit): Unit {
        val urlStr = String.format("http://" + HOST + GET_AMOUNT_AT_SOMEDAY, "pop-up", formatToday(context.getString(R.string.data_fmt)))
        logD("getTodaySum: " + urlStr)
        Http.get {
            url = urlStr
            onSuccess {
                val jsonStr = String(it)
                val json = Gson().fromJson<TodaySum>(jsonStr, TodaySum::class.java)
                logD("get result = " + it.joinToString(separator = ""))
                callBack(json.data)
            }

            onFail {
                logD("post result = $it")
            }
        }
    }
}