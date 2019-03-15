package com.exemple.lenvo.smartbutler.entity;
/*
 *   项目名：  SmartButler
 *   包名：    com.exemple.lenvo.smartbutler.entity
 *   文件名：  MusicData
 *   创建者：  LYX
 *   创建时间：2019/1/13 16:19
 *   描述：    TODO
 */

import java.io.Serializable;

public class MusicData implements Serializable {
    //歌曲名称
    private String name;
    //歌手名
    private String singer;
    //播放链接
    private String singurl;

    private String photo;

    private int time;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getSingurl() {
        return singurl;
    }

    public void setSingurl(String singurl) {
        this.singurl = singurl;
    }

    @Override
    public String toString() {
        return "CourierData{" +
                "name='" + name + '\'' +
                ", singer='" + singer + '\'' +
                ", singurl='" + singurl + '\'' +
                '}';
    }
}
