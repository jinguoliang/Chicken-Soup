package com.example.jinux.chickensoup.ui.task.adapter

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.example.jinux.chickensoup.ui.vo.RecordItem

class RecordListAdapter(val data: List<RecordItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val delegateAdapters= SparseArrayCompat<ViewTypeDelegateAdapter>()

    init {
        delegateAdapters.put(RecordAdapterConstants.ITEM_SELF, TaskSelfDelegateAdapter())
        delegateAdapters.put(RecordAdapterConstants.ITEM_OTHER, TaskOtherDelegateAdapter())
    }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegateAdapters.get(viewType).onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, data[position])
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].getViewType()
    }
}

