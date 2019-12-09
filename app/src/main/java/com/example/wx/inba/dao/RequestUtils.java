package com.example.wx.inba.dao;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class RequestUtils {
    public static AsyncHttpClient client = new AsyncHttpClient();

    public static void clientGet(String url, AsyncHttpResponseHandler cb) {
        client.get(Link.url+url, cb);
    }

    public static void clientPost(String url, RequestParams params, AsyncHttpResponseHandler cb) {
        client.post(Link.url+url, params, cb);
    }

}
