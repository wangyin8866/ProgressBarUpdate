package com.wyman.myapplication.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author wyman
 * @date 2018/6/7
 * description : 通过aidl来实现ipc
 */
public class BookManagerService extends Service {
    private static final String TAG = "BMS";
    private CopyOnWriteArrayList<Book> mBooks = new CopyOnWriteArrayList<>();
    private RemoteCallbackList<IOnNewBookArrivedListener> listeners = new RemoteCallbackList<>();

    private AtomicBoolean mIsServiceDestory = new AtomicBoolean(false);
    private Binder mBinder=new IBookManagerInterface.Stub() {
        @Override
        public List<Book> getBoobList() throws RemoteException {
            //模拟服务端耗时操作
//            SystemClock.sleep(5000);
            return mBooks;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBooks.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            listeners.register(listener);

        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            listeners.unregister(listener);
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBooks.add(new Book(1, "android"));
        mBooks.add(new Book(2, "java"));

        new Thread(new ServiceWorker()).start();
    }

    private class ServiceWorker implements Runnable {
        @Override
        public void run() {
            while(!mIsServiceDestory.get()){
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int bookId = mBooks.size() + 1;
                Book newBook = new Book(bookId, "new book#" + bookId);
                try {
                    onNewBookArrived(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void onNewBookArrived(Book newBook) throws RemoteException {
        mBooks.add(newBook);
        final int N = listeners.beginBroadcast();
        for (int i=0;i<N;i++) {
            IOnNewBookArrivedListener listener = listeners.getBroadcastItem(i);
            if (listener != null) {
                listener.onNewBookArrived(newBook);
            }
        }
        listeners.finishBroadcast();
    }
}
