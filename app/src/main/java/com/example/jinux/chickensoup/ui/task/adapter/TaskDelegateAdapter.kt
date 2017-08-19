package com.example.jinux.chickensoup.ui.task.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.example.jinux.chickensoup.R
import com.example.jinux.chickensoup.ui.vo.RecordItem
import com.example.jinux.chickensoup.utils.inflate

abstract class TaskDelegateAdapter : ViewTypeDelegateAdapter {
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