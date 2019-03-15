package com.exemple.lenvo.smartbutler.ui;
/*
 *   项目名：  SmartButler
 *   包名：    com.exemple.lenvo.smartbutler.ui
 *   文件名：  RegisteredActivity
 *   创建者：  LYX
 *   创建时间：2019/1/7 10:58
 *   描述：    注册
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.exemple.lenvo.smartbutler.R;
import com.exemple.lenvo.smartbutler.entity.MyUser;
import com.exemple.lenvo.smartbutler.utils.L;

import java.io.ByteArrayOutputStream;
import java.io.File;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class RegisteredActivity extends BaseActivity implements View.OnClickListener{
    private EditText et_user;
    private EditText et_age;
    private EditText et_desc;
    private RadioGroup mRadioGroup;
    private EditText et_pass;
    private EditText et_password;
    private EditText et_email;
    private Button btnRegistered;
    //性别
    private boolean isGender = true;
    private String et_photo;
    private CircleImageView photo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);

        initView();
    }

    private void initView() {
        et_user = (EditText)findViewById(R.id.et_user);
        et_age  = (EditText)findViewById(R.id.et_age);
        et_desc = (EditText)findViewById(R.id.et_desc);
        mRadioGroup = (RadioGroup)findViewById(R.id.mRadioGroup);
        et_pass = (EditText)findViewById(R.id.et_pass);
        et_password = (EditText)findViewById(R.id.et_password);
        et_email = (EditText)findViewById(R.id.et_email);
        btnRegistered = (Button) findViewById(R.id.btnRegistered);
        btnRegistered.setOnClickListener(this);
        photo = (CircleImageView) findViewById(R.id.et_photo);
        photo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.et_photo:
                toPicture();
                break;
            case R.id.btnRegistered:
                //获取到输入框的值,trim用于去空格
                String name = et_user.getText().toString().trim();
                String age = et_age.getText().toString().trim();
                String desc = et_desc.getText().toString().trim();
                String pass = et_pass.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                String email = et_email.getText().toString().trim();
                //判断是否为空
                if(!TextUtils.isEmpty(name) & !TextUtils.isEmpty(age) &
                        !TextUtils.isEmpty(pass) & !TextUtils.isEmpty(password)
                        & !TextUtils.isEmpty(email)){
                    //判断两次输入的密码是否一致
                    if(pass.equals(password)){
                        //先把性别判断下
                        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                if(checkedId == R.id.rb_boy){
                                    isGender = true;
                                }else if(checkedId == R.id.rb_girl){
                                    isGender = false;
                                }
                            }
                        });
                        //判断简介是否为空
                        if(!TextUtils.isEmpty(desc)){
                            desc = "这个人很懒，什么也没留下";
                        }
                        //注册
                        MyUser user = new MyUser();
                        user.setUsername(name);
                        user.setPassword(pass);
                        //强制转换
                        user.setAge(Integer.parseInt(age));
                        user.setEmail(email);
                        user.setDesc(desc);
                        user.setSex(isGender);
                        BitmapDrawable drawable = (BitmapDrawable)photo.getDrawable();
                        Bitmap bitmap = drawable.getBitmap();
                        //第一步：将Bitmap压缩成字节数组输出流
                        ByteArrayOutputStream byStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG,80,byStream);
                        //第二步：利用Base64将我们的字节数组输出流转换成String
                        byte[] byteArray = byStream.toByteArray();
                        String imgString = new String(Base64.encodeToString(byteArray,Base64.DEFAULT));
                        user.setPhoto(imgString);

                        user.signUp(new SaveListener<MyUser>() {
                            @Override
                            public void done(MyUser myUser, BmobException e) {
                                if(e==null){
                                    //内部类中使用toast时不能用this
                                    Toast.makeText(RegisteredActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                                    finish();
                                }else{
                                    Toast.makeText(RegisteredActivity.this,"注册失败："+e.toString(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(this,"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this,"输入框不能为空",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public static final String PHOTO_IMAGE_FILE_NAME = "fileImg.jpg";
    public static final int CAMERA_REQUEST_CODE = 100;
    public static final int IMAGE_REQUEST_CODE = 101;
    public static final int RESULT_REQUEST_CODE = 102;
    private File tempFile = null;

    private void toPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != this.RESULT_CANCELED) {
            switch (requestCode) {
                //相册数据
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                //相机数据
                case CAMERA_REQUEST_CODE:
                    tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME);
                    startPhotoZoom(Uri.fromFile(tempFile));
                    break;
                case RESULT_REQUEST_CODE:
                    //有可能用户点击舍弃
                    if (data != null) {
                        //拿到图片设置
                        setImageToView(data);
                        //既然已经设置了图片，我们原先的就应该删除
                        if (tempFile != null) {
                            tempFile.delete();
                        }
                    }
                    break;
            }
        }
    }

    //裁剪
    private void startPhotoZoom(Uri uri) {
        if (uri == null) {
            L.e("uri == null");
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //设置裁剪
        intent.putExtra("crop", "true");
        //裁剪宽高
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪图片的分辨率
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        //发送数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    //设置图片
    private void setImageToView(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            Bitmap bitmap = bundle.getParcelable("data");
            photo.setImageBitmap(bitmap);
        }
    }
}
