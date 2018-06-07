package com.wyman.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import com.wyman.myapplication.aidl.Book
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectOutputStream
import kotlin.concurrent.thread

/**
 * @author wyman
 * @date  2018/6/6
 * description :通过文件共享来实现ipc
 */
class FirstActivity : AppCompatActivity() {
    lateinit var btn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.binder_test)
        btn = findViewById(R.id.btn)

        btn.text = "跳转到second"



        UserManager.sUserId = 2
        Log.e("sUserId", UserManager.sUserId.toString())
        btn.setOnClickListener {
            startActivity(Intent(FirstActivity@ this, SecondActivity::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        persistToFile()
    }

    private fun persistToFile() {
        Thread({
            run {
                val book = Book(1, "Android艺术探索")

                val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), MyConstants.CHAPTER_2_PATH)
                if (!dir.exists()) {
                    dir.mkdirs()
                }
                val cachedFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), MyConstants.CACHE_FILE_PATH)
                if (!cachedFile.exists()) {
                    dir.mkdirs()
                }
                val objectOutputStream = ObjectOutputStream(FileOutputStream(cachedFile))
                objectOutputStream.writeObject(book)
                Log.e("wyman", "persist user:" + book.toString())


            }
        }).start()
    }

}