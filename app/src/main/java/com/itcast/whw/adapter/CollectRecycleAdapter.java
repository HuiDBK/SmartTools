package com.itcast.whw.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.itcast.whw.R;
import com.itcast.whw.tool.LogUtil;

import java.util.List;

/**
 * 收藏界面的适配器
 */
public class CollectRecycleAdapter extends RecyclerView.Adapter<CollectRecycleAdapter.ViewHolder> {

    private List<String> strList;
    private Context context;

    public CollectRecycleAdapter(List<String> strList, Context context){
        this.strList = strList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View collection_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(collection_view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String str = strList.get(position);
        holder.tv_function.setText(str);
        holder.cb_function.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(context,isChecked + "" + strList.get(position),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return strList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_function;
        CheckBox cb_function;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_function = itemView.findViewById(R.id.tv_function);
            cb_function = itemView.findViewById(R.id.cb_function);
        }
    }
}
