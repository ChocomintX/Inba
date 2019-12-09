package com.example.wx.inba.controller;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wx.inba.R;
import com.example.wx.inba.dao.RequestUtils;
import com.example.wx.inba.model.Ba;
import com.example.wx.inba.model.Tie;
import com.example.wx.inba.model.UserInfo;
import com.example.wx.inba.util.FansHomeAdapter;
import com.example.wx.inba.util.JsonUtil;
import com.example.wx.inba.util.MsgAdapter;
import com.example.wx.inba.util.StatusBarUtil;
import com.example.wx.inba.util.WodeTieAdapter;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class Wode_Tie_Home extends Activity implements WodeTieAdapter.CallBack {
    private ListView lv;
    private List<Tie> list;
    private List<Ba> balist;
    private ImageView back;
    private TextView zhutie,huitie,zhutie_gang,huitie_gang;
    private WodeTieAdapter adapter;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case -1:
                    Toast.makeText(Wode_Tie_Home.this,"连接失败!",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    adapter=new WodeTieAdapter(list, balist,Wode_Tie_Home.this,Wode_Tie_Home.this);
                    lv.setAdapter(adapter);
                    break;
                case 2:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wode_tie_home);
        initView();
    }

    public void initView(){
        StatusBarUtil.setRootViewFitsSystemWindows(this,true);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setStatusBarColor(this, Color.parseColor("#2a2a2a"));

        lv=(ListView)findViewById(R.id.wode_tie_home_lv);
        back=(ImageView)findViewById(R.id.wode_tie_home_return);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        zhutie=(TextView)findViewById(R.id.wode_tie_zhutie);
        zhutie_gang=(TextView)findViewById(R.id.wode_tie_zhutie_gang);
        zhutie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zhutie.setTextColor(getResources().getColor(R.color.color2));
                zhutie_gang.setVisibility(View.VISIBLE);
                huitie.setTextColor(getResources().getColor(R.color.color3));
                huitie_gang.setVisibility(View.GONE);
                getzhutie(getIntent().getIntExtra("id",1));
            }
        });

        huitie=(TextView)findViewById(R.id.wode_tie_huitie);
        huitie_gang=(TextView)findViewById(R.id.wode_tie_huitie_gang);
        huitie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zhutie.setTextColor(getResources().getColor(R.color.color3));
                zhutie_gang.setVisibility(View.GONE);
                huitie.setTextColor(getResources().getColor(R.color.color2));
                huitie_gang.setVisibility(View.VISIBLE);
            }
        });
        getzhutie(getIntent().getIntExtra("id",1));
    }

    public void getzhutie(int id){
        RequestParams params=new RequestParams();
        params.put("id",id);
        RequestUtils.clientPost("getmyzhutie", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                list= JsonUtil.getTieJson(new String(responseBody).split("#flag#")[0]);
                balist=JsonUtil.getBaJson(new String(responseBody).split("#flag#")[1]);
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.sendEmptyMessage(-1);
            }
        });
    }

    @Override
    public void Click(View v) {

    }
}
