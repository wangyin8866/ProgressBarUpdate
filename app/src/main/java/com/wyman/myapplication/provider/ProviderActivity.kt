package com.wyman.myapplication.provider

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import com.wyman.myapplication.R

/**
 * @author wyman
 * @date  2018/6/8
 * description :
 */
class ProviderActivity :AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a)
        val uri=Uri.parse("content://com.wyman.myapplication.provider.BookProvider")
        contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null)

    }
}