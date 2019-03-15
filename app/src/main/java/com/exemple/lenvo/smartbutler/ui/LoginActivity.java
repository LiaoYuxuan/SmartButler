package com.exemple.lenvo.smartbutler.ui;
/*
 *   项目名：  SmartButler
 *   包名：    com.exemple.lenvo.smartbutler.ui
 *   文件名：  LoginActivity
 *   创建者：  LYX
 *   创建时间：2019/1/7 10:34
 *   描述：    登录
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.exemple.lenvo.smartbutler.MainActivity;
import com.exemple.lenvo.smartbutler.R;
import com.exemple.lenvo.smartbutler.entity.MyUser;
import com.exemple.lenvo.smartbutler.utils.L;
import com.exemple.lenvo.smartbutler.utils.ShareUtils;
import com.exemple.lenvo.smartbutler.view.CustomDialog;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    //注册按钮
    private Button btn_registered;
    private EditText et_name;
    private EditText etpassword;
    private Button btnlogin;
    private CheckBox keep_password;
    private TextView tv_forget;
    private CustomDialog dialog;
    private ImageView bingPicImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        bingPicImg = (ImageView)findViewById(R.id.weather_layout);

        loadBingPic();
        btn_registered = (Button)findViewById(R.id.btn_registered);
        btn_registered.setOnClickListener(this);
        btnlogin = (Button)findViewById(R.id.btnlogin);
        btnlogin.setOnClickListener(this);

        dialog = new CustomDialog(this,500,500,R.layout.dialog_loding,R.style.Theme_dialog, Gravity.CENTER,R.style.pop_anim_style);
        //屏幕外点击无效
        dialog.setCancelable(false);

        et_name = (EditText)findViewById(R.id.et_name);
        etpassword = (EditText)findViewById(R.id.etpassword);
        keep_password = (CheckBox)findViewById(R.id.keep_password);
        //设置选中的状态
        boolean isCheck = ShareUtils.getBoolean(this,"keeppass",false);
        keep_password.setChecked(isCheck);
        if(isCheck){
         //显示保存的用户名和密码
            et_name.setText(ShareUtils.getString(this,"name",""));
            etpassword.setText(ShareUtils.getString(this,"password",""));
        }
        tv_forget = (TextView)findViewById(R.id.tv_forget);
        tv_forget.setOnClickListener(this);
    }

    private void loadBingPic() {
        String url = "http://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&mkt=zh-CN";
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                L.i("url:"+t);
                // Toast.makeText(SplashActivity.this, "结果：" + t, Toast.LENGTH_LONG).show();
                parsingJson(t);
            }
        });
    }

    //解析Json
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONArray jsonArray = jsonObject.getJSONArray("images");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);

                String Url = "http://www.bing.com"+json.getString("urlbase")+"_720x1280.jpg";
                L.i("url:"+Url);
                Picasso.with(this).load(Url)
                        .into(bingPicImg);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_registered:
                startActivity(new Intent(this,RegisteredActivity.class));
                break;
            case R.id.btnlogin:
                 //获取输入框的值
                String name = et_name.getText().toString().trim();
                String password = etpassword.getText().toString();
                //判断是否为空
                if(!TextUtils.isEmpty(name) & !TextUtils.isEmpty(password)){
                    //显示登陆中
                    dialog.show();
                //登录
                    final MyUser user = new MyUser();
                    user.setUsername(name);
                    user.setPassword(password);
                    user.login(new SaveListener<MyUser>() {
                        @Override
                        public void done(MyUser myUser, BmobException e) {
                            //dialog.dismiss();
                            //判断结果
                            if(e == null){
                                //判断邮箱是否验证
                                if(user.getEmailVerified()){
                                    //跳转
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    //防止出现窗体泄露
                                    if (dialog != null) {
                                        dialog.dismiss();
                                    }
                                    finish();
                                }else {
                                    Toast.makeText(LoginActivity.this,"请前往邮箱验证",Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }else{
                                Toast.makeText(LoginActivity.this,"登录失败："+e.toString(),Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    });
                }else{
                    Toast.makeText(LoginActivity.this,"输入框不能为空",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_forget:
                startActivity(new Intent(this,ForgetPasswordActivity.class));
                break;
        }
    }
    //假设我现在输入用户名与密码，但我不点击登录，而是直接退出了

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //保存状态
        ShareUtils.putBoolean(this,"keeppass",keep_password.isChecked());
        //是否记住密码
        if(keep_password.isChecked()){
           // 记住用户名和密码
            ShareUtils.putString(this,"name",et_name.getText().toString().trim());
            ShareUtils.putString(this,"password",etpassword.getText().toString().trim());
        }else{
            ShareUtils.deleShare(this,"name");
            ShareUtils.deleShare(this,"password");
        }
    }
}
