package com.example.jinux.chickensoup.task

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.FrameLayout
import android.widget.TextView
import com.example.jinux.chickensoup.R
import com.example.jinux.chickensoup.utils.getUserId

class RecordListAdapter(val data: List<RecordItem>) : BaseAdapter() {

    override fun getView(p: Int, v: View?, parent: ViewGroup?): View {
        val item = data[p]

        var view = v
        if (view == null) {
            val resId = if (getItemViewType(p) == ITEM_SELF) R.layout.item_record_list_self else R.layout.item_record_list
            view = LayoutInflater.from(parent!!.context).inflate(resId, parent, false)
        }

        view!!.findViewById<TextView>(R.id.userName).setText(item.who)
        view.findViewById<TextView>(R.id.time).text = "at ${item.time}"

        val content = TextView(parent!!.context)
        content.setText("我做了 ${item.amount} 个 ${item.action}")
        content.setTextSize(12f)
        val container = view.findViewById<FrameLayout>(R.id.container)
        container.addView(content)

        return view
    }

    private val ITEM_SELF = 1
    private val ITEM_OTHER = 2

    override fun getItemViewType(position: Int): Int {
        val isSelf = data[position].whoId == getUserId()
        return if (isSelf) ITEM_SELF else ITEM_OTHER
    }

    override fun getItem(p: Int): Any {
        return data[p]
    }

    override fun getItemId(p: Int): Long {
        return p.toLong()
    }

    override fun getCount(): Int {
        return data.size
    }

}

data class RecordItem(val whoId: String, val who: String, val action: String, val amount: Int, val time: String)
