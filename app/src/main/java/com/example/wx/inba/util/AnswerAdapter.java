package com.example.wx.inba.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wx.inba.R;
import com.example.wx.inba.controller.InAnswer_Home;
import com.example.wx.inba.controller.Tie_Home;
import com.example.wx.inba.model.Answer;
import com.example.wx.inba.model.Tie;
import com.example.wx.inba.model.User;
import com.example.wx.inba.model.UserInfo;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import static android.content.ContentValues.TAG;

public class AnswerAdapter extends BaseAdapter implements View.OnClickListener {
    private List<Answer> list;
    private Context context;
    private CallBack callBack;
    private List<UserInfo> userInfoList;

    public AnswerAdapter(List<Answer> list, List<UserInfo> userInfoList,Context context, CallBack callBack) {
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

    public class ViewHolder{
        TextView inanswer;
        RoundedImageView img;
        TextView content;
        RoundedImageView head;
        TextView username;
        TextView settime;
        TextView thumbnum;
        ImageView zan;
        ImageView cai;
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
            view=LayoutInflater.from(context).inflate(R.layout.answer_item,viewGroup,false);
            viewHolder=new ViewHolder();
            viewHolder.img=view.findViewById(R.id.answer_img);
            viewHolder.inanswer=(TextView)view.findViewById(R.id.answer_inanswer);
            viewHolder.content=(TextView)view.findViewById(R.id.answer_content);
            viewHolder.head=(RoundedImageView)view.findViewById(R.id.answer_head);
            viewHolder.settime=(TextView)view.findViewById(R.id.answer_settime);
            viewHolder.username=(TextView)view.findViewById(R.id.answer_name);
            viewHolder.thumbnum=(TextView)view.findViewById(R.id.answer_thumbnum);
            viewHolder.zan=(ImageView) view.findViewById(R.id.answer_zan);
            viewHolder.cai=(ImageView) view.findViewById(R.id.answer_cai);

            view.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)view.getTag();
        }

        Answer answer=list.get(i);
        UserInfo userInfo=userInfoList.get(i);

        viewHolder.username.setText(userInfo.getName());
        viewHolder.settime.setText("第"+answer.getFloor()+"楼·"+answer.getAnswertime());
        if(userInfo.getHead()==null){
            viewHolder.img.setVisibility(View.GONE);
        }else{
            viewHolder.head.setTag(null);
            Glide.with(context).load(userInfo.getHead()).into(viewHolder.head);
        }

        viewHolder.content.setText(answer.getContent());
        viewHolder.thumbnum.setText(answer.getThumbnum()+"");

        if(answer.getImgpath()==null||answer.getImgpath().equals("")){
            viewHolder.img.setImageResource(R.drawable.test1);
        }else{
            Glide.with(context).load(answer.getImgpath()).into(viewHolder.img);
        }

        viewHolder.inanswer.setTag(i);
        viewHolder.inanswer.setOnClickListener(this);
        viewHolder.zan.setTag(i);
        viewHolder.zan.setOnClickListener(this);
        viewHolder.cai.setTag(i);
        viewHolder.cai.setOnClickListener(this);
        viewHolder.head.setTag(i);
        viewHolder.head.setOnClickListener(this);

        if(answer.getInanswernum()==0){
            viewHolder.inanswer.setText("回复");
        }else{
            viewHolder.inanswer.setText("查看"+answer.getInanswernum()+"条回复 >");
        }

        return view;
    }
}
