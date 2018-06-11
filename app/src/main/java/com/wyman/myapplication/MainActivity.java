package com.wyman.myapplication;

import android.content.Entity;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.view.View.LAYER_TYPE_SOFTWARE;

public class MainActivity extends AppCompatActivity {
    private CustomView customView1, customView2, customView3, customView4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customView1 = findViewById(R.id.cv1);
        customView2 = findViewById(R.id.cv2);
        customView3 = findViewById(R.id.cv3);
        customView4 = findViewById(R.id.cv4);

        customView1.setLayerType(LAYER_TYPE_SOFTWARE, null);
        customView2.setLayerType(LAYER_TYPE_SOFTWARE, null);
        customView3.setLayerType(LAYER_TYPE_SOFTWARE, null);
        customView4.setLayerType(LAYER_TYPE_SOFTWARE, null);
        customView1.setData(50,1000);
        customView2.setData(50,100);
        customView3.setData(100,100);
        customView4.setData(10,100);


    }


}
