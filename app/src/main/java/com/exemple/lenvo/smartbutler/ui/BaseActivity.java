package com.exemple.lenvo.smartbutler.ui;
/*
 *   项目名：  SmartButler
 *   包名：    com.exemple.lenvo.smartbutler.ui
 *   文件名：  BaseActivity
 *   创建者：  LYX
 *   创建时间：2019/1/4 12:15
 *   描述：    Activity的基类
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * 主要做的事情：
 * 1.统一的属性
 * 2.统一的接口，如在类名后加上implements View.OnClickListener
 * 3.统一的方法
 */

public class BaseActivity extends AppCompatActivity {

    //注意此处是基类，不需要绑定布局，且onCreate方法是protected类型
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //显示返回按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //菜单栏操作,输入onop较便捷

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
