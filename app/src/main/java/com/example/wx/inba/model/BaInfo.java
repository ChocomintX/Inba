package com.example.wx.inba.model;

public class BaInfo {
	
	private int userid,focusbaid,exp,days;
	private String signdate;

	public BaInfo() {
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getFocusbaid() {
		return focusbaid;
	}

	public void setFocusbaid(int focusbaid) {
		this.focusbaid = focusbaid;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public String getSigndate() {
		return signdate;
	}

	public void setSigndate(String signdate) {
		this.signdate = signdate;
	}
}