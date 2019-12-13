package com.example.wx.inba.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wx.inba.R;
import com.example.wx.inba.controller.Login;
import com.example.wx.inba.controller.MainActivity;
import com.example.wx.inba.dao.Link;
import com.example.wx.inba.dao.RequestUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.impl.FullScreenPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.util.XPopupUtils;

import cz.msebera.android.httpclient.Header;

public class ForgetPopup extends FullScreenPopupView {
    private int h;
    private RelativeLayout top;
    private Button btn;
    private TextView title;
    private TextView answer;
    private TextView pass;
    private TextView repass;
    private Login window;
    private int flag=0;
    private String username;

    public ForgetPopup(@NonNull Context context, int h, Login window) {
        super(context);
        this.h=h;
        this.window=window;
    }

    protected int getImplLayoutId() {
        return R.layout.forget;
    }

    protected void onCreate() {
        super.onCreate();
        top=(RelativeLayout)findViewById(R.id.forget_top);
        top.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ForgetPopup.this.dismiss();
            }
        });
        StatusBarUtil.setMargins(top,0,h,0,0);

        title=(TextView)findViewById(R.id.forget_title);
        btn=(Button)findViewById(R.id.forget_submit);
        answer=(TextView)findViewById(R.id.forget_answer);
        pass=(TextView)findViewById(R.id.forget_password);
        repass=(TextView)findViewById(R.id.forget_repass);

        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag==0){
                    getQuestion();
                }else if(flag==1){
                    checkAnswer();
                }else if(flag==2){
                    changePass();
                }
            }
        });
    }

    private void getQuestion(){
        KeyBoardUtil.hideKeyboard(window);

        RequestParams params=new RequestParams();
        params.put("username", answer.getText().toString());
        params.put("type","get");

        RequestUtils.clientPost("forget", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s=new String(responseBody);
                if(s.endsWith("！")){
                    Toast.makeText(ForgetPopup.this.getContext(),"用户不存在！",Toast.LENGTH_SHORT).show();
                    return;
                }
                username=answer.getText().toString();
                title.setText(s);
                answer.setText("");
                answer.setHint("输入安全问题答案");
                flag=1;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(ForgetPopup.this.getContext(),"连接服务器失败！",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkAnswer(){
        KeyBoardUtil.hideKeyboard(window);

        RequestParams params=new RequestParams();
        params.put("username",username);
        params.put("answer",answer.getText().toString());
        params.put("type","check");

        RequestUtils.clientPost("forget", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s=new String(responseBody);
                if(s.equals("yes")){
                    flag=2;
                    title.setText("修改您的密码");
                    answer.setVisibility(View.GONE);
                    pass.setVisibility(View.VISIBLE);
                    repass.setVisibility(View.VISIBLE);
                    btn.setText("确认修改");
                }else{
                    Toast.makeText(ForgetPopup.this.getContext(),s,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(ForgetPopup.this.getContext(),"连接服务器失败！",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changePass(){
        KeyBoardUtil.hideKeyboard(window);

        final String s1=pass.getText().toString();
        final String s2=repass.getText().toString();

        if(s1.equals("")||s2.equals("")){
            Toast.makeText(this.getContext(),"请填写完整！",Toast.LENGTH_SHORT).show();
            return;
        }else if(!s1.equals(s2)){
            Toast.makeText(this.getContext(),"两次输入不一致！",Toast.LENGTH_SHORT).show();
            return;
        }

        new XPopup.Builder(getContext()).asConfirm("提示", "是否确认修改密码？",
                new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        RequestParams params=new RequestParams();
                        params.put("username", username);
                        params.put("password",s1);
                        params.put("type","change");

                        RequestUtils.clientPost("forget", params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                Toast.makeText(ForgetPopup.this.getContext(),new String(responseBody),Toast.LENGTH_SHORT).show();
                                ForgetPopup.this.dismiss();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                Toast.makeText(ForgetPopup.this.getContext(),"连接服务器失败！",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .show();


    }
}
