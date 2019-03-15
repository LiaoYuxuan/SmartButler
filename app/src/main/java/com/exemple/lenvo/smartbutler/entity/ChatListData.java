package com.exemple.lenvo.smartbutler.entity;

/*
 *   项目名：  SmartButler
 *   包名：    com.exemple.lenvo.smartbutler.entity
 *   文件名：  ChatListData
 *   创建者：  LYX
 *   创建时间：2019/1/17 14:24
 *   描述：    对话列表的实体
 */
public class ChatListData {
    //区分左边还是右边
    private int type;
    //文本
    private String text;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
