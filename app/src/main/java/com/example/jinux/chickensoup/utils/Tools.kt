package com.example.jinux.chickensoup.utils

import android.content.Intent
import android.net.Uri
import android.os.Environment
import com.example.jinux.chickensoup.BuildConfig
import com.example.jinux.chickensoup.MAPP
import com.example.jinux.chickensoup.data.TodaySumResult
import com.google.gson.Gson
import com.ohmerhe.kolley.request.Http
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

/**
 * Created by jinux on 17-7-16.
 */

fun updateApp(newRevision: String) {
    val url = "https://github.com/jinguoliang/Chicken-Soup/raw/master/release-$newRevision.apk"
    installApk(url)
}


fun installApk(location: String) {
    if (location.startsWith("https://") or location.startsWith("http://")) {
        downloadFile(location) { path ->
            installLocalApk(path)
        }
    } else {
        installLocalApk(location);
    }
}

fun downloadFile(urlString: String, callback: (String) -> Unit) {
    Http.get {
        url = urlString
        onSuccess { it
            val saveFile = File("${Environment.getExternalStorageDirectory().absolutePath}", "moveon_update.apk")
            saveFile.createNewFile()
            val buffedOutput = BufferedOutputStream(FileOutputStream(saveFile))
            buffedOutput.write(it)
            buffedOutput.close()
            callback(saveFile.absolutePath)
        }

        onFail {
            logD("get result = $it")
        }
    }
}

fun installLocalApk(path: String) {
    val intent = Intent(Intent.ACTION_VIEW);
    intent.setDataAndType(Uri.fromFile(File(path)), "application/vnd.android.package-archive");
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    MAPP.sInstance.startActivity(intent);
}

