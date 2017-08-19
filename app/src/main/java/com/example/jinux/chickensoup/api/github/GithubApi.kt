package com.example.jinux.chickensoup.api.github

import com.example.jinux.chickensoup.data.GithubBranchHeadResult
import com.example.jinux.chickensoup.data.GithubCommitInfo
import com.example.jinux.chickensoup.utils.logD
import com.google.gson.Gson
import com.ohmerhe.kolley.request.Http

/**
 * Created by Jinux on 2017/7/5.
 */

fun getGithubUpdateRevision(callback: (String, String) -> Unit) {
    getBranchHead("master") {
        getCommitInfo(it) {
            callback(it.parents[0].sha, it.sha)
        }
    }
}

fun getBranchHead(branch: String, callback: (String) -> Unit): Unit {
    Http.get {
        url = "https://api.github.com/repos/jinguoliang/Chicken-Soup/git/refs/heads/$branch"
        onSuccess {
            val jsonStr = String(it)
            val json = Gson().fromJson<GithubBranchHeadResult>(jsonStr, GithubBranchHeadResult::class.java)
            logD("result = " + json.toString())
            callback(json.`object`.sha)
        }

        onFail {
            logD("get result = $it")
        }
    }
}

fun getCommitInfo(commitId: String, callback: (GithubCommitInfo) -> Unit) {
    Http.get {
        url = "https://api.github.com/repos/jinguoliang/Chicken-Soup/git/commits/$commitId"
        onSuccess {
            val jsonStr = String(it)
            val json = Gson().fromJson<GithubCommitInfo>(jsonStr, GithubCommitInfo::class.java)
            logD("result = " + json.toString())
            callback(json)
        }

        onFail {
            logD("get result = $it")
        }
    }
}