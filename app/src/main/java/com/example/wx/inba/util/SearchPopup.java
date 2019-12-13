package com.example.wx.inba.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wx.inba.R;
import com.example.wx.inba.dao.Link;
import com.example.wx.inba.dao.RequestUtils;
import com.example.wx.inba.model.Ba;
import com.example.wx.inba.model.Report;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lxj.xpopup.impl.FullScreenPopupView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

public class SearchPopup extends FullScreenPopupView {
    private int h;
    private RelativeLayout top;
    private TextView mReturn;
    private ListView listView;
    private JinBaAdapter adapter;
    private List<Ba> list;
    private TextView msg;
    private EditText text;
    private int status=0;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case -1:
                    break;
                case 0:
                    Toast.makeText(SearchPopup.this.getContext(),status+"!",Toast.LENGTH_SHORT).show();
                    status+=1;

                    break;
            }
        }
    };



    public SearchPopup(@NonNull Context context, int h, List<Ba> list) {
        super(context);
        this.h = h;
        this.list = list;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.jinba_search;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        top = (RelativeLayout) findViewById(R.id.search_top);
        StatusBarUtil.setMargins(top, 0, h, 0, 0);
        mReturn = (TextView) findViewById(R.id.search_cancel);
        mReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchPopup.this.dismiss();
            }
        });

        if (list.size() == 0) {
            msg = (TextView) findViewById(R.id.search_no);
            msg.setVisibility(View.VISIBLE);
        }

        listView = (ListView) findViewById(R.id.search_lv);
        adapter=new JinBaAdapter(list,this.getContext());
        listView.setAdapter(adapter);

        text=(EditText)findViewById(R.id.search_text);
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                search();
            }
        });
//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                handler.sendEmptyMessage(0);
//                search();
//            }
//        }, 2000,2000);
        //初始化
    }

    private void search(){
        RequestParams params=new RequestParams();
        params.put("text", text.getText().toString());
        RequestUtils.clientPost("searchba", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                list=JsonUtil.getBaJson(new String(responseBody));
                adapter=new JinBaAdapter(list,SearchPopup.this.getContext());
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.sendEmptyMessage(-1);
            }
        });
    }

}