package com.example.jinux.chickensoup.data

/**
 * Created by jinux on 17-7-1.
 *
 * some bean
 */

data class NewScore(val amount: Int)

data class TodaySumResult(val data: Int)

data class TodayRecord(val category: String,
                       val created_time: String,
                       val amount: Int,
                       val user_id: String)
data class TodayRecordsResult(val data: List<TodayRecord>)