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
import com.example.wx.inba.model.InAnswer;
import com.example.wx.inba.model.UserInfo;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import static android.content.ContentValues.TAG;

public class FansHomeAdapter extends BaseAdapter implements View.OnClickListener {
    private List<UserInfo> list;
    private Context context;
    private CallBack callBack;

    public FansHomeAdapter(List<UserInfo> list, Context context, CallBack callBack) {
        this.list = list;
        this.context = context;
        this.callBack=callBack;
    }

    @Override
    public void onClick(View view) {
        callBack.Click(view);
    }

    public interface CallBack{
        public void Click(View v);
    }

    public class ViewHolder{
        RoundedImageView head;
        TextView name;
        TextView btn;
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
            view= LayoutInflater.from(context).inflate(R.layout.fans_home_item,viewGroup,false);
            viewHolder=new ViewHolder();

            viewHolder.head=(RoundedImageView)view.findViewById(R.id.fans_home_head) ;
            viewHolder.name=(TextView) view.findViewById(R.id.fans_home_name) ;
            viewHolder.btn=(TextView) view.findViewById(R.id.fans_home_fansbtn) ;

            view.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)view.getTag();
        }

        UserInfo ui=list.get(i);

        viewHolder.name.setText(ui.getName());
        viewHolder.btn.setOnClickListener(this);
        Glide.with(context).load(ui.getHead()).into(viewHolder.head);

        return view;
    }
}

