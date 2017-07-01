package com.example.jinux.chickensoup

import android.content.Context
import com.google.gson.Gson
import com.ohmerhe.kolley.request.Http

/**
 * Created by jinux on 17-7-1.
 */
class HttpDataBase(val context: Context) {
    init {
        Http.init(context)
    }

    val HOST = "118.89.39.72"
    val POST_AMOUNT_OF_USER = "/soup/api/categories/%s/users/%s/amounts/"
    val GET_AMOUNT_AT_SOMEDAY = "/soup/api/categories/%s/amounts/%s/"

    fun saveTodaySum(sum: Int) {
        val urlStr = String.format("http://" + HOST + POST_AMOUNT_OF_USER, "pop-up", generateUserId(context))
        logD("JIN", "saveTodaySum: " + urlStr)
        Http.post {
            url = urlStr
            raw = "{\"amount\": $sum}"
            onSuccess {
                logD("JIN", "post result = $it")
            }
            onFail {
                logD("JIN", "post result = $it")
            }
        }
    }

    fun getTodaySum(callBack: (sum: Int) -> Unit): Int {
        val urlStr = String.format("http://" + HOST + GET_AMOUNT_AT_SOMEDAY, "pop-up", formatToday())
        logD("JIN", "getTodaySum: " + urlStr)
        Http.get {
            url = urlStr
            onSuccess {
                val jsonStr = String(it)
                val json = Gson().fromJson<TodaySum>(jsonStr, TodaySum::class.java)
                logD("JIN", "get result = " + it.joinToString(separator = ""))
                callBack(json.data)
            }

            onFail {
                logD("JIN", "post result = $it")
            }
        }
        return 0
    }
}