// IOnNewBookArrivedListener.aidl
package com.wyman.myapplication.aidl;

// Declare any non-default types here with import statements
import com.wyman.myapplication.aidl.Book;
interface IOnNewBookArrivedListener {
   void onNewBookArrived(in Book newBook);
}
