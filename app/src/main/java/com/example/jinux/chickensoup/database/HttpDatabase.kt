package com.example.jinux.chickensoup.database

import android.content.Context
import com.example.jinux.chickensoup.R
import com.example.jinux.chickensoup.data.NewScore
import com.example.jinux.chickensoup.data.TodayRecord
import com.example.jinux.chickensoup.data.TodayRecordsResult
import com.example.jinux.chickensoup.data.TodaySumResult
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

    private val PROTOCAL = "http://"
    val HOST = "118.89.39.72"
    val POST_AMOUNT_OF_USER = "/soup/api/categories/%s/users/%s/amounts/"
    val GET_AMOUNT_AT_SOMEDAY = "/soup/api/categories/%s/amounts/%s/"
    val GET_LIST_OF_SOMEDAY = "/soup/api/categories/%s/amounts/%s/records/"

    fun saveTodaySum(newScore: Int) {
        val urlStr = String.format(PROTOCAL + HOST + POST_AMOUNT_OF_USER, "pop-up", generateUserId(context))
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
        val urlStr = String.format(PROTOCAL + HOST + GET_AMOUNT_AT_SOMEDAY, "pop-up", formatToday(context.getString(R.string.data_fmt)))
        logD("getTodaySum: " + urlStr)
        Http.get {
            url = urlStr
            onSuccess {
                val jsonStr = String(it)
                val json = Gson().fromJson<TodaySumResult>(jsonStr, TodaySumResult::class.java)
                callBack(json.data)
            }

            onFail {
                logD("get result = $it")
            }
        }
    }

    fun getTodayRecords(callBack: (sum: List<TodayRecord>) -> Unit): Unit {
        val urlStr = String.format(PROTOCAL + HOST + GET_LIST_OF_SOMEDAY, "pop-up", formatToday(context.getString(R.string.data_fmt)))
        logD("getTodayRecords: " + urlStr)
        Http.get {
            url = urlStr
            onSuccess {
                val jsonStr = String(it)
                val json = Gson().fromJson<TodayRecordsResult>(jsonStr, TodayRecordsResult::class.java)
                callBack(json.data)
            }

            onFail {
                logD("get result = $it")
            }
        }
    }
}