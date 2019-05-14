package com.itcast.whw.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.itcast.whw.MainActivity;
import com.itcast.whw.R;
import com.itcast.whw.activity.search_near.NearMapActivity;
import com.itcast.whw.tool.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * 收藏界面的适配器
 */
public class CollectRecycleAdapter extends RecyclerView.Adapter<CollectRecycleAdapter.ViewHolder> {

    private List<String> strList;
    private HashSet<String> collect_set = new HashSet<>();
    private int COLLECTED = 1;
    private int type;
    private List<ViewHolder> viewHolders = new ArrayList<>();
    private boolean isLongClicked = false;

    private boolean isChecked = false;

    private Context context;
    private boolean isCanAble = true;
    private View view;
    private SharedPreferences collect_info_set;

    public CollectRecycleAdapter(List<String> strList, Context context) {
        this.strList = strList;
        this.context = context;
    }

    public CollectRecycleAdapter(List<String> strList, Context context, View view) {
        this.strList = strList;
        this.context = context;
        this.view = view;
    }

    public void setCollect_set(HashSet<String> collect_set) {
        this.collect_set = collect_set;
    }

    @Override
    public int getItemViewType(int position) {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public HashSet<String> getCollect_set() {
        return collect_set;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int itemViewType = getItemViewType(viewType);
        View view;
        if (itemViewType == COLLECTED) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collected_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_item, parent, false);
        }

        ViewHolder viewHolder = new ViewHolder(view);
        viewHolders.add(viewHolder);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String function = strList.get(position);
        if (type == COLLECTED) {
            //已收藏界面
            holder.collect_function.setText(function);
            final String function_text = holder.collect_function.getText().toString();

            //长按事件
            holder.collect_function.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (isChecked) {
                        for (ViewHolder viewHolder : viewHolders) {
                            viewHolder.check_collect.setVisibility(View.VISIBLE);
                        }
                        isChecked = false;
                        CollectRecycleAdapter.this.view.setVisibility(View.VISIBLE);
                    } else {
                        for (ViewHolder viewHolder : viewHolders) {
                            viewHolder.check_collect.setVisibility(View.GONE);
                            viewHolder.check_collect.setChecked(false);
                        }
                        isChecked = true;
                        isLongClicked = false;
                        CollectRecycleAdapter.this.view.setVisibility(View.GONE);
                    }

                    return true;
                }
            });

            //点击事件
            holder.collect_function.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String collect_text = holder.collect_function.getText().toString();
                    Log.d("CollectRecycleAdapter", collect_text);
                    if(collect_text.equals("查看附近")){
                        Activity activity = (Activity) context;
                        activity.startActivity(new Intent(context,NearMapActivity.class));
                    }
                }
            });

            holder.check_collect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked) {
                        collect_set.add(function_text);
                    } else {
                        collect_set.remove(function_text);
                    }
                }
            });

            //收藏移除
            CollectRecycleAdapter.this.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!collect_set.isEmpty()) {
                        collect_info_set = context.getSharedPreferences("collect_info", Context.MODE_PRIVATE);
                        HashSet<String> collected_set = (HashSet<String>) collect_info_set.getStringSet("collect", null);
                        for (String function : collect_set) {
                            strList.remove(function);
                            if (collected_set != null) {
                                collected_set.remove(function);
                            }
                        }

                        collect_info_set.edit().putStringSet("collect", collected_set).apply();
                        for (String collect : collected_set) {
                            Log.d("CollectRecycleAdapter", collect);
                        }

                    }
                    for (ViewHolder viewHolder : viewHolders) {
                        viewHolder.check_collect.setChecked(false);
                    }
                    notifyDataSetChanged();
                    if(strList.isEmpty()){
                        Activity activity = (Activity) context;
                        Intent intent = new Intent(context, MainActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                    }
                }
            });
        } else {
            //全部收藏界面
            holder.tv_function.setText(function);
            final String function_text = holder.tv_function.getText().toString();
            holder.cb_function.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        collect_set.add(function_text);
                        Log.d("CollectionActivity", "collect_set.size():" + collect_set.size());
                    } else {
                        collect_set.remove(function_text);
                        Log.d("CollectionActivity", "collect_set.size():" + collect_set.size());
                    }
                }
            });
            if (isCanAble) {
                if (!collect_set.isEmpty()) {
                    for (String function_str : collect_set) {
                        if (function_str.equals(function_text)) {
                            holder.cb_function.setEnabled(false);
                            holder.cb_function.setChecked(true);
                        }
                    }
                }
            }
        }


    }

    @Override
    public int getItemCount() {
        return strList.size();
    }

    public void setFunctionAble(boolean isCanAble) {
        this.isCanAble = isCanAble;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_function;
        CheckBox cb_function;

        TextView collect_function;
        CheckBox check_collect;

        public ViewHolder(View itemView) {
            super(itemView);
            if (type == COLLECTED) {
                collect_function = itemView.findViewById(R.id.collect_function);
                check_collect = itemView.findViewById(R.id.check_collect);
            } else {
                tv_function = itemView.findViewById(R.id.tv_function);
                cb_function = itemView.findViewById(R.id.cb_function);
            }

        }
    }
}
