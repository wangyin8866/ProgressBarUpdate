package com.wyman.myapplication.file

import android.os.Bundle
import android.os.Environment
import android.support.annotation.Nullable
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import com.wyman.myapplication.MyConstants
import com.wyman.myapplication.R
import com.wyman.myapplication.UserManager
import java.io.File
import java.io.FileInputStream
import java.io.ObjectInputStream

/**
 * @author wyman
 * @date  2018/6/6
 * description :
 */
class SecondActivity : AppCompatActivity() {
    lateinit var btn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.binder_test)

        btn = findViewById(R.id.btn)
@Nullable
        btn.text = "跳转到third"
        Log.e("sUserId", UserManager.sUserId.toString())

    }

    override fun onResume() {
        super.onResume()
        recoverFromFile()
    }

    private fun recoverFromFile() {
        Thread({
            run {
                val cachedFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), MyConstants.CACHE_FILE_PATH)
                if (cachedFile.exists()) {
                    val objectInputStream = ObjectInputStream(FileInputStream(cachedFile))
                    val book = objectInputStream.readObject()
                    Log.e("wyman2", "recover user:" + book.toString())
                }
            }
        }).start()
    }
}