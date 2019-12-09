package com.example.wx.inba.controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wx.inba.R;
import com.example.wx.inba.dao.Link;
import com.example.wx.inba.dao.RequestUtils;
import com.example.wx.inba.model.Answer;
import com.example.wx.inba.model.InAnswer;
import com.example.wx.inba.model.UserInfo;
import com.example.wx.inba.util.HeightListView;
import com.example.wx.inba.util.InAnswerAdapter;
import com.example.wx.inba.util.JsonUtil;
import com.example.wx.inba.util.KeyBoardUtil;
import com.example.wx.inba.util.MyListView;
import com.example.wx.inba.util.StatusBarUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class InAnswer_Home extends Activity implements View.OnClickListener,InAnswerAdapter.CallBack {
    private InAnswerAdapter inAnswerAdapter;
    private List<InAnswer> list;
    private List<UserInfo> inAnswerUsers;
    private List<UserInfo> inAnswertoUsers;
    private HeightListView inanswer_listview;
    private ImageView inanswer_close;
    private UserInfo userInfo;
    private Answer answer;
    private RoundedImageView inanswer_head,inanswer_img;
    private ImageView inanswer_zan,inanswer_cai;
    private TextView inanswer_name,inanswer_settime,inanswer_content,inanswer_thumbnum,inanswer_num;
    private EditText inanswer_incontent;
    private Button inanswer_send;
    private int answertoid;


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case -1:
                    Toast.makeText(InAnswer_Home.this,"连接服务器失败！",Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    getInAnswerUser();
                    break;
                case 1:
                    inAnswerAdapter=new InAnswerAdapter(list,inAnswerUsers,inAnswertoUsers,InAnswer_Home.this,InAnswer_Home.this);
                    inanswer_listview.setAdapter(inAnswerAdapter);
                    break;
                case 2:
                    list.clear();
                    inAnswerUsers.clear();
                    inAnswertoUsers.clear();
                    inanswer_incontent.setText("");
                    getInanswer();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inanswer);

        initView();
    }

    public void initView() {
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setStatusBarColor(this, Color.parseColor("#2a2a2a"));

        inanswer_close=(ImageView)findViewById(R.id.inanswer_close);
        inanswer_close.setOnClickListener(this);
        inanswer_img=(RoundedImageView)findViewById(R.id.inanswer_img);
        inanswer_head=(RoundedImageView)findViewById(R.id.inanswer_head);
        inanswer_head.setOnClickListener(this);
        inanswer_zan=(ImageView)findViewById(R.id.inanswer_zan);
        inanswer_cai=(ImageView)findViewById(R.id.inanswer_cai);
        inanswer_name=(TextView)findViewById(R.id.inanswer_name);
        inanswer_settime=(TextView)findViewById(R.id.inanswer_settime);
        inanswer_content=(TextView)findViewById(R.id.inanswer_content);
        inanswer_thumbnum=(TextView)findViewById(R.id.inanswer_thumbnum);
        inanswer_num=(TextView)findViewById(R.id.inanswer_num);
        inanswer_listview=(HeightListView)findViewById(R.id.inanswer_listview);
        inanswer_incontent=(EditText)findViewById(R.id.inanswer_incontent);
        inanswer_send=(Button)findViewById(R.id.inanswer_send);
        inanswer_send.setOnClickListener(this);

        if(getIntent().getSerializableExtra("answer")==null){

        }else{
            answer=(Answer)getIntent().getSerializableExtra("answer");
            userInfo=(UserInfo)getIntent().getSerializableExtra("userinfo");
            answertoid=userInfo.getId();

            Glide.with(this).load(userInfo.getHead()).into(inanswer_head);
            Glide.with(this).load(answer.getImgpath()).into(inanswer_img);
            inanswer_name.setText(userInfo.getName());
            inanswer_settime.setText(answer.getAnswertime());
            inanswer_content.setText(answer.getContent());
            inanswer_thumbnum.setText(answer.getThumbnum()+"");
            inanswer_num.setText(answer.getInanswernum()+"条回复");

            getInanswer();
        }
    }

    @Override
    protected void onPause() {
        overridePendingTransition(R.anim.fade_out,R.anim.fade_in);
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.inanswer_close:
                finish();
                break;
            case R.id.inanswer_send:
                sendinanswer();
                break;
            case R.id.inanswer_head:
                Intent intent=new Intent(InAnswer_Home.this,UserInfo_Home.class);
                intent.putExtra("id",userInfo.getId());
                startActivity(intent);
                break;
        }
    }

    public void sendinanswer(){
        if(Link.fengjin){
            Toast.makeText(this,"您已被封禁，无法回复！",Toast.LENGTH_SHORT).show();
            return;
        }

        RequestParams params=new RequestParams();
        InAnswer inanswer=new InAnswer(answer.getId(),Link.user.getId(),answertoid,"",inanswer_incontent.getText()+"",list.size());
        params.put("inanswer",JsonUtil.toInAnswerJson(inanswer));
        RequestUtils.clientPost("addinanswer", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                handler.sendEmptyMessage(2);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.sendEmptyMessage(-1);
            }
        });
        KeyBoardUtil.hideKeyboard(this);
    }

    @Override
    public void Click(View v) {
        switch (v.getId()){
            case R.id.infloor_content:
                answertoid=Integer.parseInt(v.getTag()+"");
                break;
            case R.id.infloor_head:
                Intent intent=new Intent(InAnswer_Home.this,UserInfo_Home.class);
                int possition=Integer.parseInt(v.getTag()+"");
                intent.putExtra("id",inAnswerUsers.get(possition).getId());
                startActivity(intent);
                break;
        }
    }

    public void getInanswer(){
        RequestParams params=new RequestParams();
        params.put("answerid",answer.getId());

        RequestUtils.clientPost("getinanswer", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                list=JsonUtil.getInAnswerJson(new String(responseBody));
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.sendEmptyMessage(-1);
            }
        });
    }

    public void getInAnswerUser(){
        RequestParams params = new RequestParams();
        inAnswerUsers=new ArrayList<UserInfo>();
        for(InAnswer inAnswer:list){
            inAnswerUsers.add(new UserInfo(inAnswer.getInanswerid(),"","",1,1,1,1));
        }

        params.put("id",JsonUtil.toIdSetJson(inAnswerUsers));

        RequestUtils.clientPost("selectuserinfobyid", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                inAnswerUsers=JsonUtil.getIdSetJson(new String(responseBody));
                getInAnswertoUser();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.sendEmptyMessage(-1);
            }
        });
    }

    public void getInAnswertoUser(){
        RequestParams params = new RequestParams();
        inAnswertoUsers=new ArrayList<UserInfo>();
        for(InAnswer inAnswer:list){
            inAnswertoUsers.add(new UserInfo(inAnswer.getInanswertoid(),"","",1,1,1,1));
        }

        params.put("id",JsonUtil.toIdSetJson(inAnswertoUsers));

        RequestUtils.clientPost("selectuserinfobyid", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                inAnswertoUsers=JsonUtil.getIdSetJson(new String(responseBody));
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.sendEmptyMessage(-1);
            }
        });
    }


}
