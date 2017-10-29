package com.example.cf.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;


public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    //处理网络回调之后的界面处理
    private Handler myHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0){
                mTextMessage.setText(msg.getData().getString("banner","无数据，或者错误！"));
            }
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextMessage.setText("正在获取。。。");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //调用apicloud接口,封装请求为 GET "https://d.apicloud.com/mcm/api/banner"
                        Resource resource = new Resource("A6054717694420","6BE6490A-3EF2-F9D8-3C42-68D2DA9C7FF8","https://d.apicloud.com");
                        JSONObject jsonObject = resource.getObjects("banner");
                        Message msg = new Message();
                        msg.what = 0;
                        Bundle bundle = new Bundle();
                        bundle.putString("banner","banner数据（json）:"+jsonObject.toString());
                        msg.setData(bundle);
                        myHandler.sendMessage(msg);
                    }
                }).start();
            }
        });

    }

}
