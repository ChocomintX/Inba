package com.example.wx.inba.controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wx.inba.R;
import com.example.wx.inba.dao.Link;
import com.example.wx.inba.dao.RequestUtils;
import com.example.wx.inba.model.UserInfo;
import com.example.wx.inba.util.JsonUtil;
import com.example.wx.inba.util.StatusBarUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.makeramen.roundedimageview.RoundedImageView;

import cz.msebera.android.httpclient.Header;

public class EditInfo extends Activity {
    private TextView save, username, sex;
    private EditText name, jieshao;
    private ImageView edittinfo_return;
    private RoundedImageView head;
    private UserInfo userInfo;
    private boolean flag=false;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case -1:
                    Toast.makeText(EditInfo.this,"连接失败!",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(EditInfo.this,"修改成功!",Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editinfo);


        initView();
    }

    public void initView() {
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setStatusBarColor(this, Color.parseColor("#2a2a2a"));

        userInfo = (UserInfo) getIntent().getSerializableExtra("userinfo");
        save = (TextView) findViewById(R.id.editinfo_save);
        username = (TextView) findViewById(R.id.editinfo_username);
        name=(EditText) findViewById(R.id.editinfo_name);
        sex = (TextView) findViewById(R.id.editinfo_sex);
        jieshao = (EditText) findViewById(R.id.editinfo_jieshao);
        edittinfo_return = (ImageView) findViewById(R.id.editinfo_return);

        edittinfo_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        head = (RoundedImageView) findViewById(R.id.editinfo_head);

        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 1);
            }
        });

        Glide.with(this).load(userInfo.getHead()).error(R.drawable.test).into(head);
        username.setText(Link.user.getUsername());
        name.setText(userInfo.getName());
        sex.setText(userInfo.getSex());
        jieshao.setText(userInfo.getJianjie());

        sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sex.getText().equals("男"))
                    sex.setText("女");
                else if(sex.getText().equals("女"))
                    sex.setText("保密");
                else if(sex.getText().equals("保密"))
                    sex.setText("男");
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
    }

    public void submit(){
        RequestParams params=new RequestParams();

        userInfo.setName(name.getText().toString());
        userInfo.setSex(sex.getText().toString());
        userInfo.setJianjie(jieshao.getText().toString());


        params.put("userinfo", JsonUtil.toUserInfoJson(userInfo));

        RequestUtils.clientPost("edituserinfo", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.sendEmptyMessage(-1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (data != null) {
                    // 得到图片的全路径
                    Uri uri = data.getData();
                    head.setImageURI(uri);
                    flag=true;
                    break;
                }
        }
    }
}
