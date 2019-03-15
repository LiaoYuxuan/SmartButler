package com.exemple.lenvo.smartbutler.ui;
/*
 *   项目名：  SmartButler
 *   包名：    com.exemple.lenvo.smartbutler.ui
 *   文件名：  MusicActivity
 *   创建者：  LYX
 *   创建时间：2019/1/13 16:15
 *   描述：    网易云热单
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.exemple.lenvo.smartbutler.R;
import com.exemple.lenvo.smartbutler.adapter.MusicAdapter;
import com.exemple.lenvo.smartbutler.entity.MusicData;
import com.exemple.lenvo.smartbutler.utils.L;
import com.exemple.lenvo.smartbutler.utils.StaticClass;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MusicActivity extends BaseActivity implements View.OnClickListener {

    private ListView mListView;

    private List<MusicData> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        initView();
    }

    //初始化View
    private void initView() {
        mListView = (ListView) findViewById(R.id.mListView);

        String url = "https://api.bzqll.com/music/netease/songList?" +
                "key="+ StaticClass.MUSIC_KEY+"&id=3778678&limit=10&offset=0";
        //3.拿到数据去请求数据（Json）
        //在API24上运行会在此处崩溃，原因可能与权限有关
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                //      Toast.makeText(CourierActivity.this, t, Toast.LENGTH_SHORT).show();
                L.i("Courier:" + t);
                //4.解析Json
                parsingJson(t);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    //解析数据
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONObject jsonResult = jsonObject.getJSONObject("data");
            JSONArray jsonArray = jsonResult.getJSONArray("songs");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);

                MusicData data = new MusicData();
                data.setName(json.getString("name"));
                data.setSinger(json.getString("singer"));
                data.setSingurl(json.getString("url"));
                data.setPhoto(json.getString("pic"));
                data.setTime(json.getInt("time"));

                mList.add(data);
            }

            MusicAdapter adapter = new MusicAdapter(this,mList);
            mListView.setAdapter(adapter);
            //列表内部点击事件
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(MusicActivity.this, "Click item" + position, Toast.LENGTH_SHORT).show();
                    //点击列表打开相应音乐播放网页
                    Intent intent=new Intent(MusicActivity.this,GramophoneActivity.class);
                    //传递基本数据类型
                    //intent.putExtra("str",str);
                    //intent.putExtra("name",mList.get(position).getName());
                    //intent.putExtra("pic",mList.get(position).getPhoto());
                    //intent.putExtra("singer",mList.get(position).getSinger());
                    intent.putExtra("mList",(Serializable)mList);
                    intent.putExtra("position",position);
                   // intent.putExtra("time",mList.get(position).getTime());
                    startActivity(intent);

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
