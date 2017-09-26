package com.example.jinux.chickensoup.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.util.LruCache
import java.net.URL

/**
 * Created by Jinux on 2017/9/25 39 周.
 *
 * 带缓存的图片下载工具
 */

object BitmapLoader {
    private val cache = LruCache<String, Bitmap>(1024 * 1024 * 50)
    private val loadingList = mutableMapOf<String, (k: String, b: Bitmap?) -> Unit>()

    fun load(k: String, onLoaded: (k: String, b: Bitmap?) -> Unit) {

        if (cache.get(k) != null) {
            onLoaded(k, cache.get(k))
            return
        }

        if (loadingList.containsKey(k)) {
            return
        }
        loadingList.put(k, onLoaded)

        TaskPoolExecutor.post {
            try {
                val url = URL(k)
                val b = BitmapFactory.decodeStream(url.openStream())
                cache.put(k, b)
                loadingList[k]!!(k, cache.get(k))
            } catch (e: Exception) {
                toast(e.toString())
                loadingList[k]!!(k, cache.get(k))
            } finally {
                loadingList.remove(k)
            }
        }
    }
}