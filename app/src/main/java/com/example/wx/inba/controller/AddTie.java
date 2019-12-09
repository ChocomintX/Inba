package com.example.wx.inba.controller;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.example.wx.inba.R;
import com.example.wx.inba.dao.Link;
import com.example.wx.inba.dao.RequestUtils;
import com.example.wx.inba.model.Ba;
import com.example.wx.inba.model.Tie;
import com.example.wx.inba.util.BitmapUtils;
import com.example.wx.inba.util.FileUtils;
import com.example.wx.inba.util.JsonUtil;
import com.example.wx.inba.util.KeyBoardUtil;
import com.example.wx.inba.util.StatusBarUtil;
import com.example.wx.inba.util.Utils;
import com.google.gson.annotations.Until;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

public class AddTie extends Activity implements View.OnClickListener {
    private ImageView addtie_close,addtie_img1,addtie_img2,addtie_img3;
    private TextView addtie_ba,addtie_send;
    private EditText addtie_title,addtie_content;
    private String type;
    private Ba ba;
    private Tie tie;
    private String imgPath1,imgPath2,imgPath3,video;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case -1:
                    Toast.makeText(AddTie.this,"连接服务器失败！",Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    AddTie.this.setResult(1);
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtie);
        initView();
    }

    public void initView(){
        StatusBarUtil.setBar(this);
        this.setResult(0);

        ba=(Ba)getIntent().getSerializableExtra("ba");
        type=getIntent().getStringExtra("type");
        addtie_close=(ImageView)findViewById(R.id.addtie_close);
        addtie_close.setOnClickListener(this);
        addtie_img1=(ImageView)findViewById(R.id.addtie_addimg1);
        addtie_img2=(ImageView)findViewById(R.id.addtie_addimg2);
        addtie_img3=(ImageView)findViewById(R.id.addtie_addimg3);
        addtie_ba=(TextView)findViewById(R.id.addtie_ba);
        addtie_send=(TextView)findViewById(R.id.addtie_send);
        addtie_send.setOnClickListener(this);
        addtie_title=(EditText)findViewById(R.id.addtie_title);
        addtie_content=(EditText)findViewById(R.id.addtie_content);

        addtie_ba.setText("发布到"+ba.getName()+"吧");

        addtie_img1.setOnClickListener(this);
        addtie_img2.setOnClickListener(this);
        addtie_img3.setOnClickListener(this);

        switch (type){
            case "text":
                addtie_img1.setVisibility(View.GONE);
                addtie_img2.setVisibility(View.GONE);
                addtie_img3.setVisibility(View.GONE);
                break;
            case "img":
                addtie_img2.setVisibility(View.GONE);
                addtie_img3.setVisibility(View.GONE);
                break;
            case "video":
                addtie_img2.setVisibility(View.GONE);
                addtie_img3.setVisibility(View.GONE);
                break;

        }
    }

    public void sendTie(){
        if(addtie_title.getText().length()==0){
            Toast.makeText(this,"标题还是空的呢",Toast.LENGTH_SHORT).show();
            KeyBoardUtil.hideKeyboard(this);
            return;
        }else if(addtie_content.getText().length()==0){
            Toast.makeText(this,"内容你还没写啊",Toast.LENGTH_SHORT).show();
            KeyBoardUtil.hideKeyboard(this);
            return;
        }

        tie=new Tie();
        tie.setTitle(addtie_title.getText()+"");
        tie.setContent(addtie_content.getText()+"");
        tie.setThumbnum(0);
        tie.setAnswernum(0);
        tie.setBaid(ba.getId());
        tie.setUserid(Link.user.getId());

        RequestParams params=new RequestParams();
        params.put("tie", JsonUtil.toTieJson(tie));
        params.put("type",type);

        if(video!=null){
            params.put("video",video);
        }

        if(imgPath1!=null){
            String img1= Utils.BitmapToString(BitmapFactory.decodeFile(imgPath1));
            params.put("img1",img1);
        }

        if(imgPath2!=null){
            String img2= Utils.BitmapToString(BitmapFactory.decodeFile(imgPath2));
            params.put("img2",img2);
        }

        if(imgPath3!=null){
            String img3= Utils.BitmapToString(BitmapFactory.decodeFile(imgPath3));
            params.put("img3",img3);
        }

        RequestUtils.clientPost("addtie", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.sendEmptyMessage(-1);
            }
        });
    }

    public void loadImage(int num) {
        //这里就写了从相册中选择图片，相机拍照的就略过了
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, num);
    }

    public void loadVideo(){
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent,4);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==4&&null!=data){
            Uri selectedVideo = data.getData();
            String videoStr= FileUtils.getFilePathByUri(this,selectedVideo);
            File file=new File(videoStr);
            if (file.length() > 100 * 1024 * 1024) {
                Toast.makeText(this,"文件大于100M",Toast.LENGTH_SHORT).show();
                return;
            }else {
                video=Utils.VideotoString(file);

                addtie_img1.setImageBitmap(Utils.getVideoThumb(videoStr));
            }
            return;
        }


        try {
            if (resultCode == RESULT_OK && null != data) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // 获取游标
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                if(requestCode == 1){
                    imgPath1 = cursor.getString(columnIndex);
                    addtie_img1.setImageBitmap(BitmapFactory.decodeFile(imgPath1));
                    addtie_img2.setVisibility(View.VISIBLE);
                }

                if(requestCode == 2){
                    imgPath2= cursor.getString(columnIndex);
                    addtie_img2.setImageBitmap(BitmapFactory.decodeFile(imgPath2));
                    addtie_img3.setVisibility(View.VISIBLE);
                }

                if(requestCode == 3){
                    imgPath3 = cursor.getString(columnIndex);
                    addtie_img3.setImageBitmap(BitmapFactory.decodeFile(imgPath3));
                }

                cursor.close();

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
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
            case R.id.addtie_close:
                finish();
                break;
            case R.id.addtie_send:
                sendTie();
                break;
            case R.id.addtie_addimg1:
                if(type.equals("img"))
                    loadImage(1);
                else
                    loadVideo();
                break;
            case R.id.addtie_addimg2:
                loadImage(2);
                break;
            case R.id.addtie_addimg3:
                loadImage(3);
                break;
        }
    }
}
