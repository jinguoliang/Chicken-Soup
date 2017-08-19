package com.example.jinux.chickensoup.task

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.example.jinux.chickensoup.R
import com.example.jinux.chickensoup.utils.getUserId
import com.example.jinux.chickensoup.utils.inflate

class RecordListAdapter(val data: List<RecordItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM_SELF = 1
    private val ITEM_OTHER = 2

    private val delegateAdapters= SparseArrayCompat<ViewTypeDelegateAdapter>()

    init {
        delegateAdapters.put(ITEM_SELF, TaskSelfAdapter())
        delegateAdapters.put(ITEM_OTHER, TaskOtherAdapter())
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

open abstract class TaskAdapter : ViewTypeDelegateAdapter {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val v = parent.inflate(getLayoutId())
        return TaskSelfViewHolder(v)
    }

    abstract fun getLayoutId(): Int

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        holder as TaskSelfViewHolder
        holder.bind(item as RecordItem)
    }

    inner class TaskSelfViewHolder(parent: View) : RecyclerView.ViewHolder(parent) {
        val userNameView: TextView = parent.findViewById<TextView>(R.id.userName)
        val timeView: TextView = parent.findViewById<TextView>(R.id.time)
        val container: FrameLayout = parent.findViewById<FrameLayout>(R.id.container)

        fun bind(item: RecordItem) = with(item) {
            userNameView.text = item.who
            timeView.text = item.time

            container.removeAllViews()
            container.addView(TextView(container.context).apply {
                text = "我做了 ${item.amount} 个 ${item.action}"
                textSize = 16F
            })
        }
    }
}

class TaskOtherAdapter: TaskAdapter() {
    override fun getLayoutId(): Int {
        return R.layout.item_record_list
    }

}

class TaskSelfAdapter: TaskAdapter() {
    override fun getLayoutId(): Int {
        return R.layout.item_record_list_self
    }
}

data class RecordItem(val whoId: String, val who: String, val action: String, val amount: Int, val time: String) : ViewType {
    override fun getViewType(): Int {
        return if (whoId == getUserId()) 1 else 2
    }
}
