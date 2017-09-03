package com.example.jinux.chickensoup.ui.about

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.jinux.chickensoup.App
import com.example.jinux.chickensoup.R
import com.example.jinux.chickensoup.utils.getVersionName
import com.example.jinux.chickensoup.utils.inflate
import kotlinx.android.synthetic.main.about.*

/**
 * Created by jingu on 2017/8/6.
 */
class AboutFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.about)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        version_name.text = App.sInstance.getVersionName()
    }
}