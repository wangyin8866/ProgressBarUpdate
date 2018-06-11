package com.wyman.myapplication.socket

import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.io.*
import java.net.ServerSocket
import java.net.Socket
import java.util.*

/**
 * @author wyman
 * @date  2018/6/8
 * description :
 */
class TCPServerService : Service() {
    val mIsServiceDestoryed: Boolean = false
    private val mDefinedMessages = arrayOf("你好啊，哈哈",
            "请问你叫什么名字",
            "今天北京天气不错啊，shy",
            "你知道吗？我可是可以和多个人同时聊天的",
            "给你讲个笑话吧，据说爱笑的人运气不会太差")

    override fun onCreate() {
        super.onCreate()

        Thread(TcpServer()).start()
    }

    inner class TcpServer : Runnable {
        override fun run() {
            val serverSocket: ServerSocket
            try {
                serverSocket = ServerSocket(8688)
            } catch (e: Exception) {
                println("establish tcp server failed,port:8688")
                return
            }
            while (!mIsServiceDestoryed) {
                //接受客户端的请求

                try {
                    val client: Socket = serverSocket.accept()
                    println("accept")

                    Thread {
                        run {
                            responseClient(client)
                        }
                    }.start()
                } catch (e: Exception) {
                    println(e.message)
                }

            }

        }

    }

    private fun responseClient(client: Socket){
        //用于接受客户端的消息
        val inputStream = BufferedReader(InputStreamReader(client.getInputStream()))
        //用于向客户端发送消息
        val printWriter = PrintWriter(BufferedWriter(OutputStreamWriter(client.getOutputStream())), true)
        printWriter.print("欢迎来到聊天室！")
        println("aaaaaaaaaa")
        while (!mIsServiceDestoryed) {
            val str = inputStream.readLine()
            println("msg from client:$str")
            if (str == null) {
                //客户端断开连接
                break
            }
            val i=Random().nextInt(mDefinedMessages.size)
            val msg = mDefinedMessages[i]
            printWriter.println(msg)
            println("send :$msg")
        }
        println("client quit")
        //关闭 流
        inputStream.close()
        printWriter.close()
        client.close()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}