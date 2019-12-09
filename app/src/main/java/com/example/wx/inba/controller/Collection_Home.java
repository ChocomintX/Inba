package com.example.wx.inba.controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.wx.inba.R;
import com.example.wx.inba.dao.Link;
import com.example.wx.inba.dao.RequestUtils;
import com.example.wx.inba.model.Tie;
import com.example.wx.inba.util.CollectionAdapter;
import com.example.wx.inba.util.JsonUtil;
import com.example.wx.inba.util.StatusBarUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class Collection_Home extends Activity {
    private List<Tie> list;
    private CollectionAdapter adapter;
    private ListView lv;
    private ImageView collection_return;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case -1:
                    Toast.makeText(Collection_Home.this,"连接失败!",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    adapter=new CollectionAdapter(list,Collection_Home.this);
                    lv.setAdapter(adapter);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wode_collection);
        initView();
    }

    public void initView(){
        StatusBarUtil.setRootViewFitsSystemWindows(this,true);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setStatusBarColor(this, Color.parseColor("#2a2a2a"));

        lv=(ListView)findViewById(R.id.wode_collection_lv);
        collection_return=(ImageView)findViewById(R.id.wode_collection_return);
        collection_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(Collection_Home.this,Tie_Home.class);
                intent.putExtra("tie",(Serializable)list.get(i));
                startActivity(intent);
            }
        });
        getCollection();
    }

    public void getCollection(){
        RequestParams params = new RequestParams();
        params.put("type","get");
        params.put("userid", Link.user.getId());

        RequestUtils.clientPost("setcollection", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                list= JsonUtil.getTieJson(new String(responseBody));
                Collections.reverse(list);
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.sendEmptyMessage(-1);
            }
        });
    }
}
