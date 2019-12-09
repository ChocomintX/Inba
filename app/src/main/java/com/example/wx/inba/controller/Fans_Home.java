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
import com.example.wx.inba.model.UserInfo;
import com.example.wx.inba.util.FansHomeAdapter;
import com.example.wx.inba.util.FocusHomeAdapter;
import com.example.wx.inba.util.JsonUtil;
import com.example.wx.inba.util.StatusBarUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class Fans_Home extends Activity implements FansHomeAdapter.CallBack {
    private ListView lv;
    private List<UserInfo> list;
    private FansHomeAdapter adapter;
    private ImageView back;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case -1:
                    Toast.makeText(Fans_Home.this,"连接失败!",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    adapter=new FansHomeAdapter(list,Fans_Home.this,Fans_Home.this);
                    lv.setAdapter(adapter);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fans_home);
        initView();
    }



    public void initView(){
        StatusBarUtil.setRootViewFitsSystemWindows(this,true);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setStatusBarColor(this, Color.parseColor("#2a2a2a"));

        lv=(ListView)findViewById(R.id.fans_home_lv);
        list=new ArrayList<UserInfo>();
        back=(ImageView)findViewById(R.id.fans_home_return);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        RequestParams params=new RequestParams();
        params.put("id", Link.user.getId());
        RequestUtils.clientPost("selectfansuserbyid", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                list= JsonUtil.getUserInfoListJson(new String(responseBody));
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.sendEmptyMessage(-1);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int id=list.get(i).getId();
                Intent intent=new Intent(Fans_Home.this,UserInfo_Home.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
    }



    @Override
    public void Click(View v) {

    }
}
