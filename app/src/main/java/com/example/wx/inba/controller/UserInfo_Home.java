package com.example.wx.inba.controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wx.inba.R;
import com.example.wx.inba.dao.Link;
import com.example.wx.inba.dao.RequestUtils;
import com.example.wx.inba.model.Ba;
import com.example.wx.inba.model.InAnswer;
import com.example.wx.inba.model.Tie;
import com.example.wx.inba.model.User;
import com.example.wx.inba.model.UserInfo;
import com.example.wx.inba.util.JsonUtil;
import com.example.wx.inba.util.ListViewUtil;
import com.example.wx.inba.util.ShouyeAdapter;
import com.example.wx.inba.util.StatusBarUtil;
import com.example.wx.inba.util.fengjinPopup;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.makeramen.roundedimageview.RoundedImageView;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class UserInfo_Home extends Activity implements ShouyeAdapter.CallBack, View.OnClickListener {
    private ListView lv;
    private List<Tie> tieList;
    private List<Ba> baList;
    private List<UserInfo> userInfoList;
    private ShouyeAdapter adapter;
    private ImageView userinfo_return;
    private TextView fans,focus,ba,name,jianjie,edit,title,fengjin;
    private User user;
    private UserInfo userinfo;
    private RoundedImageView head;
    private boolean isfocus=false;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case  -1:
                    Toast.makeText(UserInfo_Home.this,"连接服务器失败！",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    adapter=new ShouyeAdapter(tieList,baList,userInfoList,UserInfo_Home.this,UserInfo_Home.this);
                    lv.setAdapter(adapter);
                    ListViewUtil.setListViewBasedOnChildren(lv);

                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent=new Intent(UserInfo_Home.this,Tie_Home.class);
                            intent.putExtra("tie",(Serializable)tieList.get(i));
                            startActivity(intent);
                        }
                    });
                    break;
                case 2:
                    Glide.with(UserInfo_Home.this)
                            .load(userinfo.getHead())
                            .error(R.drawable.test)
                            .into(head);
                    title.setText(userinfo.getName()+"的主页");
                    name.setText(userinfo.getName());
                    fans.setText(userinfo.getFansnum()+" 粉丝");
                    focus.setText(userinfo.getFocusnum()+" 关注");
                    ba.setText(userinfo.getFocusbanum()+" 吧");
                    jianjie.setText("个人介绍: "+userinfo.getJianjie());
                    checkfocus();
                    if(userinfo.getId()!=1&&Link.userInfo.getId()==1){
                        fengjin.setVisibility(View.VISIBLE);
                    }else{
                        fengjin.setVisibility(View.GONE);
                    }
                    break;
                case 3:
                    edit.setVisibility(View.VISIBLE);
                    if(!isfocus)
                        edit.setText("关注");
                    else
                        edit.setText("取消关注");

                    if(Link.userInfo.getId()==userinfo.getId())
                        edit.setText("编辑资料");

                    setbtnClick();
                    break;
                case 4:
                    if(edit.getText().toString().equals("关注")){
                        Toast.makeText(UserInfo_Home.this,"关注成功!",Toast.LENGTH_SHORT).show();
                        edit.setText("取消关注");
                    }else{
                        Toast.makeText(UserInfo_Home.this,"取消关注成功!",Toast.LENGTH_SHORT).show();
                        edit.setText("关注");
                    }

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinfo);

        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==0){
            getinfo();
        }
    }

    public void initView(){
        StatusBarUtil.setRootViewFitsSystemWindows(this,true);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
//        StatusBarUtil.setStatusBarColor(this, Color.parseColor("#2a2a2a"));

        userinfo_return=(ImageView)findViewById(R.id.userinfo_return);
        userinfo_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInfo_Home.this.setResult(2);
                finish();
        }
        });

        head=(RoundedImageView) findViewById(R.id.userinfo_head);

        name=(TextView)findViewById(R.id.userinfo_name);
        title=(TextView)findViewById(R.id.userinfo_titlename);
        fans=(TextView)findViewById(R.id.userinfo_fans);
        fans.setOnClickListener(this);
        focus=(TextView)findViewById(R.id.userinfo_focus);
        focus.setOnClickListener(this);
        ba=(TextView)findViewById(R.id.userinfo_ba);
        ba.setOnClickListener(this);
        jianjie=(TextView)findViewById(R.id.userinfo_jianjie);
        edit=(TextView)findViewById(R.id.userinfo_btn);
        edit.setOnClickListener(this);
        fengjin=(TextView)findViewById(R.id.userinfo_fengjin);
        fengjin.setOnClickListener(this);


        edit.setVisibility(View.GONE);
        lv=(ListView)findViewById(R.id.userinfo_lv);


        getinfo();


    }

    public void getinfo(){
        RequestParams params=new RequestParams();
        params.put("id",getIntent().getIntExtra("id",1));

        RequestUtils.clientPost("getuserinfo", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String str=new String(responseBody);
                userinfo=JsonUtil.getUserInfoJson(str);
                handler.sendEmptyMessage(2);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.sendEmptyMessage(-1);
            }
        });

        RequestUtils.clientPost("gettiebyuserid", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String[] str=new String(responseBody).split("#flag#");
                tieList= JsonUtil.getTieJson(str[0]);
                baList=JsonUtil.getBaJson(str[1]);
                userInfoList=JsonUtil.getUserInfoListJson(str[2]);
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.sendEmptyMessage(-1);
            }
        });
    }

    public void checkfocus(){
        RequestParams params1=new RequestParams();
        params1.put("id",Link.user.getId());
        RequestUtils.clientPost("selectfocususerbyid", params1, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                List<UserInfo> userInfoList=JsonUtil.getUserInfoListJson(new String(responseBody));
                for(UserInfo u:userInfoList){
                    if(u.getId()==userinfo.getId()){
                        isfocus=true;
                    }
                }
                handler.sendEmptyMessage(3);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.sendEmptyMessage(-1);
            }
        });
    }

    public void setbtnClick(){
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestParams params=new RequestParams();
                params.put("focusid",userinfo.getId());
                params.put("fansid",Link.userInfo.getId());
                if(edit.getText().toString().equals("编辑资料")){
                    Toast.makeText(UserInfo_Home.this,"编辑资料",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(UserInfo_Home.this,EditInfo.class);
                    intent.putExtra("userinfo",(Serializable)userinfo);
                    startActivityForResult(intent,0);
                    return;
                }
                else if(edit.getText().toString().equals("关注")){
                    params.put("type","add");
                } else if(edit.getText().toString().equals("取消关注")){
                    params.put("type","remove");
                }

                RequestUtils.clientPost("editfocus", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        handler.sendEmptyMessage(4);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        handler.sendEmptyMessage(-1);
                    }
                });

            }
        });
    }


    public void fj(final int time){
        RequestParams params=new RequestParams();
        params.put("id",userinfo.getId());
        params.put("time",time);
        RequestUtils.clientPost("fengjin", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(time==-1){
                    Toast.makeText(UserInfo_Home.this,"解除封禁成功！",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(UserInfo_Home.this,"封禁成功！",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.sendEmptyMessage(-1);
            }
        });
    }

    @Override
    public void Click(View v) {
        switch (v.getId()){
            case  R.id.userinfo_btn:

                break;

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.userinfo_fengjin:
                new XPopup.Builder(this)
                        .asCenterList("请选择操作", new String[]{"封禁1天", "封禁7天", "封禁30天", "取消封禁"},
                                new OnSelectListener() {
                                    @Override
                                    public void onSelect(int position, String text) {
                                        switch (position){
                                            case 0:
                                                fj(1);
                                                break;
                                            case 1:
                                                fj(7);
                                                break;
                                            case 2:
                                                fj(30);
                                                break;
                                            case 3:
                                                fj(-1);
                                                break;
                                        }
                                    }
                                })
                        .show();
//                new XPopup.Builder(this)
//                        .asCustom(new fengjinPopup(this))
//                        .show();
                break;
        }
    }
}
