package com.example.jinux.chickensoup.database

import android.content.Context
import com.example.jinux.chickensoup.BuildConfig
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
    private val HOST_RELEASE = "118.89.39.72"
    private val HOST_DEBUG = "118.89.39.72:81"
    private val POST_AMOUNT_OF_USER = "/soup/api/categories/%s/users/%s/amounts/"
    private val GET_AMOUNT_AT_SOMEDAY = "/soup/api/categories/%s/amounts/%s/"
    private val GET_LIST_OF_SOMEDAY = "/soup/api/categories/%s/amounts/%s/records/"

    private val host = if (BuildConfig.DEBUG) HOST_DEBUG else HOST_RELEASE

    private fun generateURL(path: String): String {
        return PROTOCAL + host + path
    }

    fun saveTodaySum(newScore: Int) {
        val urlStr = String.format(generateURL(POST_AMOUNT_OF_USER), "pop-up", generateUserId(context))
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
        val urlStr = String.format(generateURL(GET_AMOUNT_AT_SOMEDAY), "pop-up", formatToday(context.getString(R.string.data_fmt)))
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
        val urlStr = String.format(generateURL(GET_LIST_OF_SOMEDAY), "pop-up", formatToday(context.getString(R.string.data_fmt)))
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