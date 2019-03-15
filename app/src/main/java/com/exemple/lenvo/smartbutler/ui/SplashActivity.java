package com.exemple.lenvo.smartbutler.ui;
/*
 *   项目名：  SmartButler
 *   包名：    com.exemple.lenvo.smartbutler.ui
 *   文件名：  SplashActivity
 *   创建者：  LYX
 *   创建时间：2019/1/6 12:26
 *   描述：    闪屏页
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.exemple.lenvo.smartbutler.R;
import com.exemple.lenvo.smartbutler.utils.ShareUtils;
import com.exemple.lenvo.smartbutler.utils.StaticClass;
import com.exemple.lenvo.smartbutler.utils.UtilTools;

public class SplashActivity extends AppCompatActivity{
    /**
     * 1.延时2000ms
     * 2.判断程序是否第一次运行
     * 3.自定义字体
     * 4.Activity全屏主题
     */
    private TextView tv_splash;
    //延时
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //过滤
            switch (msg.what){
                case StaticClass.HANDLER_SPLASH:
                    //判断程序是否第一次运行
                    if(isFirst()){
                        startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                    }else{
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                    //跳转之后要记得finish
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        initView();
    }

    //初始化view
    private void initView() {
        //程序进入则发送延时消息
        handler.sendEmptyMessageDelayed(StaticClass.HANDLER_SPLASH,4000);
        tv_splash = (TextView) findViewById(R.id.tv_splash);
        //设置字体
        UtilTools.setFont(this,tv_splash);
    }

        //判断程序是否第一次运行
        private boolean isFirst() {
            boolean isFirst = ShareUtils.getBoolean(this,StaticClass.SHARE_IS_FIRST,true);
            if(isFirst){
                ShareUtils.putBoolean(this,StaticClass.SHARE_IS_FIRST,false);
                //是第一次运行
                return true;
            }else {
                return false;
            }
        }
    //禁止返回键
    @Override
    public void onBackPressed() {
       // super.onBackPressed();
    }
}
