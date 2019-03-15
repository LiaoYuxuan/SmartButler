package com.exemple.lenvo.smartbutler.adapter;
/*
 *   项目名：  SmartButler
 *   包名：    com.exemple.lenvo.smartbutler.adapter
 *   文件名：  MusicAdapter
 *   创建者：  LYX
 *   创建时间：2019/1/13 16:23
 *   描述：    音乐适配器
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.exemple.lenvo.smartbutler.R;
import com.exemple.lenvo.smartbutler.entity.MusicData;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MusicAdapter extends BaseAdapter {

    private Context mContext;
    private List<MusicData> mList;
    //布局加载器
    private LayoutInflater inflater;
    private MusicData data;
    private ImageView image;

    public MusicAdapter(Context mContext, List<MusicData> mList) {
        this.mContext = mContext;
        this.mList = mList;
        //获取系统服务
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        //第一次加载
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_music_item,null);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_singer = (TextView) convertView.findViewById(R.id.tv_singer);
            viewHolder.tv_singurl = (TextView) convertView.findViewById(R.id.tv_singurl);
            //设置缓存
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //设置数据
        data = mList.get(position);

        viewHolder.tv_name.setText(data.getName());
        viewHolder.tv_singer.setText(data.getSinger());
        viewHolder.tv_singurl.setText(data.getSingurl());
        //利用Picasso加载歌曲图片
        image =(ImageView) convertView.findViewById(R.id.sing_photo);
        Picasso.with(mContext).load(mList.get(position).getPhoto())
                .into(image);

        return convertView;
    }



    class ViewHolder{
        private TextView tv_name;
        private TextView tv_singer;
        private TextView tv_singurl;
    }
}
