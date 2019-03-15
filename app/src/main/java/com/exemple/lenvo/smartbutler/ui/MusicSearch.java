package com.exemple.lenvo.smartbutler.ui;
/*
 *   项目名：  SmartButler
 *   包名：    com.exemple.lenvo.smartbutler.ui
 *   文件名：  MusicSearch
 *   创建者：  LYX
 *   创建时间：2019/1/13 17:10
 *   描述：    TODO
 */

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.exemple.lenvo.smartbutler.R;
import com.exemple.lenvo.smartbutler.adapter.MusicAdapter;
import com.exemple.lenvo.smartbutler.entity.MusicData;
import com.exemple.lenvo.smartbutler.utils.L;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.angmarch.views.NiceSpinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class MusicSearch extends AppCompatActivity {

    private ListView mListView;
    private EditText toolbar_title;
    private String str1 = "",str2 = "",str3 = "";

    private List<MusicData> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicsearch);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //initView();

        final ImageView imageView =(ImageView)findViewById(R.id.mv_background);
        NiceSpinner niceSpinner = (NiceSpinner)findViewById(R.id.nice_spinner);
        NiceSpinner niceSpinner2 = (NiceSpinner)findViewById(R.id.nice_spinner2);
        List<String> dataList = new ArrayList<>();
        dataList.add("网易云音乐");
        dataList.add("QQ音乐");
        dataList.add("酷狗音乐");
        List<String> dataList2 = new ArrayList<>();
        dataList2.add("音乐搜索");
        //初始默认选择网易云音乐
        str1="https://api.bzqll.com/music/netease/search?key=579621905&s=";
        str2="&limit=100&offset=0&type=";
        str3="song";

        niceSpinner.attachDataSource(dataList);
        niceSpinner2.attachDataSource(dataList2);
        niceSpinner.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0 :
                        //网易云音乐接口
                        str1="https://api.bzqll.com/music/netease/search?key=579621905&s=";
                        str2="&limit=100&offset=0&type=";
                        imageView.setImageResource(R.mipmap.ic_wangyiyun);
                        Toast.makeText(MusicSearch.this, "网易云音乐" + position, Toast.LENGTH_SHORT).show();
                        break;
                    case 1 :
                        //qq音乐接口
                        str1="https://api.bzqll.com/music/tencent/search?key=579621905&s=";
                        str2="&limit=100&offset=0&type=";
                        imageView.setImageResource(R.mipmap.ic_qqmusic);
                        Toast.makeText(MusicSearch.this, "QQ音乐" + position, Toast.LENGTH_SHORT).show();
                        break;
                    case 2 :
                        //酷狗音乐接口
                        //注意酷狗音乐提供的是下载链接，不能直接播放
                        str1="https://api.bzqll.com/music/kugou/search?key=579621905&s=";
                        str2="&limit=100&offset=0&type=";
                        imageView.setImageResource(R.mipmap.ic_kugou);
                        Toast.makeText(MusicSearch.this, "酷狗音乐" + position, Toast.LENGTH_SHORT).show();
                        break;
            }
        }
        });

        niceSpinner2.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        str3 = "song";
                        break;
                }
            }
            });
    }

    //初始化View
    private void initView(String str1,String str2) {
        mListView = (ListView) findViewById(R.id.mListView);
        toolbar_title = (EditText) findViewById(R.id.toolbar_title);
        toolbar_title.setCursorVisible(false);

        //清空之前的listview
        if(mList.size()>0){
            mList.removeAll(mList);
            MusicAdapter adapter = new MusicAdapter(this,mList);
            adapter.notifyDataSetChanged();
            mListView.setAdapter(adapter);
        }

        String str = toolbar_title.getText().toString();
        //网易云接口
        //String str1="https://api.bzqll.com/music/netease/search?key=579621905&s=";
        //String str = toolbar_title.getText().toString();
        //String str2="&type=song&limit=100&offset=0";
        //转码后的中文网址
        //String str = "https://api.bzqll.com/music/netease/search?key=579621905&s=%E6%88%91%E5%96%9C%E6%AC%A2%E4%B8%8A%E4%BD%A0%E5%86%85%E5%BF%83%E6%97%B6%E7%9A%84%E6%B4%BB%E5%8A%A8&type=song&limit=100&offset=0";
        //用于证明解析方式正确
        // String str = "https://api.bzqll.com/music/netease/topMvList?key=579621905&limit=10&offset=0";
        try {
            String url = str1 + URLEncoder.encode(str, "utf-8") + str2;
            L.i(url);
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

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //解析数据
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
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
                    Toast.makeText(MusicSearch.this, "Click item" + position, Toast.LENGTH_SHORT).show();
                    //点击列表打开相应音乐播放网页
                    TextView tex = (TextView)view.findViewById(R.id.tv_singurl);
                    String str = tex.getText().toString();
                    Uri uri = Uri.parse(str);
                    //   Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    //   startActivity(intent);
                    //MediaPlayer player = MediaPlayer.create(MusicSearch.this, uri);
                    //player.start();
                    //startActivity(new Intent(MusicSearch.this,GramophoneActivity.class));
                    Intent intent=new Intent(MusicSearch.this,GramophoneActivity.class);
                    intent.putExtra("mList",(Serializable)mList);
                    intent.putExtra("position",position);
                    startActivity(intent);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }


    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.ic_search:
                if(str1.equals(""))
                {
                    Toast.makeText(this,"请先选择音乐平台",LENGTH_SHORT).show();
                }else {
                    initView(str1,str2 + str3);
                    Toast.makeText(this,"点击了搜索",LENGTH_SHORT).show();
                }
                break;
            case R.id.item_share:
                //网易云音乐接口
                 str1="https://api.bzqll.com/music/netease/search?key=579621905&s=";
                 str2="&type=song&limit=100&offset=0";
                Toast.makeText(this,"点击了网易云音乐",LENGTH_SHORT).show();
                break;
            case R.id.item_blacklist:
                //qq音乐接口
                 str1="https://api.bzqll.com/music/tencent/search?key=579621905&s=";
                 str2="&limit=100&offset=0&type=song";
                Toast.makeText(this,"点击了QQ音乐",LENGTH_SHORT).show();
                break;
            case R.id.item_report:
                //酷狗音乐接口
                //注意酷狗音乐提供的是下载链接，不能直接播放
                str1="https://api.bzqll.com/music/kugou/search?key=579621905&s=";
                str2="&limit=100&offset=0&type=song";
                Toast.makeText(this,"点击了酷狗音乐",LENGTH_SHORT).show();
                break;
            case R.id.toolbar_title:
                toolbar_title.setCursorVisible(true);
                break;
        }
        return true;}
}
