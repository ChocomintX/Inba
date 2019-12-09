package com.example.wx.inba.model;

public class Report {
    private String type;
    private int tieid;
    private String reason;
    private int userid;
    private String reporttime;

    public Report() {
    }

    public Report(String type, int tieid, String reason, int userid, String reporttime) {
        this.type = type;
        this.tieid = tieid;
        this.reason = reason;
        this.userid = userid;
        this.reporttime = reporttime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTieid() {
        return tieid;
    }

    public void setTieid(int tieid) {
        this.tieid = tieid;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getReporttime() {
        return reporttime;
    }

    public void setReporttime(String reporttime) {
        this.reporttime = reporttime;
    }
}
