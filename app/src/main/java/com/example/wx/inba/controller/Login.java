package com.example.wx.inba.controller;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wx.inba.R;
import com.example.wx.inba.dao.Link;
import com.example.wx.inba.dao.RequestUtils;
import com.example.wx.inba.model.UserInfo;
import com.example.wx.inba.util.JsonUtil;
import com.example.wx.inba.util.KeyBoardUtil;
import com.example.wx.inba.util.StatusBarUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class Login extends Activity implements View.OnClickListener {
    private EditText logininfo_username,logininfo_password,logininfo_repass;
    private TextView logininfo_title,logininfo_register,logininfo_hint,logininfo_forget;
    private Button logininfo_login;
    private int flag=0;
    private RequestParams params;
    private String state;


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case -1:
                    Toast.makeText(Login.this,"连接服务器失败！",Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    if(state.equals("no")){
                        Toast.makeText(Login.this,"账号或密码错误！",Toast.LENGTH_SHORT).show();
                        logininfo_username.setText("");
                        logininfo_password.setText("");
                        KeyBoardUtil.hideKeyboard(Login.this);
                    }else{
                        String[] str=state.split("#flag#");
                        Link.user=JsonUtil.getUser(str[0]);
                        Link.userInfo=JsonUtil.getUserInfoJson(str[1]);
                        if(str[2].equals("yes")){
                            Link.fengjin=true;
                        }else{
                            Link.fengjin=false;
                        }
                        setResult(1);
                        finish();
                    }

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logininfo);

        initView();
    }

    public void initView(){
        StatusBarUtil.setBar(this);
        setResult(0);

        logininfo_username=(EditText)findViewById(R.id.logininfo_username);
        logininfo_password=(EditText)findViewById(R.id.logininfo_password);
        logininfo_repass=(EditText)findViewById(R.id.logininfo_repass);
        logininfo_title=(TextView)findViewById(R.id.logininfo_title) ;
        logininfo_register=(TextView)findViewById(R.id.logininfo_register) ;
        logininfo_register.setOnClickListener(this);
        logininfo_hint=(TextView)findViewById(R.id.logininfo_hint) ;
        logininfo_login=(Button)findViewById(R.id.logininfo_login);
        logininfo_login.setOnClickListener(this);
        logininfo_forget=(TextView)findViewById(R.id.logininfo_forget) ;
        logininfo_forget.setOnClickListener(this);

        logininfo_repass.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        overridePendingTransition(R.anim.fade_out,R.anim.fade_in);
        super.onPause();
    }

    public void login(){
        if(logininfo_username.getText().toString().equals("")){
            Toast.makeText(Login.this,"账号为空！",Toast.LENGTH_SHORT).show();
            KeyBoardUtil.hideKeyboard(Login.this);
            return;
        }else if(logininfo_password.getText().toString().equals("")){
            Toast.makeText(Login.this,"密码为空！",Toast.LENGTH_SHORT).show();
            KeyBoardUtil.hideKeyboard(Login.this);
            return;
        }

        params=new RequestParams();
        params.put("username",logininfo_username.getText().toString());
        params.put("password",logininfo_password.getText().toString());
        RequestUtils.clientPost("login", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                state=new String(responseBody);
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.sendEmptyMessage(-1);
            }
        });
    }

    public void register(){

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.logininfo_register:
                if(flag==0){
                    logininfo_repass.setVisibility(View.VISIBLE);
                    logininfo_register.setText("返回登录");
                    logininfo_hint.setText("注册即可浏览所有帖子！");
                    logininfo_title.setText("注册账号");
                    logininfo_login.setText("注册账号");
                    flag=1;
                }else{
                    logininfo_repass.setVisibility(View.GONE);
                    logininfo_register.setText("注册");
                    logininfo_hint.setText("没有账号?");
                    logininfo_title.setText("登录到INBA");
                    logininfo_login.setText("登录");
                    flag=0;
                }
                break;
            case R.id.logininfo_login:
                if(flag==0){
                    login();
                }else{
                    register();
                }
                break;
        }
    }
}
