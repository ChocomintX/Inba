package com.example.wx.inba.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wx.inba.R;
import com.example.wx.inba.model.Tie;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import static com.makeramen.roundedimageview.RoundedImageView.TAG;

public class CollectionAdapter extends BaseAdapter {
    private List<Tie> list;
    private Context context;


    public CollectionAdapter(List<Tie> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public class ViewHolder{
        TextView title;
        TextView time;
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
            view= LayoutInflater.from(context).inflate(R.layout.wode_collection_item,viewGroup,false);
            viewHolder=new ViewHolder();

            viewHolder.title=(TextView) view.findViewById(R.id.collection_title) ;
            viewHolder.time=(TextView) view.findViewById(R.id.collection_time) ;

            view.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)view.getTag();
        }

        Tie tie=list.get(i);
        viewHolder.title.setText(tie.getTitle());
        viewHolder.time.setText("最后回复于"+tie.getAnswertime());

        return view;
    }
}
