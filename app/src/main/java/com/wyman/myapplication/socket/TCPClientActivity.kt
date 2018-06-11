package com.wyman.myapplication.socket

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.SystemClock
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.text.format.DateUtils.formatDateTime
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.wyman.myapplication.R
import com.wyman.myapplication.R.layout.a
import java.io.*
import java.net.ServerSocket
import java.net.Socket
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.InflaterInputStream

/**
 * @author wyman
 * @date  2018/6/11
 * description :
 */
class TCPClientActivity : AppCompatActivity() {
    companion object {
        private const val MESSAGE_RECEIVE_NEW_MSG = 1
        private const val MESSAGE_SOCKET_CONNECTED = 2

    }

    private lateinit var tvMessage: TextView
    private lateinit var etMessage: EditText
    private lateinit var btn: Button

    private lateinit var mPrintWriter: PrintWriter
    private lateinit var mClientSocket: Socket


    private val mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg?.what) {
                MESSAGE_RECEIVE_NEW_MSG -> tvMessage.text = tvMessage.text.toString() + msg.obj.toString()

                MESSAGE_SOCKET_CONNECTED -> btn.isEnabled = true
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a)

        init()

        startService(Intent(TCPClientActivity@ this, TCPServerService::class.java))

        Thread {
            run {
                connectTCPServer()
            }
        }.start()

    }

    private fun connectTCPServer() {
        var socket: Socket? = null
        while (socket == null) {
            try {
                socket = Socket("localhost", 8688)
                mClientSocket = socket
                mPrintWriter = PrintWriter(BufferedWriter(OutputStreamWriter(socket.getOutputStream())), true)
                mHandler.sendEmptyMessage(MESSAGE_SOCKET_CONNECTED)
                println("connect server success")
            } catch (e: Exception) {
                println(e.message)
                SystemClock.sleep(1000)
                println("connect tcp server failed,retry...")

            }
        }

        //接受服务器端消息
        val br = BufferedReader(InputStreamReader(socket.getInputStream()))
        println(br.readLine())
        while (!TCPClientActivity@ this.isFinishing) {
            var msg = br.readLine()
            println("receive$msg")
            if (msg != null) {
                val time = formatDateTime(System.currentTimeMillis())

                val showedMsg="server$time:$msg\n"
                mHandler.obtainMessage(MESSAGE_RECEIVE_NEW_MSG, showedMsg).sendToTarget()


            }


        }
        println("quit...")
        mPrintWriter.close()
        br.close()
        socket.close()


    }

    private fun init() {
        tvMessage = findViewById(R.id.tv_message)
        etMessage = findViewById(R.id.et_message)
        btn = findViewById(R.id.btn)


        btn.setOnClickListener {
            val msg = etMessage.text.toString()
            if (!TextUtils.isEmpty(msg)) {
                mPrintWriter.println(msg)

                etMessage.setText("")
                val time = formatDateTime(System.currentTimeMillis())
                val showedMsg = "self$time:$msg\n"
                tvMessage.text = tvMessage.text.toString() + showedMsg
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun formatDateTime(time: Long): String {
        return SimpleDateFormat("(HH:mm:ss)").format(Date(time))
    }

    override fun onDestroy() {
        super.onDestroy()
        mClientSocket.shutdownInput()
        mClientSocket.close()

    }
}


