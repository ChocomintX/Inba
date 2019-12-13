package com.example.wx.inba.util;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wx.inba.R;
import com.example.wx.inba.controller.MainActivity;
import com.example.wx.inba.dao.Link;
import com.example.wx.inba.dao.RequestUtils;
import com.example.wx.inba.model.Report;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.impl.FullScreenPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import java.util.List;

import cz.msebera.android.httpclient.Header;


public class ChangePassPopup extends FullScreenPopupView {
    private int h;
    private RelativeLayout top;
    private Button change;
    private TextView oldpass;
    private TextView pass;
    private TextView repass;
    private MainActivity window;

    public ChangePassPopup(@NonNull Context context, int h, MainActivity window) {
        super(context);
        this.h=h;
        this.window=window;
    }

    protected int getImplLayoutId() {
        return R.layout.wode_safety;
    }

    protected void onCreate() {
        super.onCreate();
        top=(RelativeLayout)findViewById(R.id.wode_safety_top);
        top.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePassPopup.this.dismiss();
            }
        });
        StatusBarUtil.setMargins(top,0,h,0,0);
        change=(Button)findViewById(R.id.wode_safety_submit);
        change.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                chagePass();
            }
        });

        oldpass=(TextView)findViewById(R.id.wode_safety_oldpass);
        pass=(TextView)findViewById(R.id.wode_safety_password);
        repass=(TextView)findViewById(R.id.wode_safety_repass);
    }

    private void chagePass(){
        KeyBoardUtil.hideKeyboard(window);

        final String s1=oldpass.getText().toString();
        final String s2=pass.getText().toString();
        String s3=repass.getText().toString();

        if(s1.equals("")||s2.equals("")||s3.equals("")){
            Toast.makeText(this.getContext(),"请填写完整信息！",Toast.LENGTH_SHORT).show();
            return;
        }else if(!s2.equals(s3)){
            Toast.makeText(this.getContext(),"两次输入不一致！",Toast.LENGTH_SHORT).show();
            return;
        }

        new XPopup.Builder(getContext()).asConfirm("提示", "是否确认修改密码？",
                new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        RequestParams params=new RequestParams();
                        params.put("id", Link.user.getId());
                        params.put("oldpass",s1);
                        params.put("password",s2);

                        RequestUtils.clientPost("changepass", params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                Toast.makeText(ChangePassPopup.this.getContext(),new String(responseBody),Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                Toast.makeText(ChangePassPopup.this.getContext(),"连接服务器失败！",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .show();
    }
}
