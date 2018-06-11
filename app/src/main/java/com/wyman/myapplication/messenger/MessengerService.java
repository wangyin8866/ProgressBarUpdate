package com.wyman.myapplication.messenger;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * @author wyman
 * @date 2018/6/6
 * description :通过messenger 来实现ipc
 */
public class MessengerService extends Service {



    private static class MessengerHandler extends Handler{
        private static final String TAG = "MessengerService";

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Log.e(TAG, "receive msg from Client :" + msg.getData().getString("msg"));

                    Messenger client = msg.replyTo;
                    Message replyMessage = Message.obtain(null, 2);
                    Bundle bundle = new Bundle();
                    bundle.putString("reply", "嗯，你的消息我已经收到，稍后回复你。");
                    replyMessage.setData(bundle);


                    try {
                        client.send(replyMessage);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private final Messenger mMessenger = new Messenger(new MessengerHandler());
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
