package com.example.wx.inba.controller;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wx.inba.R;
import com.example.wx.inba.dao.Link;
import com.example.wx.inba.dao.RequestUtils;
import com.example.wx.inba.model.Answer;
import com.example.wx.inba.model.Ba;
import com.example.wx.inba.model.InAnswer;
import com.example.wx.inba.model.Tie;
import com.example.wx.inba.model.UserInfo;
import com.example.wx.inba.util.JinBaAdapter;
import com.example.wx.inba.util.JsonUtil;
import com.example.wx.inba.util.ListViewUtil;
import com.example.wx.inba.util.MsgAdapter;
import com.example.wx.inba.util.MyListView;
import com.example.wx.inba.util.NoScrollViewPager;
import com.example.wx.inba.util.ShouyeAdapter;
import com.example.wx.inba.util.StatusBarUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends Activity implements ShouyeAdapter.CallBack {
    private NoScrollViewPager viewPager;
    private View view_shouye,view_jinba,view_xiaoxi,view_wode,view_login;
    private List<View> viewList;
    private LinearLayout btn_shouye,btn_jinba,btn_xiaoxi,btn_wode;
    private JinBaAdapter jinBaAdapter;
    private List<Ba> balist;
    private MyListView lv_jinba,lv_message;
    private ListView lv_shouye;
    private ShouyeAdapter shouyeAdapter;
    private List<Tie> tieList;
    private MsgAdapter msgAdapter;
    private List<InAnswer> msgList;
    private List<UserInfo> shouye_userinfo;
    private List<Ba> shouye_ba;
    private Button login_tologininfo;
    private RoundedImageView wode_head;
    private Button wode_exit;
    private TextView wode_name,wode_focusnum,wode_fansnum,wode_focusbanum,wode_tienum;
    private RelativeLayout wode_collection,wode_message,wode_safety,wode_report;
    private RelativeLayout wode_user;
    private String[] msgstr;
    List<InAnswer> msg_inanswers;
    List<UserInfo> msg_userInfos;
    List<Ba> msg_bas;
    List<Answer> msg_answers;
    List<Tie> msg_ties;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case -1:
                    Toast.makeText(MainActivity.this,"连接服务器失败！",Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    jinBaAdapter=new JinBaAdapter(balist,MainActivity.this);
                    lv_jinba.setAdapter(jinBaAdapter);
                    break;
                case 1:
                    shouyeAdapter=new ShouyeAdapter(tieList,shouye_ba,shouye_userinfo,MainActivity.this,MainActivity.this);
                    lv_shouye.setAdapter(shouyeAdapter);
                    ListViewUtil.setListViewBasedOnChildren(lv_shouye);
                    break;
                case 2:
                    msg_inanswers=JsonUtil.getInAnswerJson(msgstr[0]);
                    msg_userInfos=JsonUtil.getUserInfoListJson(msgstr[1]);
                    msg_bas=JsonUtil.getBaJson(msgstr[2]);
                    msg_answers=JsonUtil.getAnswerJson(msgstr[3]);
                    msg_ties=JsonUtil.getTieJson(msgstr[4]);
                    msgAdapter=new MsgAdapter(msg_inanswers,msg_userInfos,msg_answers,msg_bas,MainActivity.this);
                    lv_message.setAdapter(msgAdapter);

                    lv_message.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent=new Intent(MainActivity.this,InAnswer_Home.class);
                            intent.putExtra("answer",msg_answers.get(i));
                            intent.putExtra("userinfo",msg_userInfos.get(i));
                            startActivity(intent);
                        }
                    });
                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ApplyPermission();
        initView();
    }

    private void ApplyPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET
        },1);
    }//申请权限

    public void initView(){
        StatusBarUtil.setBar(this);

        //加载主界面
        viewPager=(NoScrollViewPager)findViewById(R.id.vpg);
        LayoutInflater inflater=getLayoutInflater();
        view_shouye=inflater.inflate(R.layout.shouye,null);
        view_jinba=inflater.inflate(R.layout.jinba,null);
        view_xiaoxi=inflater.inflate(R.layout.message,null);
        view_wode=inflater.inflate(R.layout.wode,null);
        view_login=inflater.inflate(R.layout.login,null);
        viewList=new ArrayList<>();
        viewList.add(view_shouye);
        viewList.add(view_jinba);
        viewList.add(view_xiaoxi);
        viewList.add(view_wode);
        viewList.add(view_login);
        PagerAdapter pagerAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return viewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                // TODO Auto-generated method stub
                container.removeView(viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // TODO Auto-generated method stub
                container.addView(viewList.get(position));
                return viewList.get(position);
            }
        };
        viewPager.setAdapter(pagerAdapter);

        btn_shouye=(LinearLayout)findViewById(R.id.btn_home);
        btn_jinba=(LinearLayout)findViewById(R.id.btn_jinba);
        btn_xiaoxi=(LinearLayout)findViewById(R.id.btn_xiaoxi);
        btn_wode=(LinearLayout)findViewById(R.id.btn_wode);
        btn_shouye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Link.user!=null){
                    viewPager.setCurrentItem(0);
                    initshouye();
                } else{
                    initlogin();
                }
            }
        });
        btn_jinba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Link.user!=null){
                    viewPager.setCurrentItem(1);
                    initjinba();
                } else{
                    initlogin();
                }
            }
        });
        btn_xiaoxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Link.user!=null){
                    viewPager.setCurrentItem(2);
                    initxiaoxi();
                } else{
                    initlogin();
                }
            }
        });
        btn_wode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Link.user!=null){
                    viewPager.setCurrentItem(3);
                    initwode();
                } else{
                    initlogin();
                }

            }
        });
        initlogin();

    }

    public void initlogin(){
        viewPager.setCurrentItem(4);
        login_tologininfo=(Button)view_login.findViewById(R.id.login_tologininfo);
        login_tologininfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Login.class);
                startActivityForResult(intent,0);
            }
        });
    }

    public void initjinba(){
        RequestParams params = new RequestParams();
        params.put("id", "2");
        RequestUtils.clientPost("getfocusba", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                balist=JsonUtil.getBaJson(new String(responseBody));
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.sendEmptyMessage(-1);
            }
        });

        lv_jinba=(MyListView) view_jinba.findViewById(R.id.lv_jinba);

        lv_jinba.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(MainActivity.this,Ba_Home.class);
                intent.putExtra("ba",(Serializable)balist.get(i));
                startActivity(intent);
                overridePendingTransition(R.anim.fade_out,R.anim.fade_in);
            }
        });
    }

    public void initshouye(){

        lv_shouye=(ListView) view_shouye.findViewById(R.id.lv_shouye);
        RequestParams params=new RequestParams();
        params.put("id",Link.user.getId());

        RequestUtils.clientPost("getfocustie", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String[] str=new String(responseBody).split("#flag#");
                tieList=JsonUtil.getTieJson(str[0]);
                shouye_ba=JsonUtil.getBaJson(str[1]);
                shouye_userinfo=JsonUtil.getUserInfoListJson(str[2]);
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.sendEmptyMessage(-1);
            }
        });

        lv_shouye.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(MainActivity.this,Tie_Home.class);
                intent.putExtra("tie",(Serializable)tieList.get(i));
                intent.putExtra("userinfo",(Serializable)shouye_userinfo.get(i));
                intent.putExtra("ba",(Serializable)shouye_ba.get(i));
                startActivity(intent);
            }
        });
    }

    public void initxiaoxi(){

        RequestParams params=new RequestParams();
        params.put("id",Link.user.getId());

        RequestUtils.clientPost("getmessage", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                msgstr=new  String(responseBody).split("#flag#");
                handler.sendEmptyMessage(2);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.sendEmptyMessage(-1);
            }
        });
        lv_message=(MyListView)view_xiaoxi.findViewById(R.id.lv_message);

    }

    public void initwode(){
        wode_exit=(Button)view_wode.findViewById(R.id.wode_exit);
        wode_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Link.user=null;
                Link.userInfo=null;
                viewPager.setCurrentItem(4);
            }
        });
        wode_head=(RoundedImageView)view_wode.findViewById(R.id.wode_head);
        wode_name=(TextView)view_wode.findViewById(R.id.wode_name);
        wode_focusnum=(TextView)view_wode.findViewById(R.id.wode_focusnum);
        wode_focusbanum=(TextView)view_wode.findViewById(R.id.wode_focusbanum);
        wode_fansnum=(TextView)view_wode.findViewById(R.id.wode_fansnum);
        wode_tienum=(TextView)view_wode.findViewById(R.id.wode_tienum);
        wode_user=(RelativeLayout)view_wode.findViewById(R.id.wode_user);
        wode_collection=(RelativeLayout)view_wode.findViewById(R.id.wode_collection);
        wode_message=(RelativeLayout)view_wode.findViewById(R.id.wode_message);
        wode_safety=(RelativeLayout)view_wode.findViewById(R.id.wode_safety);
        wode_report=(RelativeLayout)view_wode.findViewById(R.id.wode_report);

        if(Link.userInfo.getHead()!=""&&Link.userInfo.getHead()!=null){
//            GlideApp.with(this).load(Link.userInfo.getHead()).into(wode_head);
            Glide.with(this)
                    .load(Link.userInfo.getHead())
                    .into(wode_head);
        }


        wode_name.setText(Link.userInfo.getName());
        wode_focusnum.setText(Link.userInfo.getFocusnum()+"");
        wode_fansnum.setText(Link.userInfo.getFansnum()+"");
        wode_focusbanum.setText(Link.userInfo.getFocusbanum()+"");
        wode_tienum.setText(Link.userInfo.getTienum()+"");

        wode_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Collection_Home.class);
                intent.putExtra("id",Link.user.getId());
                startActivity(intent);
            }
        });

        wode_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        wode_safety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        wode_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        wode_focusnum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Focus_Home.class);
                intent.putExtra("id",Link.user.getId());
                startActivity(intent);
            }
        });

        wode_fansnum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Fans_Home.class);
                intent.putExtra("id",Link.user.getId());
                startActivity(intent);
            }
        });

        wode_focusbanum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
                initjinba();
            }
        });

        wode_tienum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Wode_Tie_Home.class);
                intent.putExtra("id",Link.user.getId());
                startActivity(intent);
            }
        });

        wode_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,UserInfo_Home.class);
                intent.putExtra("id",Link.user.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case 0:
                break;
            case 1:
                viewPager.setCurrentItem(3);
                initwode();
                break;
        }
    }

    @Override
    protected void onPause() {
        overridePendingTransition(R.anim.fade_out,R.anim.fade_in);
        super.onPause();
    }

    @Override
    public void Click(View v) {
        int possition=Integer.parseInt(v.getTag()+"");
        View view=lv_shouye.getChildAt(possition - lv_shouye.getFirstVisiblePosition());

        TextView thumbnum=view.findViewById(R.id.shouye_thumbnum);
        switch (v.getId()){
            case R.id.shouye_zan:
                tieList.get(possition).setThumbnum(tieList.get(possition).getThumbnum()+1);
                thumbnum.setText(tieList.get(possition).getThumbnum()+"");
                break;
            case R.id.shouye_cai:
                tieList.get(possition).setThumbnum(tieList.get(possition).getThumbnum()-1);
                thumbnum.setText(tieList.get(possition).getThumbnum()+"");
                break;
            case R.id.shouye_ba:
                Intent intent=new Intent(MainActivity.this,Ba_Home.class);
                intent.putExtra("ba",shouye_ba.get(possition));
                startActivity(intent);
                break;
        }
    }
}
