package com.example.jinux.chickensoup.ui.video

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import com.example.jinux.chickensoup.R
import com.example.jinux.chickensoup.utils.BitmapLoader
import com.example.jinux.chickensoup.utils.inflate
import com.example.jinux.chickensoup.utils.logD
import kotlinx.android.synthetic.main.video_list.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.sdk25.coroutines.onPrepared
import org.json.JSONArray
import java.net.URLDecoder
import java.util.*


/**
 * Created by Jinux on 2017/9/22 38 周.
 */
class VideoFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.video_list)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        video_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val stream = context.assets.open("video_list")

        val videoData = stream.buffered().reader().use {
            JSONArray(it.readText()).let {

                val videoData = mutableListOf<Map<String, String>>()
                for (i in 0 until it.length()) {
                    val info = it.getJSONObject(i)
                    val map = mutableMapOf<String, String>()
                    map.put("video", info.getString("video").replace("\\", "").replace("\"", ""))
                    map.put("cover", "src=(http%3A%2F%2F.*\\.(pn|jp|jpe)g)".toRegex().find
                    (info.getString("ext"))
                            ?.groupValues?.get(1) ?: "")
                    map.put("cover", URLDecoder.decode(map.get("cover")))
                    map.put("title", info.getString("title")
                            .replace("\\\"", "").replace("\\n", "").replace("\\\\", "\\"))
                    map.put("duration", info.getString("duration").replace("\\", "").replace("\"", ""))
                    videoData.add(map)

                    logD("data = " + map)
                }
                videoData
            }
        }
        val buffer = StringBuffer();
        buffer.append("the unicode: \\u9519".replace("\\\\", "\\"))
        logD(buffer.toString())
        Collections.shuffle(videoData)
        logD("video count = ${videoData.size}")
        video_list.adapter = VideoAdapter(videoData)
    }

    companion object {
        fun newInstance(): VideoFragment {
            return VideoFragment()
        }
    }
}

class VideoAdapter(val data: List<Map<String, String>>) : RecyclerView.Adapter<VideoViewHolder>() {
    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VideoViewHolder {
        return VideoViewHolder(parent!!.inflate(R.layout.item_video))
    }

    override fun onBindViewHolder(holder: VideoViewHolder?, position: Int) {
        val item = data[position]
        holder?.bind(item)
    }

}

class VideoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val videoView = view.findViewById<VideoView>(R.id.video_view).apply {
        onPrepared {
            start()
        }
    }

    private val coverView = view.findViewById<ImageView>(R.id.cover_view)

    private lateinit var currentVideoPath: String

    private val playBtn = view.findViewById<TextView>(R.id.play_btn).apply {
        onClick {
            videoView.setVideoPath(currentVideoPath)

            visibility = View.INVISIBLE
            coverView.visibility = View.INVISIBLE
            videoView.visibility = View.VISIBLE
        }
    }

    fun bind(data: Map<String, String>) {
        logD("cover = ${data["cover"]}")

        coverView.tag = data["cover"]!!
        BitmapLoader.load(coverView.tag as String) { k, v ->
            coverView.post {
                if (k == coverView.tag) {
                    coverView.setImageBitmap(v)
                    coverView.alpha = 0.4f
                    coverView.animate().alpha(1f).start()
                }
            }
        }
        coverView.setImageResource(R.drawable.icon)
        videoView.stopPlayback()
        playBtn.visibility = View.VISIBLE
        coverView.visibility = View.VISIBLE

        currentVideoPath = data["video"].toString()
    }

}

