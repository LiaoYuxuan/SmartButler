package com.exemple.lenvo.smartbutler.utils;
/*
 *   项目名：  SmartButler
 *   包名：    com.exemple.lenvo.smartbutler.utils
 *   文件名：  L
 *   创建者：  LYX
 *   创建时间：2019/1/4 12:43
 *   描述：    LOG封装类
 */

import android.util.Log;

public class L {

    //开关
    public static final  boolean DEBUG = true;
    //TAG
    public static final String TAG = "Smartbutler";

    //五个等级  DIWE

    public static void d(String text){
        if(DEBUG){
            Log.d(TAG,text);
        }
    }

    public static void i(String text){
        if(DEBUG){
            Log.i(TAG,text);
        }
    }

    public static void w(String text){
        if(DEBUG){
            Log.w(TAG,text);
        }
    }

    public static void e(String text){
        if(DEBUG){
            Log.e(TAG,text);
        }
    }
}
