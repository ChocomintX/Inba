package com.example.wx.inba.dao;

import android.preference.PreferenceActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.wx.inba.model.Ba;
import com.example.wx.inba.model.User;
import com.example.wx.inba.model.UserInfo;
import com.example.wx.inba.util.JsonUtil;
import com.example.wx.inba.util.MyListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class Link {
    public static User user;
    public static UserInfo userInfo;
    public static int i=-1;
    public static String s="1";
    public static String url = "http://10.65.41.82:8080/InbaServer/";
    public static boolean fengjin=false;
    private static boolean isadmin=false;


    public static void getuser(){

        RequestParams params = new RequestParams();
        params.put("id", "1");

        RequestUtils.clientPost(Link.url + "selectuserbyid", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                user=JsonUtil.getUser(new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Link.s="连接失败";
            }
        });

        RequestParams params2 = new RequestParams();
        List<UserInfo> list=new ArrayList<UserInfo>();
        list.add(new UserInfo(1));
        params.put("id", JsonUtil.toIdSetJson(list));

        RequestUtils.clientPost(Link.url + "selectuserinfobyid", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                userInfo=JsonUtil.getIdSetJson(new String(responseBody)).get(0);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Link.s="连接失败";
            }
        });

    }



}
