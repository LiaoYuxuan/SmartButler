package com.exemple.lenvo.smartbutler.ui;
/*
 *   项目名：  SmartButler
 *   包名：    com.exemple.lenvo.smartbutler.ui
 *   文件名：  GuideActivity
 *   创建者：  LYX
 *   创建时间：2019/1/6 13:01
 *   描述：    引导页
 */

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.exemple.lenvo.smartbutler.R;

import java.util.ArrayList;
import java.util.List;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

public class GuideActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager mViewPager;
    //容器
    private List<View>mList = new ArrayList<>();
    private View view1,view2,view3;
    //小圆点
    private ImageView point1,point2,point3;
    //跳过按钮
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();  
    }

    private void initView() {
        //动态申请权限
        List<PermissionItem> permissions = new ArrayList<PermissionItem>();
        permissions.add(new PermissionItem(Manifest.permission.CAMERA, "照相机", R.drawable.permission_ic_camera));
        permissions.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "存储", R.drawable.permission_ic_storage));
        permissions.add(new PermissionItem(Manifest.permission.RECEIVE_SMS , "短信", R.drawable.permission_ic_sms));
        HiPermission.create(this)
                .title(getString(R.string.permission_cus_title))
                .permissions(permissions)
                .msg(getString(R.string.permission_cus_msg))
                .animStyle(R.style.PermissionAnimScale)
                // .style(R.style.PermissionDefaultGreenStyle)
                .style(R.style.PermissionBlueStyle)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {

                        showToast(getString(R.string.permission_on_close));
                    }

                    @Override
                    public void onFinish() {
                        showToast(getString(R.string.permission_completed));

                    }

                    @Override
                    public void onDeny(String permission, int position) {

                    }

                    @Override
                    public void onGuarantee(String permission, int position) {

                    }
                });


        point1 = (ImageView) findViewById(R.id.point1);
        point2 = (ImageView) findViewById(R.id.point2);
        point3 = (ImageView) findViewById(R.id.point3);
        //设置默认图片
        setPointImg(true,false,false);

        iv_back = (ImageView)findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        mViewPager = (ViewPager)findViewById(R.id.mViewPager);

        view1 = View.inflate(this,R.layout.pager_item_one,null);
        view2 = View.inflate(this,R.layout.pager_item_two,null);
        view3 = View.inflate(this,R.layout.pager_item_three,null);
        view3.findViewById(R.id.btn_start).setOnClickListener(this);

        mList.add(view1);
        mList.add(view2);
        mList.add(view3);
        //设置适配器
        mViewPager.setAdapter(new GuideAdapter());
        //监听ViewPager滑动
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        setPointImg(true,false,false);
                        iv_back.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        setPointImg(false,true,false);
                        iv_back.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        setPointImg(false,false,true);
                        iv_back.setVisibility(View.GONE);
                        break;
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void showToast(String text) {
        Toast.makeText(GuideActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start:
            case R.id.iv_back:
                startActivity(new Intent(this, LoginActivity.class));
                finish();

            break;
        }
    }

    class GuideAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        //增加
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager)container).addView(mList.get(position));
            return mList.get(position);
        }
        //销毁

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager)container).removeView(mList.get(position));
            //super.destroyItem(container, position, object);
        }
    }
    //设置小圆点的选中效果
    private void setPointImg(boolean isCheck1,boolean isCheck2,boolean isCheck3){
       if(isCheck1){
           point1.setBackgroundResource(R.drawable.point_on);
       }else {
           point1.setBackgroundResource(R.drawable.point_off);
       }
        if(isCheck2){
            point2.setBackgroundResource(R.drawable.point_on);
        }else {
            point2.setBackgroundResource(R.drawable.point_off);
        }
        if(isCheck3){
            point3.setBackgroundResource(R.drawable.point_on);
        }else {
            point3.setBackgroundResource(R.drawable.point_off);
        }
    }
}
