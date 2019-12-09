package com.example.wx.inba.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wx.inba.R;
import com.example.wx.inba.model.Answer;
import com.example.wx.inba.model.Ba;
import com.example.wx.inba.model.InAnswer;
import com.example.wx.inba.model.Tie;
import com.example.wx.inba.model.User;
import com.example.wx.inba.model.UserInfo;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import static android.content.ContentValues.TAG;

public class MsgAdapter extends BaseAdapter {
    private List<InAnswer> list;
    private List<UserInfo> userInfos;
    private List<Answer> answers;
    private List<Ba> bas;
    private Context context;

    public MsgAdapter(List<InAnswer> list, List<UserInfo> userInfos, List<Answer> answers, List<Ba> bas, Context context) {
        this.list = list;
        this.userInfos=userInfos;
        this.answers=answers;
        this.bas=bas;
        this.context = context;
    }

    public class ViewHolder{
        RoundedImageView message_head;
        TextView message_name;
        TextView message_settime;
        TextView message_answer;
        TextView message_content;
        TextView message_ba;
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
        if(view==null) {
            view = LayoutInflater.from(context).inflate(R.layout.message_item, viewGroup, false);
            viewHolder=new ViewHolder();

            viewHolder.message_head=(RoundedImageView) view.findViewById(R.id.message_head) ;
            viewHolder.message_name=(TextView)view.findViewById(R.id.message_name) ;
            viewHolder.message_settime=(TextView)view.findViewById(R.id.message_settime) ;
            viewHolder.message_answer=(TextView)view.findViewById(R.id.message_answer) ;
            viewHolder.message_content=(TextView)view.findViewById(R.id.message_content) ;
            viewHolder.message_ba=(TextView)view.findViewById(R.id.message_ba) ;

            view.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)view.getTag();
        }

        InAnswer inAnswer=list.get(i);
        UserInfo ui=userInfos.get(i);
        Answer as=answers.get(i);
        Ba ba=bas.get(i);

        if(!ui.getHead().equals("")){
            Glide.with(context).load(ui.getHead()).into(viewHolder.message_head);
        }
        viewHolder.message_name.setText(ui.getName());
        viewHolder.message_content.setText(as.getContent());
        viewHolder.message_settime.setText(inAnswer.getInanswertime());
        viewHolder.message_answer.setText(inAnswer.getContent());
        viewHolder.message_ba.setText(ba.getName()+"吧");

        return view;
    }
}
