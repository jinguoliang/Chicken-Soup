package com.example.jinux.chickensoup.about

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.jinux.chickensoup.R

/**
 * Created by jingu on 2017/8/6.
 */
class AboutFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return TextView(context).apply {
            setText(R.string.about)
        }
    }
}