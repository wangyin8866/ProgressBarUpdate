package com.wyman.myapplication.aidl

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import com.wyman.myapplication.R

/**
 * @author wyman
 * @date  2018/6/7
 * description :
 */
class BookManagerActivity : AppCompatActivity() {
    lateinit var bookManagerService: IBookManagerInterface

    lateinit var btn: Button

    companion object {
        const val MESSAGE_NEW_BOOK_ARRIVED: Int = 1

    }

    private val mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg?.what) {
                MESSAGE_NEW_BOOK_ARRIVED -> Log.e("BookManagerActivity", "receive new book :" + msg.obj)

            }
        }

    }
    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            bookManagerService = IBookManagerInterface.Stub.asInterface(service)
            val list = bookManagerService.boobList
            Log.e("BookManagerActivity", "query book list ,list type:" + list.javaClass.canonicalName)


            Log.e("BookManagerActivity", "query book list :" + list.toString())

            val book = Book(3, "android进阶")
            val newList = bookManagerService.addBook(book)
//            val newList = bookManagerService.boobList

            Log.e("BookManager222222", "query book list :" + newList.toString())
            bookManagerService.registerListener(mIOnNewBookArrivedListener)
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }

    }
    private val mIOnNewBookArrivedListener = object : IOnNewBookArrivedListener.Stub() {
        override fun onNewBookArrived(newBook: Book?) {
            mHandler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED, newBook).sendToTarget()

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a)

        btn = findViewById(R.id.btn)


        btn.setOnClickListener {

            Log.e("setOnClickListener", "setOnClickListener")

            bookManagerService.boobList

        }



        bindService(Intent(BookManagerActivity@ this, BookManagerService::class.java), mConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        if (bookManagerService.asBinder().isBinderAlive) {
            Log.e("BookManagerActivity", "unregister listener:$mIOnNewBookArrivedListener")
            bookManagerService.unregisterListener(mIOnNewBookArrivedListener)

        }
        unbindService(mConnection)
        super.onDestroy()

    }


}