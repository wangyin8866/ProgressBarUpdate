// IBookManagerInterface.aidl
package com.wyman.myapplication.aidl;

// Declare any non-default types here with import statements

import com.wyman.myapplication.aidl.Book;
import com.wyman.myapplication.aidl.IOnNewBookArrivedListener;

interface IBookManagerInterface {

    List<Book> getBoobList();
    List<Book> addBook(in Book book);

    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);

}
