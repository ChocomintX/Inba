package com.example.wx.inba.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Answer implements Serializable, Comparable<Answer> {
    private int id;
    private int userid;
    private int tieid;
    private String content;
    private int inanswernum;
    private String imgpath;
    private String answertime;
    private int floor;
    private int thumbnum;

    public Answer(int userid, int tieid, String content, int inanswernum, String imgpath, String answertime, int floor, int thumbnum) {
        this.userid = userid;
        this.tieid = tieid;
        this.content = content;
        this.inanswernum = inanswernum;
        this.imgpath = imgpath;
        this.answertime = answertime;
        this.floor = floor;
        this.thumbnum = thumbnum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getTieid() {
        return tieid;
    }

    public void setTieid(int tieid) {
        this.tieid = tieid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getInanswernum() {
        return inanswernum;
    }

    public void setInanswernum(int inanswernum) {
        this.inanswernum = inanswernum;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public String getAnswertime() {
        return answertime;
    }

    public void setAnswertime(String answertime) {
        this.answertime = answertime;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getThumbnum() {
        return thumbnum;
    }

    public void setThumbnum(int thumbnum) {
        this.thumbnum = thumbnum;
    }

    @Override
    public int compareTo(Answer answer) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date dt1=df.parse(this.getAnswertime());
            Date dt2=df.parse(answer.answertime);
            if(dt1.getTime()>dt2.getTime()){
                return -1;
            }else {
                return 1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


}
