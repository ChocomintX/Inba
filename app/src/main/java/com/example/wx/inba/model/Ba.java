package com.example.wx.inba.model;

import com.example.wx.inba.dao.Link;

import java.io.Serializable;

public class Ba implements Serializable {
    private int id,topid,peoplenum;
    private String name,imgpath;
    private int bazhuid;
    private String jianjie;

    public Ba(int id, int topid, int peoplenum, String name, String imgpath) {
        this.id = id;
        this.topid = topid;
        this.peoplenum = peoplenum;
        this.name = name;
        this.imgpath = imgpath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTopid() {
        return topid;
    }

    public void setTopid(int topid) {
        this.topid = topid;
    }

    public int getPeoplenum() {
        return peoplenum;
    }

    public void setPeoplenum(int peoplenum) {
        this.peoplenum = peoplenum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgpath() {
        if(imgpath.substring(0,4).equals("http"))
            return imgpath;
        return Link.url+imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public int getBazhuid() {
        return bazhuid;
    }

    public void setBazhuid(int bazhuid) {
        this.bazhuid = bazhuid;
    }

    public String getJianjie() {
        return jianjie;
    }

    public void setJianjie(String jianjie) {
        this.jianjie = jianjie;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "Ba[id="+id+",topid="+topid+",peoplenum="+peoplenum+",name="+name+",imgpath="+imgpath+"]";
    }
}
