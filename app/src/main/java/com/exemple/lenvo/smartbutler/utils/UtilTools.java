package com.exemple.lenvo.smartbutler.utils;
/*
 *   项目名：  SmartButler
 *   包名：    com.exemple.lenvo.smartbutler.utils
 *   文件名：  UtilTools
 *   创建者：  LYX
 *   创建时间：2019/1/4 12:41
 *   描述：    工具统一类
 */

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import com.exemple.lenvo.smartbutler.R;
import com.exemple.lenvo.smartbutler.entity.MyUser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import cn.bmob.v3.BmobUser;

public class UtilTools {
    //设置字体
    public static void setFont(Context mContext, TextView mTextView){
        Typeface fontType = Typeface.createFromAsset(mContext.getAssets(),"fonts/FONT.TTF");
        mTextView.setTypeface(fontType);
    }
    //保存图片
    public static void putImageToShare(Context mContext,ImageView imageView){
        BitmapDrawable drawable = (BitmapDrawable)imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        //第一步：将Bitmap压缩成字节数组输出流
        ByteArrayOutputStream byStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,80,byStream);
        //第二步：利用Base64将我们的字节数组输出流转换成String
        byte[] byteArray = byStream.toByteArray();
        String imgString = new String(Base64.encodeToString(byteArray,Base64.DEFAULT));
        //第三步：将String保存ShareUtils
        MyUser user = new MyUser();
        user.setPhoto(imgString);
      //  ShareUtils.putString(mContext,"image_title",imgString);
    }
    //读取图片
    public static void getImageToShare(Context mContext,ImageView imageView){
        //1.拿到string
      //  String imgString = ShareUtils.getString(mContext,"image_title","");
        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
        String imgString = userInfo.getPhoto();
        if(!imgString.equals("")){
            //2.利用Base64将string转换
            byte [] byteArray =Base64.decode(imgString,Base64.DEFAULT);
            ByteArrayInputStream byStream =new ByteArrayInputStream(byteArray);
            //3.生成Bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(byStream);
            imageView.setImageBitmap(bitmap);
        }
    }

    //获取版本号
    public static String getVersion(Context mContext){
        PackageManager pm = mContext.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(mContext.getPackageName(),0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return mContext.getString(R.string.text_unknown);
        }
    }
}
