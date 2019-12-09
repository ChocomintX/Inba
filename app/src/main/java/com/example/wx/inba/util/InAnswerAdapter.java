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
import com.example.wx.inba.model.InAnswer;
import com.example.wx.inba.model.UserInfo;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import static android.content.ContentValues.TAG;

public class InAnswerAdapter extends BaseAdapter implements View.OnClickListener {
    private List<InAnswer> list;
    private Context context;
    private CallBack callBack;
    private List<UserInfo> inAnswerUsers;
    private List<UserInfo> inAnswertoUsers;

    public InAnswerAdapter(List<InAnswer> list, List<UserInfo> inAnswerUsers,List<UserInfo> inAnswertoUsers, Context context, CallBack callBack) {
        this.list = list;
        this.context = context;
        this.callBack=callBack;
        this.inAnswertoUsers=inAnswertoUsers;
        this.inAnswerUsers=inAnswerUsers;
    }

    @Override
    public void onClick(View view) {
        callBack.Click(view);
    }

    public interface CallBack{
        public void Click(View v);
    }

    public class ViewHolder{
        RoundedImageView infloor_head;
        TextView infloor_name;
        TextView infloor_settime;
        TextView infloor_content;
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
            view=LayoutInflater.from(context).inflate(R.layout.inanswer_item,viewGroup,false);
            viewHolder=new ViewHolder();
            viewHolder.infloor_content=(TextView)view.findViewById(R.id.infloor_content);
            viewHolder.infloor_settime=(TextView)view.findViewById(R.id.infloor_settime);
            viewHolder.infloor_name=(TextView)view.findViewById(R.id.infloor_name);
            viewHolder.infloor_head=(RoundedImageView) view.findViewById(R.id.infloor_head);

            view.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)view.getTag();
        }

        InAnswer inAnswer=list.get(i);
        UserInfo user1=inAnswerUsers.get(i);
        UserInfo user2=inAnswertoUsers.get(i);

        viewHolder.infloor_content.setText("回复 "+user2.getName()+" :"+inAnswer.getContent());
        viewHolder.infloor_content.setTag(user1.getId());
        viewHolder.infloor_content.setOnClickListener(this);
        viewHolder.infloor_name.setText(user1.getName());
        viewHolder.infloor_settime.setText(inAnswer.getInanswertime());

        viewHolder.infloor_head.setTag(null);
        Glide.with(context).load(user1.getHead()).into(viewHolder.infloor_head);
        viewHolder.infloor_head.setTag(i);
        viewHolder.infloor_head.setOnClickListener(this);

        return view;
    }
}
