package com.exemple.lenvo.smartbutler.utils;
/*
 *   项目名：  SmartButler
 *   包名：    com.exemple.lenvo.smartbutler.utils
 *   文件名：  AndroidPermissionUtils
 *   创建者：  LYX
 *   创建时间：2019/1/14 14:55
 *   描述：    TODO
 */

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Android 6.0及以上权限工具类
 */

public class AndroidPermissionUtils {

    /**
     * Android 6.0及以上 检测是否具有某些权限
     * */

    public static boolean hasAndroidPermission(Context context, String [] permission){
        boolean has=true;
        for(String per:permission){
            if(ContextCompat.checkSelfPermission(context,per) != PackageManager.PERMISSION_GRANTED){
                has=false;
                break;
            }
        }
        return has;
    }

    /**
     * Android 6.0及以上 申请某些权限
     * */

    public static void requestAndroidPermission(Activity activity, int code, String []permission){
        ActivityCompat.requestPermissions(activity,permission,code);
    }

}
