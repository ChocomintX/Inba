package com.example.wx.inba.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.bumptech.glide.Glide;
import com.example.wx.inba.R;
import com.example.wx.inba.dao.Link;
import com.example.wx.inba.dao.RequestUtils;
import com.example.wx.inba.model.Ba;
import com.example.wx.inba.model.Tie;
import com.example.wx.inba.model.User;
import com.example.wx.inba.model.UserInfo;
import com.example.wx.inba.util.BaHomeAdapter;
import com.example.wx.inba.util.Ba_Home_Dialog;
import com.example.wx.inba.util.HeightListView;
import com.example.wx.inba.util.JsonUtil;
import com.example.wx.inba.util.ListViewUtil;
import com.example.wx.inba.util.MyListView;
import com.example.wx.inba.util.StatusBarUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.content.ContentValues.TAG;

public class Ba_Home extends AppCompatActivity implements View.OnClickListener,BaHomeAdapter.CallBack, ActionSheet.ActionSheetListener {
    private BaHomeAdapter baHomeAdapter;
    private List<Tie> list;
    private List<UserInfo> userInfoList;
    private ListView ba_home_listview;
    private ImageView ba_home_return,ba_home_tixing,ba_home_gengduo;
    private TextView ba_home_name,ba_home_toptitle;
    private Ba ba;
    private RoundedImageView ba_home_head;
    private Tie toptie;
    private UserInfo topuserinfo;
    private LinearLayout ba_home_refresh,ba_home_add;
    private ScrollView ba_home_scrollview;
    private Dialog dialog;
    private ProgressBar ba_home_exp;
    private TextView ba_home_lelvel;
    private double exp;
    private Button ba_home_sign;
    private String isSign;
    private int flag=0;
    private String expstr;

