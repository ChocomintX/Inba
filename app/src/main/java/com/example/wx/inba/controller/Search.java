package com.example.wx.inba.controller;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.wx.inba.R;
import com.example.wx.inba.util.StatusBarUtil;

public class Search extends AppCompatActivity {

    private Handler handler=new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.jinba_search);

        initView();
    }

    private void initView(){
        StatusBarUtil.setTranslucentStatus(this);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(refreshRunnable);
        super.onDestroy();
    }

    private Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(Search.this,"111",Toast.LENGTH_SHORT).show();
            handler.postDelayed(this,3000);
        }
    };
}
