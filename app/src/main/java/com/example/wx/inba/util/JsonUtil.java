package com.example.wx.inba.util;

import java.lang.reflect.Type;
import java.util.List;

import com.example.wx.inba.model.Answer;
import com.example.wx.inba.model.Ba;
import com.example.wx.inba.model.InAnswer;
import com.example.wx.inba.model.Report;
import com.example.wx.inba.model.Tie;
import com.example.wx.inba.model.User;
import com.example.wx.inba.model.UserInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonUtil {
	private static Gson gson;

	public static User getUser(String jsonStr){
		gson=new Gson();
		Type listType = new TypeToken<User>() {
		}.getType();
		return  gson.fromJson(jsonStr,listType);
	}
	
	public static List<Ba> getBaJson(String jsonStr) {
		gson=new Gson();
		Type listType = new TypeToken<List<Ba>>() {
		}.getType();
		return gson.fromJson(jsonStr,listType);
	}

	public static Ba getBaJson1(String jsonStr) {
		gson=new Gson();
		Type listType = new TypeToken<Ba>() {
		}.getType();
		return gson.fromJson(jsonStr,listType);
	}

    public static List<Tie> getTieJson(String jsonStr) {
        gson=new Gson();
        Type listType = new TypeToken<List<Tie>>() {
        }.getType();
        return gson.fromJson(jsonStr,listType);
    }

    public static String toIdSetJson(List<UserInfo> list){
		gson=new Gson();
		return gson.toJson(list);
	}

	public static List<UserInfo> getIdSetJson(String jsonStr) {
		gson=new Gson();
		Type listType = new TypeToken<List<UserInfo>>() {
		}.getType();
		return gson.fromJson(jsonStr,listType);
	}

	public static UserInfo getUserInfoJson(String jsonStr){
		gson=new Gson();
		Type listType = new TypeToken<UserInfo>() {
		}.getType();
		return gson.fromJson(jsonStr,listType);
	}

	public static List<UserInfo> getUserInfoListJson(String jsonStr){
		gson=new Gson();
		Type listType = new TypeToken<List<UserInfo>>() {
		}.getType();
		return gson.fromJson(jsonStr,listType);
	}

	public static List<Answer> getAnswerJson(String jsonStr){
		gson=new Gson();
		Type listType = new TypeToken<List<Answer>>() {
		}.getType();
		return gson.fromJson(jsonStr,listType);
	}

	public static List<InAnswer> getInAnswerJson(String jsonStr){
		gson=new Gson();
		Type listType = new TypeToken<List<InAnswer>>() {
		}.getType();
		return gson.fromJson(jsonStr,listType);
	}

	public static List<Report> getReportJson(String jsonStr){
		gson=new Gson();
		Type listType = new TypeToken<List<Report>>() {
		}.getType();
		return gson.fromJson(jsonStr,listType);
	}

	public static String toAnswerJson(Answer answer){
		gson=new Gson();
		return gson.toJson(answer);
	}

	public static String toTieJson(Tie tie){
		gson=new Gson();
		return gson.toJson(tie);
	}

	public static String toInAnswerJson(InAnswer inAnswer){
		gson=new Gson();
		return gson.toJson(inAnswer);
	}

	public static String toUserInfoJson(UserInfo userInfo){
		gson=new Gson();
		return gson.toJson(userInfo);
	}

    public static String toReportJson(Report report){
        gson=new Gson();
        return gson.toJson(report);
    }

	public static String toBaJson(Ba ba){
		gson=new Gson();
		return gson.toJson(ba);
	}
}
