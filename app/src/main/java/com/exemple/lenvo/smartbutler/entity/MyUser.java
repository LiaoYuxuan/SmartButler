package com.exemple.lenvo.smartbutler.entity;
/*
 *   项目名：  SmartButler
 *   包名：    com.exemple.lenvo.smartbutler.entity
 *   文件名：  MyUser
 *   创建者：  LYX
 *   创建时间：2019/1/7 12:01
 *   描述：    用户属性
 */

import cn.bmob.v3.BmobUser;

public class MyUser extends BmobUser{
    //用户年龄
    private int age;
    //true为男，false为女
    private boolean sex;
    //用户描述
    private String desc;
    //用户头像
    private String photo;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
