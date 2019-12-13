package com.example.wx.inba.util;

import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wx.inba.R;
import com.example.wx.inba.dao.Link;
import com.example.wx.inba.dao.RequestUtils;
import com.example.wx.inba.model.Ba;
import com.example.wx.inba.model.Tie;
import com.example.wx.inba.model.UserInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

public class BaHomeAdapter extends BaseAdapter implements View.OnClickListener {
    private List<Tie> list;
    private Context context;
    private CallBack callBack;
    private List<UserInfo> userInfoList;


    public BaHomeAdapter(List<Tie> list, List<UserInfo> userInfoList, Context context, CallBack callBack) {
        this.list = list;
        this.context = context;
        this.callBack=callBack;
        this.userInfoList=userInfoList;
    }

    @Override
    public void onClick(View view) {
        callBack.Click(view);
    }

    public interface CallBack{
        public void Click(View v);
    }

    public class ViewHolder {
        RoundedImageView ba_home_head;
        TextView ba_home_name;
        TextView ba_home_content;
        TextView ba_home_ba;
        TextView ba_home_sharenum;
        TextView ba_home_answernum;
        TextView ba_home_thumbnum;
        TextView ba_home_settime;
        RoundedImageView img1;
        RoundedImageView img2;
        RoundedImageView img3;
        ImageView ba_home_zan;
        ImageView ba_home_cai;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Log.i(TAG,"getView");
        ViewHolder viewHolder=null;
        if(view==null){
            view=LayoutInflater.from(context).inflate(R.layout.ba_home_item,viewGroup,false);
            viewHolder=new ViewHolder();

            viewHolder.ba_home_head=(RoundedImageView)view.findViewById(R.id.ba_home_head);
            viewHolder.ba_home_name=(TextView) view.findViewById(R.id.ba_home_name);
            viewHolder.ba_home_content=(TextView) view.findViewById(R.id.ba_home_content);
            viewHolder.ba_home_ba=(TextView)view.findViewById(R.id.ba_home_ba);
            viewHolder.ba_home_sharenum=(TextView)view.findViewById(R.id.ba_home_sharenum);
            viewHolder.ba_home_answernum=(TextView)view.findViewById(R.id.ba_home_answernum);
            viewHolder.ba_home_thumbnum=(TextView)view.findViewById(R.id.ba_home_thumbnum);
            viewHolder.ba_home_settime=(TextView)view.findViewById(R.id.ba_home_settime);
            viewHolder.img1=(RoundedImageView)view.findViewById(R.id.ba_home_img1);
            viewHolder.img2=(RoundedImageView)view.findViewById(R.id.ba_home_img2);
            viewHolder.img3=(RoundedImageView)view.findViewById(R.id.ba_home_img3);
            viewHolder.ba_home_zan=(ImageView)view.findViewById(R.id.ba_home_zan);
            viewHolder.ba_home_cai=(ImageView)view.findViewById(R.id.ba_home_cai);

            view.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)view.getTag();
        }

        Tie tie=list.get(i);
        UserInfo userInfo=userInfoList.get(i);

        if(tie.getImg1()==""){
            viewHolder.img1.setVisibility(View.GONE);
        }else{
            viewHolder.img1.setVisibility(View.VISIBLE);
            Glide.with(context).load(tie.getImg1()).into(viewHolder.img1);
        }

        if(tie.getImg2()==""){
            viewHolder.img2.setVisibility(View.GONE);
        }else{
            viewHolder.img2.setVisibility(View.VISIBLE);
            Glide.with(context).load(tie.getImg2()).into(viewHolder.img2);
        }

        if(tie.getImg3()==""){
            viewHolder.img3.setVisibility(View.GONE);
        }else{
            viewHolder.img3.setVisibility(View.VISIBLE);
            Glide.with(context).load(tie.getImg3()).into(viewHolder.img3);
        }

        if(tie.getVideo()!=""){
            viewHolder.img1.setVisibility(View.VISIBLE);
            viewHolder.img1.setImageResource(R.drawable.test);
        }

        viewHolder.ba_home_name.setText(userInfo.getName());
        Glide.with(context).load(userInfo.getHead()).into(viewHolder.ba_home_head);
        viewHolder.ba_home_content.setText(tie.getTitle()+"                                                                                                            ");
        viewHolder.ba_home_ba.setText(tie.getBaid()+"");
        viewHolder.ba_home_ba.setVisibility(View.GONE);
        viewHolder.ba_home_answernum.setText(tie.getAnswernum()+"");
        viewHolder.ba_home_thumbnum.setText(tie.getThumbnum()+"");
        viewHolder.ba_home_settime.setText("回复于"+tie.getAnswertime());
        viewHolder.ba_home_zan.setOnClickListener(this);
        viewHolder.ba_home_zan.setTag(i);
        viewHolder.ba_home_cai.setOnClickListener(this);
        viewHolder.ba_home_cai.setTag(i);
        return view;
    }

}