    private Handler handler=
            new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case -1:
                    Toast.makeText(Ba_Home.this,"连接服务器失败！",Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    getTieUser();
                    break;
                case 1:
                    baHomeAdapter=new BaHomeAdapter(list,userInfoList,Ba_Home.this,Ba_Home.this);
                    ba_home_listview.setAdapter(baHomeAdapter);
                    ListViewUtil.setListViewBasedOnChildren(ba_home_listview);
                    for (int i=0;i<list.size();i++){
                        Tie tie=list.get(i);
                        if(tie.getId()==ba.getTopid()){
                            if(tie.getTitle().length()<16){
                                ba_home_toptitle.setText(tie.getTitle());
                            }else{
                                ba_home_toptitle.setText(tie.getTitle().substring(0,15)+"......");
                            }
                            toptie=tie;
                            topuserinfo=userInfoList.get(i);
                        }
                    }
                    if(flag!=0){
                        ba_home_scrollview.fullScroll(ScrollView.FOCUS_UP);
                    }
                    flag=1;
                    break;
                case 2:
//                    if(Link.fengjin)
//                        break;
                    showDialog();
                    break;
                case 3:
                    double progress=0;

                    if(exp>=0&&exp<10){
                        ba_home_lelvel.setText("LV1 初级用户");
                        progress=exp/10*100;
                        expstr=(int)exp+"/"+"10";
                    }

                    if(exp>=10&&exp<100){
                        ba_home_lelvel.setText("LV2 中级用户");
                        progress=exp/100*100;
                        expstr=(int)exp+"/"+"100";
                    }

                    if(exp>=100&&exp<1000){
                        ba_home_lelvel.setText("LV3 高级用户");
                        progress=exp/1000*100;
                        expstr=(int)exp+"/"+"1000";
                    }

                    if(exp>=1000&&exp<10000){
                        ba_home_lelvel.setText("LV4 强无敌");
                        progress=exp/10000*100;
                        expstr=(int)exp+"/"+"10000";
                    }
                    ba_home_exp.setProgress((int)progress);
                    ba_home_exp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new XPopup.Builder(Ba_Home.this)
                                    .atView(ba_home_exp)  // 依附于所点击的View，内部会自动判断在上方或者下方显示
                                    .hasShadowBg(false)
                                    .asAttachList(new String[]{expstr},
                                            new int[]{},
                                            new OnSelectListener() {
                                                @Override
                                                public void onSelect(int position, String text) {
                                                }
                                            })
                                    .show();
                        }
                    });
                    break;
                case 4:
                    if(isSign.equals("true")){
                        Toast.makeText(Ba_Home.this,"签到成功!",Toast.LENGTH_SHORT).show();
                        ba_home_sign.setText("已签到");
                    }else{
                        Toast.makeText(Ba_Home.this,"签到失败!今日已签到",Toast.LENGTH_SHORT).show();
                        ba_home_sign.setText("已签到");
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ba_home);

        initView();

    }


    public void initView(){
        StatusBarUtil.setRootViewFitsSystemWindows(this,true);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setStatusBarColor(this,Color.parseColor("#587177"));

        ba=(Ba)getIntent().getSerializableExtra("ba");
        ba_home_exp=(ProgressBar)findViewById(R.id.ba_home_exp);
        ba_home_lelvel=(TextView)findViewById(R.id.ba_home_level);
        ba_home_sign=(Button)findViewById(R.id.ba_home_sign);
        ba_home_sign.setOnClickListener(this);
        ba_home_name=(TextView)findViewById(R.id.ba_home_name);
        ba_home_name.setText(ba.getName());
        ba_home_head=(RoundedImageView)findViewById(R.id.ba_home_head);
        if (ba.getImgpath()!=null){
            Glide.with(Ba_Home.this).load(ba.getImgpath()).into(ba_home_head);
            Toast.makeText(this,"1",Toast.LENGTH_SHORT).show();
        }
        ba_home_toptitle=(TextView)findViewById(R.id.ba_home_toptitle);
        ba_home_toptitle.setOnClickListener(this);

        ba_home_return=(ImageView)findViewById(R.id.ba_home_return);
        ba_home_tixing=(ImageView)findViewById(R.id.ba_home_tixing);
        ba_home_gengduo=(ImageView)findViewById(R.id.ba_home_gengduo);
        ba_home_return.setOnClickListener(this);
        ba_home_tixing.setOnClickListener(this);
        ba_home_gengduo.setOnClickListener(this);
        ba_home_refresh=(LinearLayout)findViewById(R.id.ba_home_refresh);
        ba_home_add=(LinearLayout)findViewById(R.id.ba_home_add);
        ba_home_refresh.setOnClickListener(this);
        ba_home_add.setOnClickListener(this);
        ba_home_scrollview=(ScrollView)findViewById(R.id.ba_home_scroll);

        ba_home_listview=(ListView) findViewById(R.id.ba_home_listview);
        list=new ArrayList<Tie>();

        getTie();
        getSignState();

        ba_home_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(Ba_Home.this,Tie_Home.class);
                intent.putExtra("tie",(Serializable)list.get(i));
                intent.putExtra("userinfo",(Serializable)userInfoList.get(i));
                intent.putExtra("ba",(Serializable)ba);
                startActivityForResult(intent,2);
            }
        });
    }

    public void getSignState(){
        RequestParams params = new RequestParams();
        params.put("baid", ba.getId());
        params.put("userid",Link.userInfo.getId());

        RequestUtils.clientPost("getexp", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                exp=Double.parseDouble(new String(responseBody));
                handler.sendEmptyMessage(3);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.sendEmptyMessage(-1);
            }
        });
    }

    public void sign(){
        RequestParams params = new RequestParams();
        params.put("baid", ba.getId());
        params.put("userid",Link.userInfo.getId());

        RequestUtils.clientPost("basign", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                isSign=new String(responseBody);
                handler.sendEmptyMessage(4);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.sendEmptyMessage(-1);
            }
        });
    }

    public void getTie(){
        RequestParams params = new RequestParams();
        params.put("baid", ba.getId());

        RequestUtils.clientPost("gettie", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                list=JsonUtil.getTieJson(new String(responseBody));
                Collections.sort(list);
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.sendEmptyMessage(-1);
            }
        });
    }

    public void getTieUser(){
        RequestParams params = new RequestParams();
        userInfoList=new ArrayList<UserInfo>();
        for(Tie tie:list){
            userInfoList.add(new UserInfo(tie.getUserid(),"","",1,1,1,1));
        }

        params.put("id",JsonUtil.toIdSetJson(userInfoList));

        RequestUtils.clientPost("selectuserinfobyid", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                userInfoList=JsonUtil.getIdSetJson(new String(responseBody));
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.sendEmptyMessage(-1);
            }
        });
    }

    @Override
    protected void onPause() {
        overridePendingTransition(R.anim.fade_out,R.anim.fade_in);
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode){
            case 0:
                if(resultCode==1){
                    dialog.dismiss();
                    Combo(-1);
                    Toast.makeText(this,"发表成功！赶快叫朋友来围观吧！",Toast.LENGTH_SHORT).show();
                    break;
                }

        }

        getTie();
        updateBa();
    }

    public void updateBa(){
        RequestParams params = new RequestParams();

        params.put("baid",ba.getId());

        RequestUtils.clientPost("getbabyid", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                ba=JsonUtil.getBaJson1(new String(responseBody));
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.sendEmptyMessage(-1);
            }
        });
    }

    public void Combo(int i) {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int screen_x = metric.widthPixels/2-(ba_home_add.getWidth()/2+35);     // 屏幕宽度（像素）

        Animation translateAnimation=null;
        if (i==1){
            translateAnimation = new TranslateAnimation(0,-screen_x,0,0);//平移动画  从0,0,平移到100,100
        }else {
            translateAnimation = new TranslateAnimation(-screen_x,0,0,0);//平移动画  从0,0,平移到100,100
        }

        translateAnimation.setDuration(1000);//动画持续的时间为1.5s

        //如果不添加setFillEnabled和setFillAfter则动画执行结束后会自动回到远点
        translateAnimation.startNow();//动画开始执行 放在最后即可

        // 0表示从0度开始,360表示旋转360度
        // Animation.RELATIVE_TO_SELF, 0.5f表示绕着自己的中心点进行动画
        RotateAnimation rotateAni=null;
        if(i==1){
            rotateAni = new RotateAnimation(0, -225,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.5f);
        }else {
            rotateAni = new RotateAnimation(-225, 0,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.5f);
        }

        // 设置动画完成的时间（速度），单位是毫秒，1000是1秒内完成动画
        rotateAni.setDuration(1000);

        // 设置动画重复次数
        // -1或者Animation.INFINITE表示无限重复，正数表示重复次数，0表示不重复只播放一次
        rotateAni.setRepeatCount(0);

        // 设置动画模式（Animation.REVERSE设置循环反转播放动画,Animation.RESTART每次都从头开始）
        rotateAni.setRepeatMode(Animation.REVERSE);

        AnimationSet aset=new AnimationSet(true);


        // 将缩放动画和旋转动画放到动画插值器
        AnimationSet as = new AnimationSet(false);

        as.addAnimation(rotateAni);
        as.addAnimation(translateAnimation);
        as.setFillBefore(true);
        as.setFillAfter(true);
        // 启动动画
        ba_home_add.startAnimation(as);
        if(i==1){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(800);
                        handler.sendEmptyMessage(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }

    public void showDialog(){
        final Intent intent=new Intent(this,AddTie.class);
        intent.putExtra("ba",(Serializable)ba);
        dialog = new Ba_Home_Dialog(this, R.layout.ba_home_adddialog, true, true, new Ba_Home_Dialog.mCallBack() {
            @Override
            public void mClick(View v) {
                switch (v.getId()){
                    case R.id.ba_home_add_exit:
                        dialog.dismiss();
                        Combo(-1);
                        break;
                    case R.id.ba_home_add_text:
                        Toast.makeText(Ba_Home.this,"文字帖",Toast.LENGTH_SHORT).show();
                        intent.putExtra("type","text");
                        startActivityForResult(intent,0);
                        break;
                    case R.id.ba_home_add_img:
                        Toast.makeText(Ba_Home.this,"图片帖",Toast.LENGTH_SHORT).show();
                        intent.putExtra("type","img");
                        startActivityForResult(intent,0);
                        break;
                    case R.id.ba_home_add_video:
                        Toast.makeText(Ba_Home.this,"视频帖",Toast.LENGTH_SHORT).show();
                        intent.putExtra("type","video");
                        startActivityForResult(intent,0);
                        break;
                }

            }
        });
        //去掉标题线
        dialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.ba_home_adddialog);
        //背景透明
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 居中位置
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.MyDialog);  //添加动画
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ba_home_return:
                finish();
                break;
            case R.id.ba_home_tixing:
                Toast.makeText(this,"还没有新消息哦",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ba_home_gengduo:
                new XPopup.Builder(this)
                        .asBottomList("请选择操作", new String[]{"关注本吧", "分享本吧",},
                                new OnSelectListener() {
                                    @Override
                                    public void onSelect(int position, String text) {
                                    }
                                })
                        .show();
                break;
            case R.id.ba_home_toptitle:
                Intent intent=new Intent(Ba_Home.this,Tie_Home.class);
                intent.putExtra("tie",(Serializable)toptie);
//                intent.putExtra("userinfo",(Serializable)topuserinfo);
//                intent.putExtra("ba",(Serializable)ba);
                startActivity(intent);
                break;
            case R.id.ba_home_refresh:
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate);
                ba_home_refresh.startAnimation(animation);
                getTie();
                Toast.makeText(this,"刷新帖子",Toast.LENGTH_LONG).show();
                break;
            case R.id.ba_home_add:
                if(Link.fengjin){
                    Toast.makeText(Ba_Home.this,"您已被封禁，无法发帖！",Toast.LENGTH_SHORT).show();
                }else{
                    Combo(1);
                }
                break;
            case R.id.ba_home_sign:
                sign();
                break;
        }
    }

    @Override
    public void Click(View v) {
        int possition=Integer.parseInt(v.getTag()+"");
        View view=ba_home_listview.getChildAt(possition);
        TextView thumbnum=view.findViewById(R.id.ba_home_thumbnum);
        switch (v.getId()){
            case R.id.ba_home_zan:
                list.get(possition).setThumbnum(list.get(possition).getThumbnum()+1);
                thumbnum.setText(list.get(possition).getThumbnum()+"");
                break;
            case R.id.ba_home_cai:
                list.get(possition).setThumbnum(list.get(possition).getThumbnum()-1);
                thumbnum.setText(list.get(possition).getThumbnum()+"");
                break;
        }
    }


    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

    }

    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {

    }
}
