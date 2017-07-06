package com.example.jinux.chickensoup.network

import com.example.jinux.chickensoup.data.GithubCurrentRevicsionResult
import com.example.jinux.chickensoup.data.TodayRecordsResult
import com.example.jinux.chickensoup.utils.logD
import com.google.gson.Gson
import com.ohmerhe.kolley.request.Http
import javax.security.auth.callback.Callback

/**
 * Created by yq on 2017/7/5.
 */

fun getCommitId(branch: String, callback: (String) -> Unit): Unit {
    Http.get {
        url = "https://api.github.com/repos/jinguoliang/Chicken-Soup/git/refs/heads/$branch"
        onSuccess {
            val jsonStr = String(it)
            val json = Gson().fromJson<GithubCurrentRevicsionResult>(jsonStr, GithubCurrentRevicsionResult::class.java)
            logD("result = " + json.toString())
            callback(json.`object`.sha)
        }

        onFail {
            logD("get result = $it")
        }
    }
}