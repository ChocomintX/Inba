package com.example.wx.inba.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wx.inba.R;
import com.example.wx.inba.model.Ba;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class JinBaAdapter extends BaseAdapter {
    private List<Ba> list;
    private Context context;

    public JinBaAdapter(List<Ba> list, Context context) {
        this.list = list;
        this.context = context;
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
        view=LayoutInflater.from(context).inflate(R.layout.jinba_item,viewGroup,false);
        RoundedImageView jinba_img=(RoundedImageView)view.findViewById(R.id.jinba_img);
        TextView jinba_name=(TextView) view.findViewById(R.id.jinba_name);
        TextView jinba_tiezi=(TextView) view.findViewById(R.id.jinba_tiezi);

        Ba ba=list.get(i);
        jinba_name.setText(ba.getName());
        jinba_tiezi.setText("这个吧的介绍");
        Glide.with(context).load(ba.getImgpath()).into(jinba_img);
        return view;
    }
}
