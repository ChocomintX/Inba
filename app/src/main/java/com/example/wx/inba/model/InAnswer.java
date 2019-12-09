package com.example.wx.inba.model;

public class InAnswer {
    private int id,answerid,inanswerid,inanswertoid;
    private String inanswertime;
    private String content;
    private int floor;

    public InAnswer(int id, int answerid, int inanswerid, int inanswertoid, String inanswertime,String content,int floor) {
        this.id = id;
        this.answerid = answerid;
        this.inanswerid = inanswerid;
        this.inanswertoid = inanswertoid;
        this.inanswertime = inanswertime;
        this.content=content;
        this.floor=floor;
    }

    public InAnswer(int answerid, int inanswerid, int inanswertoid, String inanswertime,String content,int floor) {
        this.answerid = answerid;
        this.inanswerid = inanswerid;
        this.inanswertoid = inanswertoid;
        this.inanswertime = inanswertime;
        this.content=content;
        this.floor=floor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnswerid() {
        return answerid;
    }

    public void setAnswerid(int answerid) {
        this.answerid = answerid;
    }

    public int getInanswerid() {
        return inanswerid;
    }

    public void setInanswerid(int inanswerid) {
        this.inanswerid = inanswerid;
    }

    public int getInanswertoid() {
        return inanswertoid;
    }

    public void setInanswertoid(int inanswertoid) {
        this.inanswertoid = inanswertoid;
    }

    public String getInanswertime() {
        return inanswertime;
    }

    public void setInanswertime(String inanswertime) {
        this.inanswertime = inanswertime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
