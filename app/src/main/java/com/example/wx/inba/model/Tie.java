package com.example.wx.inba.model;

import com.example.wx.inba.dao.Link;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class Tie implements Serializable, Comparable<Tie>{
    int id,baid,userid,thumbnum,answernum;
    String title,content,settime,answertime;
    String img1;
    String img2;
    String img3;
    String video;

    public Tie() {
        this.settime="";
        this.answertime="";
        this.img1="";
        this.img2="";
        this.img3="";
        this.video="";
    }

    public Tie(int id, int baid, int userid, int thumbnum, int answernum, String title, String content, String settime, String answertime, String img1, String img2, String img3, String video) {
        this.id = id;
        this.baid = baid;
        this.userid = userid;
        this.thumbnum = thumbnum;
        this.answernum = answernum;
        this.title = title;
        this.content = content;
        this.settime = settime;
        this.answertime = answertime;
        this.img1 = img1;
        this.img2 = img2;
        this.img3 = img3;
        this.video = video;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBaid() {
        return baid;
    }

    public void setBaid(int baid) {
        this.baid = baid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getThumbnum() {
        return thumbnum;
    }

    public void setThumbnum(int thumbnum) {
        this.thumbnum = thumbnum;
    }

    public int getAnswernum() {
        return answernum;
    }

    public void setAnswernum(int answernum) {
        this.answernum = answernum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSettime() {
        return settime;
    }

    public void setSettime(String settime) {
        this.settime = settime;
    }

    public String getAnswertime() {
        return answertime;
    }

    public void setAnswertime(String answertime) {
        this.answertime = answertime;
    }

    public String getImg1() {
        if(img1==null||img1.equals(""))
            return img1;

        if(img1.substring(0,4).equals("http"))
            return img1;
        return Link.url+img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        if(img2==null||img2.equals(""))
            return img2;

        if(img2.substring(0,4).equals("http"))
            return img2;
        return Link.url+img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        if(img3==null||img3.equals(""))
            return img3;

        if(img3.substring(0,4).equals("http"))
            return img3;
        return Link.url+img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    @Override
    public int compareTo(Tie tie) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date dt1=df.parse(this.getAnswertime());
            Date dt2=df.parse(tie.answertime);
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
