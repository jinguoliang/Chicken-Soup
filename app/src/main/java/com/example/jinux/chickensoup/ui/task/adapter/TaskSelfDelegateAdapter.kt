package com.example.jinux.chickensoup.ui.task.adapter

import com.example.jinux.chickensoup.R

class TaskSelfDelegateAdapter : TaskDelegateAdapter() {
    override fun getLayoutId(): Int {
        return R.layout.item_record_list_self
    }
}