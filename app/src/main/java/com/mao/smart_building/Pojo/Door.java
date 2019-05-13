package com.mao.smart_building.Pojo;

import java.util.Date;

/**
 * Created by Mingpeidev on 2019/5/11.
 */

public class Door {

    private int id;
    private String residentname;
    private String doorid;

    //居民信息
    private String sex;

    //出行信息和alarm
    private String state;
    private String time;

    //alarm信息的
    private String human;
    private String smoke;

    public Door() {
    }

    public Door(int id, String residentname, String doorid, String sex, String state, String time, String human, String smoke) {
        this.id = id;
        this.residentname = residentname;
        this.doorid = doorid;
        this.sex = sex;
        this.state = state;
        this.time = time;

        this.human = human;
        this.smoke = smoke;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResidentname() {
        return residentname;
    }

    public void setResidentname(String residentname) {
        this.residentname = residentname;
    }

    public String getDoorid() {
        return doorid;
    }

    public void setDoorid(String doorid) {
        this.doorid = doorid;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getHuman() {
        return human;
    }

    public void setHuman(String human) {
        this.human = human;
    }

    public String getSmoke() {
        return smoke;
    }

    public void setSmoke(String smoke) {
        this.smoke = smoke;
    }
}
