package com.example.wx.inba.util;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wx.inba.R;
import com.example.wx.inba.model.Report;
import com.lxj.xpopup.impl.FullScreenPopupView;

import java.util.ArrayList;
import java.util.List;

public class CustomFullScreenPopup extends FullScreenPopupView {
    private int h;
    private RelativeLayout top;
    private ImageView mReturn;
    private RecyclerView recyclerView;
    private List<Report> list;
    private TextView msg;

    public CustomFullScreenPopup(@NonNull Context context,int h,List<Report> list) {
        super(context);
        this.h=h;
        this.list=list;
    }
    @Override
    protected int getImplLayoutId() {
        return R.layout.wode_message_home;
    }
    @Override
    protected void onCreate() {
        super.onCreate();
        top=(RelativeLayout)findViewById(R.id.wode_message_top);
        StatusBarUtil.setMargins(top,0,h,0,0);
        mReturn=(ImageView)findViewById(R.id.wode_message_return);
        mReturn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomFullScreenPopup.this.dismiss();
            }
        });

        if(list.size()==0){
            msg=(TextView)findViewById(R.id.wode_message_no);
            msg.setVisibility(View.VISIBLE);
        }

        recyclerView = (RecyclerView) findViewById(R.id.wode_message__recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        WodeMessageAdapter adp=new WodeMessageAdapter(list);
        
        recyclerView.setAdapter(adp);

        //初始化
    }
}