package com.example.wx.inba.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wx.inba.R;
import com.example.wx.inba.model.Ba;
import com.example.wx.inba.model.Tie;
import com.example.wx.inba.model.UserInfo;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import static android.content.ContentValues.TAG;

public class ShouyeAdapter extends BaseAdapter implements View.OnClickListener {
    private List<Tie> list;
    private Context context;
    private List<Ba> baList;
    private List<UserInfo> userInfoList;
    private CallBack callBack;

    public ShouyeAdapter(List<Tie> list, List<Ba> baList, List<UserInfo> userInfoList, CallBack callBack, Context context) {
        this.list = list;
        this.context = context;
        this.userInfoList=userInfoList;
        this.baList=baList;
        this.callBack=callBack;
    }

    @Override
    public void onClick(View view) {
        callBack.Click(view);
    }

    public interface CallBack{
        public void Click(View v);
    }

    public class ViewHolder {
        RoundedImageView shouye_head;
        TextView shouye_name;
        TextView shouye_content;
        TextView shouye_ba;
        TextView shouye_sharenum;
        TextView shouye_answernum;
        TextView shouye_thumbnum;
        TextView shouye_settime;
        RoundedImageView img1;
        RoundedImageView img2;
        RoundedImageView img3;
        ImageView shouye_zan;
        ImageView shouye_cai;
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
            view=LayoutInflater.from(context).inflate(R.layout.shouye_item,viewGroup,false);
            viewHolder=new ViewHolder();

            viewHolder.shouye_head=(RoundedImageView)view.findViewById(R.id.shouye_head);
            viewHolder.shouye_name=(TextView) view.findViewById(R.id.shouye_name);
            viewHolder.shouye_content=(TextView) view.findViewById(R.id.shouye_content);
            viewHolder.shouye_ba=(TextView)view.findViewById(R.id.shouye_ba);
            viewHolder.shouye_sharenum=(TextView)view.findViewById(R.id.shouye_sharenum);
            viewHolder.shouye_answernum=(TextView)view.findViewById(R.id.shouye_answernum);
            viewHolder.shouye_thumbnum=(TextView)view.findViewById(R.id.shouye_thumbnum);
            viewHolder.shouye_settime=(TextView)view.findViewById(R.id.shouye_settime);
            viewHolder.img1=(RoundedImageView) view.findViewById(R.id.shouye_img1);
            viewHolder.img2=(RoundedImageView) view.findViewById(R.id.shouye_img2);
            viewHolder.img3=(RoundedImageView) view.findViewById(R.id.shouye_img3);
            viewHolder.shouye_zan=(ImageView)view.findViewById(R.id.shouye_zan);
            viewHolder.shouye_cai=(ImageView)view.findViewById(R.id.shouye_cai);

            view.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)view.getTag();
        }


        Tie tie=list.get(i);
        Ba ba=baList.get(i);
        UserInfo ui=userInfoList.get(i);

        viewHolder.shouye_content.setText(tie.getTitle()+"                                                                                                                ");
        viewHolder.shouye_ba.setText(ba.getName());
        viewHolder.shouye_answernum.setText(tie.getAnswernum()+"");
        viewHolder.shouye_thumbnum.setText(tie.getThumbnum()+"");
        viewHolder.shouye_settime.setText("发布于"+tie.getSettime());

        if(ui.getHead()!=""){
            Glide.with(context).load(ui.getHead()).into(viewHolder.shouye_head);
        }

        if(tie.getImg1().equals("")){
            viewHolder.img1.setVisibility(View.GONE);
        }else{
            viewHolder.img1.setVisibility(View.VISIBLE);
            Glide.with(context).load(tie.getImg1()).into(viewHolder.img1);
        }

        if(tie.getImg2().equals("")){
            viewHolder.img2.setVisibility(View.GONE);
        }else{
            viewHolder.img2.setVisibility(View.VISIBLE);
            Glide.with(context).load(tie.getImg2()).into(viewHolder.img2);
        }

        if(tie.getImg3().equals("")){
            viewHolder.img3.setVisibility(View.GONE);
        }else{
            viewHolder.img3.setVisibility(View.VISIBLE);
            Glide.with(context).load(tie.getImg3()).into(viewHolder.img3);
        }

        viewHolder.shouye_name.setText(ui.getName());
        viewHolder.shouye_ba.setOnClickListener(this);
        viewHolder.shouye_ba.setTag(i);
        viewHolder.shouye_zan.setOnClickListener(this);
        viewHolder.shouye_zan.setTag(i);
        viewHolder.shouye_cai.setOnClickListener(this);
        viewHolder.shouye_cai.setTag(i);
        return view;
    }
}
