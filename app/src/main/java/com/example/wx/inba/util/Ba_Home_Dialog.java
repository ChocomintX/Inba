package com.example.wx.inba.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;

import com.example.wx.inba.R;

public class Ba_Home_Dialog extends Dialog implements View.OnClickListener {
    private boolean iscancelable;//控制点击dialog外部是否dismiss
    private boolean isBackCancelable;//控制返回键是否dismiss
    private int view;
    private Context context;
    private mCallBack cb;
    private LinearLayout exit,text,img,video;

    //这里的view其实可以替换直接传layout过来的 因为各种原因没传(lan)
    public Ba_Home_Dialog(Context context, int view, boolean isCancelable,boolean isBackCancelable,mCallBack cb) {
        super(context, R.style.MyDialog);

        this.context = context;
        this.view = view;
        this.iscancelable = isCancelable;
        this.cb=cb;
    }

    @Override
    public void onClick(View view) {
        cb.mClick(view);
    }

    public interface mCallBack{
        public void mClick(View v);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(view);//这行一定要写在前面

        exit=(LinearLayout)findViewById(R.id.ba_home_add_exit);
        exit.setOnClickListener(this);
        text=(LinearLayout)findViewById(R.id.ba_home_add_text);
        text.setOnClickListener(this);
        img=(LinearLayout)findViewById(R.id.ba_home_add_img);
        img.setOnClickListener(this);
        video=(LinearLayout)findViewById(R.id.ba_home_add_video);
        video.setOnClickListener(this);

        setCancelable(iscancelable);//点击外部不可dismiss
        setCanceledOnTouchOutside(isBackCancelable);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        window.setAttributes(params);
    }
}
