package com.example.wx.inba.controller;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.baoyz.actionsheet.ActionSheet;
import com.bumptech.glide.Glide;
import com.example.wx.inba.R;
import com.example.wx.inba.dao.Link;
import com.example.wx.inba.dao.RequestUtils;
import com.example.wx.inba.model.Answer;
import com.example.wx.inba.model.Ba;
import com.example.wx.inba.model.Report;
import com.example.wx.inba.model.Tie;
import com.example.wx.inba.model.UserInfo;
import com.example.wx.inba.util.AnswerAdapter;
import com.example.wx.inba.util.HeightListView;
import com.example.wx.inba.util.JsonUtil;
import com.example.wx.inba.util.KeyBoardUtil;
import com.example.wx.inba.util.StatusBarUtil;
import com.example.wx.inba.util.Utils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class Tie_Home extends AppCompatActivity implements View.OnClickListener,AnswerAdapter.CallBack, ActionSheet.ActionSheetListener {
    private AnswerAdapter answerAdapter;
    private List<Answer> list;
    private List<UserInfo> userInfoList;
    private HeightListView answer_listview;
    private ImageView tie_home_return,tie_home_louzhu,tie_home_gengduo,tie_home_collection;
    private TextView inanswer;
    private TextView tie_home_name,tie_home_settime,tie_home_content,tie_home_thumbnum,tie_home_answernum,tie_home_ba;
    private RoundedImageView tie_home_head;
    private Tie tie;
    private UserInfo userInfo;
    private Ba ba;
    private RoundedImageView tie_home_img1,tie_home_img2,tie_home_img3;
    private Button tie_home_sendmessage;
    private EditText tie_home_messagecontent;
    private VideoView tie_home_video;
    private int floor=2,louzhu=0,isCollection=0;
    private ImageView answer_photo;
    private String answerphotopath;
    private ImageView identity;


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case -1:
                    Toast.makeText(Tie_Home.this,"连接服务器失败！",Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    getAnswerUser();
                    break;
                case 1:
                    answerAdapter=new AnswerAdapter(list,userInfoList,Tie_Home.this,Tie_Home.this);
                    if(list.size()!=0){
                        floor=list.get(list.size()-1).getFloor()+1;
                    }
                    answer_listview.setAdapter(answerAdapter);
                    break;
                case 2:
                    tie_home_collection.setImageResource(R.drawable.collection_yes);
                    break;
                case 3:
                    tie_home_name.setText(userInfo.getName());
                    Glide.with(Tie_Home.this).load(userInfo.getHead()).into(tie_home_head);
                    break;
                case 4:
                    tie_home_ba.setText(ba.getName());
                    identity=(ImageView)findViewById(R.id.tie_home_identity);
                    if(ba.getBazhuid()==userInfo.getId()){
                        identity.setVisibility(View.VISIBLE);
                        identity.setImageResource(R.drawable.admin);
                    }else if(userInfo.getId()==1){
                        identity.setVisibility(View.VISIBLE);
                        identity.setImageResource(R.drawable.superadmin);
                    }

                    break;
                case 5:
                    Toast.makeText(Tie_Home.this,"删除成功！",Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tie_home);
        initView();
    }

    public void initView(){
        StatusBarUtil.setRootViewFitsSystemWindows(this,true);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setStatusBarColor(this,Color.parseColor("#2a2a2a"));

        tie=(Tie)getIntent().getSerializableExtra("tie");
//        userInfo=(UserInfo)getIntent().getSerializableExtra("userinfo");
//        ba=(Ba) getIntent().getSerializableExtra("ba");
        if(userInfo==null){
            getInfo();
        }

        tie_home_name=(TextView)findViewById(R.id.tie_home_name);
//        tie_home_name.setText(userInfo.getName());
        tie_home_settime=(TextView)findViewById(R.id.tie_home_settime);
        tie_home_settime.setText(tie.getSettime());
        tie_home_content=(TextView)findViewById(R.id.tie_home_content);
        tie_home_content.setText(tie.getContent());
        tie_home_answernum=(TextView)findViewById(R.id.tie_home_answernum);
        tie_home_answernum.setText(tie.getAnswernum()+"");
        tie_home_thumbnum=(TextView)findViewById(R.id.tie_home_thumbnum);
        tie_home_thumbnum.setText(tie.getThumbnum()+"");
        tie_home_ba=(TextView)findViewById(R.id.tie_home_ba) ;
//        tie_home_ba.setText(ba.getName());
        tie_home_ba.setOnClickListener(this);
        tie_home_head=(RoundedImageView) findViewById(R.id.tie_home_head);
//        Glide.with(this).load(userInfo.getHead()).into(tie_home_head);
        tie_home_head.setOnClickListener(this);
        tie_home_img1=(RoundedImageView)findViewById(R.id.tie_home_img1);
        tie_home_img2=(RoundedImageView)findViewById(R.id.tie_home_img2);
        tie_home_img3=(RoundedImageView)findViewById(R.id.tie_home_img3);
        tie_home_video=(VideoView)findViewById(R.id.tie_home_video);
        tie_home_sendmessage=(Button)findViewById(R.id.tie_home_sendmessage);
        tie_home_sendmessage.setOnClickListener(this);
        tie_home_messagecontent=(EditText)findViewById(R.id.tie_home_messagecontent);

        setimg();

        answer_listview=(HeightListView)findViewById(R.id.answer_listview);
        answer_photo=(ImageView)findViewById(R.id.answer_photo);
        answer_photo.setOnClickListener(this);

        getAnswer();
        getCollection();

        tie_home_return=(ImageView)findViewById(R.id.tie_home_return);
        tie_home_louzhu=(ImageView)findViewById(R.id.tie_home_zhikanlouzhu);
        tie_home_gengduo=(ImageView)findViewById(R.id.tie_home_gengduo);
        tie_home_collection=(ImageView)findViewById(R.id.tie_home_collection);
        tie_home_return.setOnClickListener(this);
        tie_home_louzhu.setOnClickListener(this);
        tie_home_gengduo.setOnClickListener(this);
        tie_home_collection.setOnClickListener(this);
    }

    public void getInfo(){
        RequestParams params=new RequestParams();
        params.put("id",tie.getUserid());
        params.put("baid",tie.getBaid());

        RequestUtils.clientPost("getuserinfo", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                userInfo=JsonUtil.getUserInfoJson(new String(responseBody));
                handler.sendEmptyMessage(3);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.sendEmptyMessage(-1);
            }
        });

        RequestUtils.clientPost("getbabyid", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                ba=JsonUtil.getBaJson1(new String(responseBody));
                handler.sendEmptyMessage(4);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.sendEmptyMessage(-1);
            }
        });
    }

    public void getAnswer(){
        RequestParams params = new RequestParams();
        params.put("tieid", tie.getId());

        RequestUtils.clientPost("getanswer", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                list=JsonUtil.getAnswerJson(new String(responseBody));
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.sendEmptyMessage(-1);
            }
        });
    }

    public void getAnswerUser(){
        RequestParams params = new RequestParams();
        userInfoList=new ArrayList<UserInfo>();
        for(Answer answer:list){
            userInfoList.add(new UserInfo(answer.getUserid(),"","",1,1,1,1));
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

    public void zhikanlouzhu(){

        if (louzhu==1){

            louzhu=0;
            tie_home_louzhu.setImageResource(R.drawable.zhikanlouzhu_no);
            getAnswer();
        }else {
            List<UserInfo> ls1=new ArrayList<UserInfo>();
            List<Answer> ls2=new ArrayList<>();
            for(int i=0;i<userInfoList.size();i++){
                if(list.get(i).getUserid()==userInfo.getId()){
                    ls1.add(userInfoList.get(i));
                    ls2.add(list.get(i));
                }
            }

            tie_home_louzhu.setImageResource(R.drawable.zhikanlouzhu_yes);
            louzhu=1;
            list.clear();
            userInfoList.clear();
            userInfoList=ls1;
            list=ls2;
            handler.sendEmptyMessage(1);
            Toast.makeText(this,"只看楼主",Toast.LENGTH_SHORT).show();
        }

    }

    public void setimg(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(tie.getImg1().equals("")){
                    tie_home_img1.setVisibility(View.GONE);
                }else{
                    Glide.with(Tie_Home.this).load(tie.getImg1()).into(tie_home_img1);
                }

                if(tie.getImg2().equals("")){
                    tie_home_img2.setVisibility(View.GONE);
                }else{
                    Glide.with(Tie_Home.this).load(tie.getImg2()).into(tie_home_img2);
                }

                if(tie.getImg3().equals("")){
                    tie_home_img3.setVisibility(View.GONE);
                }else{
                    Glide.with(Tie_Home.this).load(tie.getImg3()).into(tie_home_img3);
                }

                if(tie.getVideo().equals("")){
                    tie_home_video.setVisibility(View.GONE);
                }else{

                    MediaController localMediaController = new MediaController(Tie_Home.this);
                    tie_home_video.setMediaController(localMediaController);
                    String url = Link.url +"video.mp4";
                    tie_home_video.setVideoPath(url);
                    tie_home_video.start();
                }
            }
        });
    }

    public void sendAnswer(){
        if(Link.fengjin){
            Toast.makeText(this,"您已被封禁，无法回复！",Toast.LENGTH_SHORT).show();
            return;
        }

        if(tie_home_messagecontent.getText().length()==0){
            Toast.makeText(this,"消息为空！",Toast.LENGTH_SHORT).show();
        }else{
            final String messagecontent=tie_home_messagecontent.getText()+"";

            Answer as=new Answer(Link.user.getId(),tie.getId(),messagecontent,0,"",null,floor,0);
            list.clear();
            userInfoList.clear();

            RequestParams params=new RequestParams();
            params.put("answer",JsonUtil.toAnswerJson(as));

            if(answerphotopath!=null){
                String img= Utils.BitmapToString(BitmapFactory.decodeFile(answerphotopath));
                Toast.makeText(this,"图片为空！",Toast.LENGTH_SHORT).show();
                params.put("img",img);
            }

            RequestUtils.clientPost("addanswer", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Toast.makeText(Tie_Home.this,"回复成功！",Toast.LENGTH_SHORT).show();
                    tie_home_messagecontent.setText("");
                    tie.setAnswernum(tie.getAnswernum()+1);
                    tie_home_answernum.setText(tie.getAnswernum()+"");
                    getAnswer();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    handler.sendEmptyMessage(-1);
                }
            });
        }
        KeyBoardUtil.hideKeyboard(Tie_Home.this);
    }

    public void getCollection(){
        RequestParams params = new RequestParams();
        params.put("tieid", tie.getId());
        params.put("userid",Link.user.getId());
        params.put("type","get");

        RequestUtils.clientPost("setcollection", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                List<Tie> cllist=JsonUtil.getTieJson(new String(responseBody));
                for(Tie t:cllist){
                    if(t.getId()==tie.getId()){
                        handler.sendEmptyMessage(2);
                        isCollection=1;
                        break;
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.sendEmptyMessage(-1);
            }
        });
    }

    public void setCollection(){
        RequestParams params = new RequestParams();
        params.put("tieid", tie.getId());
        params.put("userid",Link.user.getId());
        if(isCollection==0){
            params.put("type","add");
            tie_home_collection.setImageResource(R.drawable.collection_yes);
            isCollection=1;
        }else if(isCollection==1){
            params.put("type","cancel");
            tie_home_collection.setImageResource(R.drawable.collection);
            isCollection=0;
        }


        RequestUtils.clientPost("setcollection", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.sendEmptyMessage(-1);
            }
        });
    }

    public void deleteTie(){
        new XPopup.Builder(this).asConfirm("提示", "确认删除该帖？",
                new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        RequestParams params = new RequestParams();
                        params.put("tieid", tie.getId());
                        params.put("userid",Link.user.getId());

                        RequestUtils.clientPost("deletetie", params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                handler.sendEmptyMessage(5);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                handler.sendEmptyMessage(-1);
                            }
                        });
                    }
                })
                .show();
    }

    @Override
    protected void onPause() {
        overridePendingTransition(R.anim.fade_out,R.anim.fade_in);
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tie_home_return:
                finish();
                break;
            case R.id.tie_home_zhikanlouzhu:
                zhikanlouzhu();
                break;
            case R.id.tie_home_gengduo:
                String[] strings;
                if(Link.userInfo.getId()==1){
                    strings=new String[]{"举报该帖子","正序排序","倒序排序","删除该帖","置顶该帖"};
                }else if(ba.getBazhuid()==Link.userInfo.getId()&&userInfo.getId()!=1){
                    strings=new String[]{"举报该帖子","正序排序","倒序排序","删除该帖","置顶该帖"};
                }else if(userInfo.getId()==Link.userInfo.getId()){
                    strings=new String[]{"举报该帖子","正序排序","倒序排序","删除该帖"};
                }else{
                    strings=new String[]{"举报该帖子","正序排序","倒序排序"};
                }
                ActionSheet.createBuilder(this, getSupportFragmentManager())
                        .setCancelButtonTitle("取消")
                        .setOtherButtonTitles(strings)
                        .setCancelableOnTouchOutside(true)
                        .setListener(this).show();
//                if(userInfo.getId()==Link.user.getId()||ba.getBazhuid()==Link.userInfo.getId()||1==Link.userInfo.getId()){
//                    if(Link.userInfo.getId()!=1&&userInfo.getId()==1){
//                        ActionSheet.createBuilder(this, getSupportFragmentManager())
//                                .setCancelButtonTitle("取消")
//                                .setOtherButtonTitles("举报该帖子", "正序排序","倒序排序")
//                                .setCancelableOnTouchOutside(true)
//                                .setListener(this).show();
//                    }else{
//                        ActionSheet.createBuilder(this, getSupportFragmentManager())
//                                .setCancelButtonTitle("取消")
//                                .setOtherButtonTitles("举报该帖子", "正序排序","倒序排序","删除该帖")
//                                .setCancelableOnTouchOutside(true)
//                                .setListener(this).show();
//                    }
//                }else{
//                    ActionSheet.createBuilder(this, getSupportFragmentManager())
//                            .setCancelButtonTitle("取消")
//                            .setOtherButtonTitles("举报该帖子", "正序排序","倒序排序")
//                            .setCancelableOnTouchOutside(true)
//                            .setListener(this).show();
//                }
                break;
            case R.id.tie_home_sendmessage:
                sendAnswer();
                break;
            case R.id.tie_home_ba:
                Intent intent=new Intent(Tie_Home.this,Ba_Home.class);
                intent.putExtra("ba",ba);
                startActivity(intent);
                break;
            case R.id.tie_home_head:
                Intent intent1=new Intent(Tie_Home.this,UserInfo_Home.class);
                intent1.putExtra("id",userInfo.getId());
                startActivity(intent1);
                break;
            case R.id.tie_home_collection:
                setCollection();
                break;
            case R.id.answer_photo:
                loadAnswerPhoto();
                break;
        }
}

    public void loadAnswerPhoto(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == RESULT_OK && null != data) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // 获取游标
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                answerphotopath = cursor.getString(columnIndex);
                answer_photo.setImageBitmap(BitmapFactory.decodeFile(answerphotopath));
                cursor.close();

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    public void sendReport(String text){
        if(text.equals("")){
            Toast.makeText(this,"举报理由不能为空！",Toast.LENGTH_SHORT).show();
            return;
        }
        Report report=new Report();
        report.setTieid(tie.getId());
        report.setType("report");
        report.setUserid(Link.user.getId());
        report.setReason(text);

        RequestParams params=new RequestParams();
        params.put("report",JsonUtil.toReportJson(report));

        RequestUtils.clientPost("addreport", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(Tie_Home.this,"举报成功,管理员将及时处理",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.sendEmptyMessage(-1);
            }
        });
    }

    public void setTop(){
        RequestParams params=new RequestParams();
        Ba b=ba;
        b.setTopid(tie.getId());
        params.put("ba",JsonUtil.toBaJson(b));
        RequestUtils.clientPost("updateba", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(Tie_Home.this,"置顶成功",Toast.LENGTH_SHORT).show();
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
            case R.id.answer_inanswer:
                Intent intent=new Intent(this,InAnswer_Home.class);
                int possition=Integer.parseInt(v.getTag()+"");
                intent.putExtra("answer",(Serializable)list.get(possition));
                intent.putExtra("userinfo",(Serializable)userInfoList.get(possition));
                startActivity(intent);
                break;
            case R.id.answer_cai:
                Toast.makeText(Tie_Home.this,"踩了一下！",Toast.LENGTH_SHORT).show();
                int possition1=Integer.parseInt(v.getTag()+"");
                list.get(possition1).setThumbnum(list.get(possition1).getThumbnum()-1);
                View view1=answer_listview.getChildAt(possition1);
                TextView thumbnum1=view1.findViewById(R.id.answer_thumbnum);
                thumbnum1.setText(list.get(possition1).getThumbnum()+"");
                break;
            case R.id.answer_zan:
                Toast.makeText(Tie_Home.this,"赞了一下！",Toast.LENGTH_SHORT).show();
                int possition2=Integer.parseInt(v.getTag()+"");
                list.get(possition2).setThumbnum(list.get(possition2).getThumbnum()+1);
                View view2=answer_listview.getChildAt(possition2);
                TextView thumbnum2=view2.findViewById(R.id.answer_thumbnum);
                thumbnum2.setText(list.get(possition2).getThumbnum()+"");
                break;
            case R.id.answer_head:
                Intent intent1=new Intent(Tie_Home.this,UserInfo_Home.class);
                int possition3=Integer.parseInt(v.getTag()+"");
                intent1.putExtra("id",userInfoList.get(possition3).getId());
                startActivity(intent1);
                break;

        }
    }

    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
    }

    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
        if(index==0){
            new XPopup.Builder(Tie_Home.this)
                    .moveUpToKeyboard(true)
                    .autoOpenSoftInput(true)
                    .asInputConfirm("举报违规帖子", "举报理由：",
                    new OnInputConfirmListener() {
                        @Override
                        public void onConfirm(String text) {
                            sendReport(text);
                        }
                    })
                    .show();

        }
        else if(index==1){
            Collections.reverse(list);
            handler.sendEmptyMessage(1);
        }else if(index==2){
            Collections.sort(list);
            handler.sendEmptyMessage(1);
        }else if(index==3){
            deleteTie();
        }else if(index==4){
            setTop();
        }
    }


}
