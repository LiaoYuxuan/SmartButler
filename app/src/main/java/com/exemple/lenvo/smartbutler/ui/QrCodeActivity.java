package com.exemple.lenvo.smartbutler.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.exemple.lenvo.smartbutler.R;
import com.exemple.lenvo.smartbutler.utils.QRCodeUtil;

/*
 *   项目名：  SmartButler
 *   包名：    com.exemple.lenvo.smartbutler.ui
 *   文件名：  QrCodeActivity
 *   创建者：  LYX
 *   创建时间：2019/1/28 18:44
 *   描述：    TODO
 */
public class QrCodeActivity extends BaseActivity {

    //我的二维码
    private ImageView iv_qr_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        initView();
    }

    private void initView() {

        iv_qr_code = (ImageView) findViewById(R.id.iv_qr_code);
        //屏幕的宽
        int width = getResources().getDisplayMetrics().widthPixels;

        Bitmap bitmap = ((BitmapDrawable)getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap();
        Bitmap mBitmap = QRCodeUtil.createQRCodeBitmap("https://www.baidu.com", 480 ,bitmap ,0.2F);
        iv_qr_code.setImageBitmap(mBitmap);

//        Bitmap qrCodeBitmap = EncodingUtils.createQRCode("我是智能管家", width / 2, width / 2,
//                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
//        iv_qr_code.setImageBitmap(qrCodeBitmap);
    }
}
