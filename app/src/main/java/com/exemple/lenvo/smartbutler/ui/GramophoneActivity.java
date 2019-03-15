package com.exemple.lenvo.smartbutler.ui;
/*
 *   项目名：  Disc
 *   包名：    com.exemple.lenvo.disc
 *   文件名：  GramophoneActivity
 *   创建者：  LYX
 *   创建时间：2019/1/16 9:15
 *   描述：    TODO
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.exemple.lenvo.smartbutler.R;
import com.exemple.lenvo.smartbutler.entity.MusicData;
import com.exemple.lenvo.smartbutler.view.GramophoneView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 演示唱片机View用的界面
 */
public class GramophoneActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {


    private TextView now_time;
    private SeekBar audio_seekBar;
    private ImageView btn_play_pause;
    private ImageView ivNext;
    private ImageView ivLast;
    private TextView tvTotalTime;
    private GramophoneView gramophoneView;
    private ViewPager mv_viewpager;
    private List<MusicData> mv_List = new ArrayList<>();
    private int now_position;
    private int music_time;
    final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if(msg.what == 1){
                m.stop();
                audio_seekBar.setProgress(0);
                gramophoneView.setPlaying(false);
                btn_play_pause.setImageResource(R.drawable.ic_play);
                playNext();
            }
        }
    };

    private MediaPlayer m;
    private Context mContext;
    private Thread thread;
    //记录播放位置
    private int time;
    //记录是否暂停
    private boolean flage = false, isChanging = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gramophone);

        //Media控件设置
        m = new MediaPlayer();
        init();

        //final GramophoneView gramophoneView = (GramophoneView)findViewById(R.id.gramophone_view);
       // final ImageView button = (ImageView) findViewById(R.id.btn_play_pause);

       // final String str=getIntent().getStringExtra("str");
        //final Uri uri = Uri.parse(str);
        //final MediaPlayer player = MediaPlayer.create(GramophoneActivity.this, uri);
        //gramophoneView.setPlaying(true);

        //gramophoneView.setPictureRes();


      //  player.start();
   /*
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(gramophoneView.getPlaying()){
                    button.setImageResource(R.drawable.ic_play);
                    player.pause();
                    //button.setText("点击播放");
                }else{
                    button.setImageResource(R.drawable.ic_pause);
                   // button.setText("点击暂停");
                    player.start();
                }
                gramophoneView.setPlaying(!gramophoneView.getPlaying());
            }
        });
    */
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //点击back键finish当前activity
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    //时间显示函数,我们获得音乐信息的是以毫秒为单位的，把把转换成我们熟悉的00:00格式
    public String ShowTime(int time) {
        time /= 1000;
        int minute = time / 60;
        int second = time % 60;
        minute %= 60;
        return String.format("%02d:%02d", minute, second);
    }

    //Activity从后台重新回到前台时被调用
    @Override
    protected void onRestart() {
        super.onRestart();
        if (m != null) {
            if (m.isPlaying()) {
                m.start();
            }
        }
    }

    //Activity被覆盖到下面或者锁屏时被调用
    @Override
    protected void onPause() {
        super.onPause();
        if (m != null) {
            if (m.isPlaying()) {
                m.pause();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (m != null) {
            if (!m.isPlaying()) {
                m.start();
            }
        }
    }

    //Activity被销毁
    protected void onDestroy() {
        if (m.isPlaying()) {
            m.stop();//停止音频的播放
        }
        m.release();//释放资源
        super.onDestroy();
    }

    class ClickEvent implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_play_pause:
                    if (m.isPlaying()) {
                        //m.getCurrentPosition();获取当前播放位置
                        time = m.getCurrentPosition();
                        // 如果正在播放，则暂停，并把按钮上的文字设置成“暂停”
                        m.pause();
                        gramophoneView.setPlaying(false);
                        btn_play_pause.setImageResource(R.drawable.ic_play);
                        flage = true;//flage 标记为 ture
                    } else if (flage) {
                        m.start();//先开始播放
                        gramophoneView.setPlaying(true);
                        m.seekTo(time);//设置从哪里开始播放
                        btn_play_pause.setImageResource(R.drawable.ic_pause);
                        flage = false;
                    } else {
                        m.reset();//恢复到未初始化的状态
                        String str = mv_List.get(now_position).getSingurl();
                        Uri uri = Uri.parse(str);
                        m = MediaPlayer.create(GramophoneActivity.this, uri);//读取音频
                        audio_seekBar.setMax(m.getDuration());//设置SeekBar的长度
                        try {
                            m.prepare();    //准备
                        } catch (IllegalStateException | IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        m.start();  //播放
                        gramophoneView.setPlaying(true);
                        // 创建一个线程
                        btn_play_pause.setImageResource(R.drawable.ic_pause);
                    }
                    thread = new Thread(new SeekBarThread());
                    // 启动线程
                    thread.start();
                    break;
                case R.id.ivNext:
                    m.stop();
                    audio_seekBar.setProgress(0);
                    gramophoneView.setPlaying(false);
                    btn_play_pause.setImageResource(R.drawable.ic_play);

                    playNext();

                    break;
                case R.id.ivLast:
                    m.stop();
                    audio_seekBar.setProgress(0);
                    gramophoneView.setPlaying(false);
                    btn_play_pause.setImageResource(R.drawable.ic_play);

                    playLast();

                    break;
            }
        }
    }

    private void playLast(){
        if(now_position > 0){
            Toast.makeText(GramophoneActivity.this, now_position+"和"+mv_List.size(), Toast.LENGTH_SHORT).show();
            now_position = now_position - 1;
            updateView();
        }else{
            Toast.makeText(GramophoneActivity.this, "已经是第一首歌曲啦", Toast.LENGTH_SHORT).show();
        }

        m.reset();//恢复到未初始化的状态
        String str = mv_List.get(now_position).getSingurl();
        Uri uri = Uri.parse(str);
        m = MediaPlayer.create(GramophoneActivity.this, uri);//读取音频
        audio_seekBar.setMax(m.getDuration());//设置SeekBar的长度
        try {
            m.prepare();    //准备
        } catch (IllegalStateException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        m.start();  //播放
        gramophoneView.setPlaying(true);
        // 创建一个线程
        btn_play_pause.setImageResource(R.drawable.ic_pause);
        thread = new Thread(new SeekBarThread());
        // 启动线程
        thread.start();
    }

    private void playNext(){
        if(now_position < mv_List.size()-1){
            Toast.makeText(GramophoneActivity.this, now_position+"和"+mv_List.size(), Toast.LENGTH_SHORT).show();
            now_position = now_position+1;
            updateView();
        }else{
            Toast.makeText(GramophoneActivity.this, "已经是最后一首歌曲啦", Toast.LENGTH_SHORT).show();
        }

        m.reset();//恢复到未初始化的状态
        String str = mv_List.get(now_position).getSingurl();
        Uri uri = Uri.parse(str);
        m = MediaPlayer.create(GramophoneActivity.this, uri);//读取音频
        audio_seekBar.setMax(m.getDuration());//设置SeekBar的长度
        try {
            m.prepare();    //准备
        } catch (IllegalStateException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        m.start();  //播放
        gramophoneView.setPlaying(true);
        // 创建一个线程
        btn_play_pause.setImageResource(R.drawable.ic_pause);
        thread = new Thread(new SeekBarThread());
        // 启动线程
        thread.start();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        now_time.setText(ShowTime(progress));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //防止在拖动进度条进行进度设置时与Thread更新播放进度条冲突
        isChanging = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        now_time.setText(ShowTime(seekBar.getProgress()));
        //将media进度设置为当前seekbar的进度
        m.seekTo(seekBar.getProgress());
        isChanging = false;
        thread = new Thread(new SeekBarThread());
        // 启动线程
        thread.start();
    }

    // 自定义的线程
    class SeekBarThread implements Runnable {

        @Override
        public void run() {
            while (!isChanging && m.isPlaying()) {
                // 将SeekBar位置设置到当前播放位置
                audio_seekBar.setProgress(m.getCurrentPosition());
                //在非主线程中使用Toast需要用looper
                //Looper.prepare();
                //Toast.makeText(GramophoneActivity.this, ShowTime(m.getCurrentPosition())
                        //+"和"+ShowTime(mv_List.get(now_position).getTime()*1000), Toast.LENGTH_SHORT).show();
                //Looper.loop();

                if (ShowTime(m.getCurrentPosition())
                        .equals(ShowTime(mv_List.get(now_position).getTime()*1000))){
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }
                try {
                    // 每100毫秒更新一次位置
                    Thread.sleep(100);
                    //播放进度
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void init() {
        //接收传递的参数
        mv_List = (ArrayList<MusicData>)getIntent().getSerializableExtra("mList");
        now_position =getIntent().getIntExtra("position",0);

        audio_seekBar = (SeekBar) findViewById(R.id.musicSeekBar);

        btn_play_pause = (ImageView) findViewById(R.id.btn_play_pause);
        ivNext = (ImageView) findViewById(R.id.ivNext);
        ivLast = (ImageView) findViewById(R.id.ivLast);

        gramophoneView = (GramophoneView)findViewById(R.id.gramophone_view);

        btn_play_pause.setOnClickListener(new ClickEvent());
        ivNext.setOnClickListener(new ClickEvent());
        ivLast.setOnClickListener(new ClickEvent());
        audio_seekBar.setOnSeekBarChangeListener(this);

        btn_play_pause.setImageResource(R.drawable.ic_play);

        updateView();
    }

    private void updateView(){
        //当前播放时间和总歌曲长度
        now_time = (TextView) findViewById(R.id.tvCurrentTime);
        now_time.setText("00:00");
        tvTotalTime = (TextView)findViewById(R.id.tvTotalTime);
        music_time =  mv_List.get(now_position).getTime();
        tvTotalTime.setText(ShowTime(music_time*1000));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);

        String name = mv_List.get(now_position).getName();
        String pic = mv_List.get(now_position).getPhoto();
        String singer = mv_List.get(now_position).getSinger();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setSubtitle(singer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //设置图片
        ImageView image = (ImageView)findViewById(R.id.picture);
        Picasso.with(mContext).load(pic)
                .into(image);
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        gramophoneView.setBitmap(bitmap);
    }
}
