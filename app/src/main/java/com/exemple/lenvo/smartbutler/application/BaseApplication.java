package com.exemple.lenvo.smartbutler.application;
/*
 *   项目名：  SmartButler
 *   包名：    com.exemple.lenvo.smartbutler.application
 *   文件名：  BaseApplication
 *   创建者：  LYX
 *   创建时间：2019/1/4 12:05
 *   描述：    Application
 */

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.exemple.lenvo.smartbutler.utils.StaticClass;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.tencent.bugly.crashreport.CrashReport;

import cn.bmob.v3.Bmob;

public class BaseApplication extends Application{

    //创建

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化Bugly
        CrashReport.initCrashReport(getApplicationContext(), StaticClass.BUGLY_APP_ID, true);
        //初始化Bmob
        Bmob.initialize(this, StaticClass.BMOB_APP_ID);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        // 将“12345678”替换成您申请的APPID，申请地址：http://www.xfyun.cn
       // 请勿在“=”与appid之间添加任何空字符或者转义符
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID +"="+StaticClass.VOICE_KEY);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(getApplicationContext());
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }
}
