package com.example.jinux.chickensoup.ui.task.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * Created by jingu on 2017/8/12.
 */
interface ViewTypeDelegateAdapter {
    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType)
}