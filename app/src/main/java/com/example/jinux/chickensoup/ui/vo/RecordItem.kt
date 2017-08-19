package com.example.jinux.chickensoup.ui.vo

import com.example.jinux.chickensoup.ui.task.adapter.RecordAdapterConstants
import com.example.jinux.chickensoup.ui.task.adapter.ViewType
import com.example.jinux.chickensoup.utils.getUserId

data class RecordItem(val whoId: String, val who: String, val action: String, val amount: Int, val time: String) : ViewType {
    override fun getViewType(): Int {
        return if (whoId == getUserId()) RecordAdapterConstants.ITEM_SELF else RecordAdapterConstants.ITEM_OTHER
    }
}