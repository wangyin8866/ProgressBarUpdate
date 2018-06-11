package com.wyman.myapplication.messenger

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import com.wyman.myapplication.R
import com.wyman.myapplication.UserManager

/**
 * @author wyman
 * @date  2018/6/6
 * description :
 */
class ThirdActivity :AppCompatActivity() {
    companion object {
        val TAG="ThirdActivity"
    }

    private lateinit var mService: Messenger

    private val  mConnection=object :ServiceConnection{
        override fun onServiceDisconnected(name: ComponentName?) {
                Log.e(TAG,name.toString())
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mService= Messenger(service)
            val msg = Message.obtain(null, 1)
            val data=Bundle()
            data.putString("msg","hello this is client")
            msg.data=data

            msg.replyTo=mGetReplyMessenger

            mService.send(msg)

        }

    }
    private val mGetReplyMessenger = Messenger(MessengerHandler())

    class MessengerHandler :Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg?.what) {
                2 -> Log.e(TAG, "receive msg from service:" + msg.data.getString("reply"))
            }
        }
    }

    lateinit var btn:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.binder_test)

        btn = findViewById(R.id.btn)

        btn.text="跳转到Home"
        Log.e("sUserId", UserManager.sUserId.toString())
        btn.setOnClickListener {
        }


        bindService(Intent(ThirdActivity@ this, MessengerService::class.java), mConnection, Context.BIND_AUTO_CREATE)

    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(mConnection)
    }
}