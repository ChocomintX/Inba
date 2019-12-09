package com.example.wx.inba.model;

import java.io.Serializable;

public class UserInfo implements Serializable {
    private int id;
    private String name;
    private String head;
    private int fansnum;
    private int focusnum;
    private int focusbanum;
    private int tienum;
    private String jianjie;
    private String sex;
    private String safety_problem;
    private String safety_answer;

    public UserInfo(int id, String name, String head, int fansnum, int focusnum, int focusbanum, int tienum) {
        this.id = id;
        this.name=name;
        this.head = head;
        this.fansnum = fansnum;
        this.focusnum = focusnum;
        this.focusbanum = focusbanum;
        this.tienum = tienum;
    }

    public UserInfo() {
    }

    public UserInfo(int id){
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public int getFansnum() {
        return fansnum;
    }

    public void setFansnum(int fansnum) {
        this.fansnum = fansnum;
    }

    public int getFocusnum() {
        return focusnum;
    }

    public void setFocusnum(int focusnum) {
        this.focusnum = focusnum;
    }

    public int getFocusbanum() {
        return focusbanum;
    }

    public void setFocusbanum(int focusbanum) {
        this.focusbanum = focusbanum;
    }

    public int getTienum() {
        return tienum;
    }

    public void setTienum(int tienum) {
        this.tienum = tienum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJianjie() {
        return jianjie;
    }

    public void setJianjie(String jianjie) {
        this.jianjie = jianjie;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSafety_problem() {
        return safety_problem;
    }

    public void setSafety_problem(String safety_problem) {
        this.safety_problem = safety_problem;
    }

    public String getSafety_answer() {
        return safety_answer;
    }

    public void setSafety_answer(String safety_answer) {
        this.safety_answer = safety_answer;
    }
}
