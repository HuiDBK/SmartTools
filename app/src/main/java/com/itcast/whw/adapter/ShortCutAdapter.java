package com.itcast.whw.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itcast.whw.R;
import com.itcast.whw.tool.LogUtil;

import java.util.List;


/**
 * 快捷方式界面的适配器
 */
public class ShortCutAdapter extends RecyclerView.Adapter<ShortCutAdapter.ViewHolder> {

    private List<String> strList;

    public ShortCutAdapter(List<String> strList){
        this.strList = strList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_function_head;
        TextView tv_function;
        RelativeLayout rl_layout;
        RelativeLayout rl;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_function_head = itemView.findViewById(R.id.tv_function_head);
            tv_function = itemView.findViewById(R.id.tv_function);
            rl_layout = itemView.findViewById(R.id.rl_layout);
            rl = itemView.findViewById(R.id.rl);
        }
    }

    @Override
    public ShortCutAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shortcut_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ShortCutAdapter.ViewHolder holder, int position) {
        String str = strList.get(position);
        //截取第一个汉字
        String substring = str.substring(0, 1);
        holder.tv_function_head.setText(substring);
        holder.tv_function.setText(str);

        if(position % 2 == 0){
            //偶数item
            holder.rl_layout.setBackgroundColor(Color.parseColor("#ffffff"));
        }else{
            //奇数item
            holder.rl_layout.setBackgroundColor(Color.parseColor("#e0e8e8"));
            holder.rl.setBackgroundResource(R.drawable.background_pink_light);
            holder.tv_function_head.setBackgroundResource(R.drawable.background_pink);
        }
    }

    @Override
    public int getItemCount() {
        return strList.size();
    }
}

