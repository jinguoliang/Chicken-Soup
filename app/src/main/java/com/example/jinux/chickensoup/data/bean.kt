package com.example.jinux.chickensoup.data

/**
 * Created by jinux on 17-7-1.
 *
 * some bean
 */

data class NewScore(val amount: Int)

data class TodaySumResult(val data: Int)

data class User(val username: String?, val uid: String)
data class TodayRecord(val category: String,
                       val created_time: String,
                       val amount: Int,
                       val user_id: String,
                       val user: User)
data class UserResult(val data: User)
data class TodayRecordsResult(val data: List<TodayRecord>)
data class NickName(val username: String)

data class GitObject(val sha: String)
data class GithubCurrentRevicsionResult(val `object`: GitObject)