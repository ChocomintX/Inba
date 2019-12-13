package com.example.wx.inba.util;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wx.inba.R;
import com.example.wx.inba.model.Report;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import java.util.List;

public class WodeMessageAdapter extends RecyclerView.Adapter<WodeMessageAdapter.ViewHolder> {
    private List<Report> list;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView time;
        TextView content;
        RelativeLayout bg;

        public ViewHolder (View view)
        {
            super(view);
            content = (TextView) view.findViewById(R.id.wode_message_title);
            time = (TextView) view.findViewById(R.id.wode_message_time);
            bg=(RelativeLayout)view.findViewById(R.id.wode_message_bg);
        }


    }

    public WodeMessageAdapter(List<Report> list) {
        this.list=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wode_message_item,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);

        final String content=list.get(i).getReason();
        holder.bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new XPopup.Builder(viewGroup.getContext()).asConfirm("详细信息", content,
                        new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                            }
                        })
                        .show();
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Report report=list.get(i);

        if(report.getReason().length()<16){
            viewHolder.content.setText(report.getReason());
        }else{
            viewHolder.content.setText(report.getReason());
        }
        viewHolder.time.setText("收到于 "+report.getReporttime());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
