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
import com.example.wx.inba.model.Ba;
import com.example.wx.inba.model.Tie;
import com.example.wx.inba.model.UserInfo;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

public class WodeTieAdapter extends BaseAdapter implements View.OnClickListener {

    private List<Tie> list;
    private List<Ba> balist;
    private Context context;
    private WodeTieAdapter.CallBack callBack;

    public WodeTieAdapter(List<Tie> list, List<Ba> balist, Context context, WodeTieAdapter.CallBack callBack) {
        this.list = list;
        this.balist=balist;
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
        TextView content;
        TextView ba;
        TextView time;
        TextView answernum;
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
        WodeTieAdapter.ViewHolder viewHolder=null;
        if(view==null){
            view= LayoutInflater.from(context).inflate(R.layout.wode_tie_item_1,viewGroup,false);
            viewHolder=new WodeTieAdapter.ViewHolder();

            viewHolder.ba=(TextView)view.findViewById(R.id.wode_tie_item1_ba);
            viewHolder.time=(TextView)view.findViewById(R.id.wode_tie_item1_time);
            viewHolder.content=(TextView)view.findViewById(R.id.wode_tie_item1_content);
            viewHolder.answernum=(TextView)view.findViewById(R.id.wode_tie_item1_answernum);

            view.setTag(viewHolder);
        }else{
            viewHolder=(WodeTieAdapter.ViewHolder)view.getTag();
        }

        Tie tie=list.get(i);
        Ba ba=balist.get(i);

        viewHolder.ba.setText(ba.getName()+"吧");

        String s=tie.getSettime().split(" ")[0];

        viewHolder.time.setText(s.split("-")[1]+"月"+s.split("-")[2]+"日");
        viewHolder.content.setText(tie.getContent());
        viewHolder.answernum.setText(tie.getAnswernum()+"");
        return view;
    }

}
