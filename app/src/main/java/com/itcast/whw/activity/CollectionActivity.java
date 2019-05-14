package com.itcast.whw.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.itcast.whw.MainActivity;
import com.itcast.whw.R;
import com.itcast.whw.adapter.CollectRecycleAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 工具收藏界面
 */
public class CollectionActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar tool_bar;
    private CollapsingToolbarLayout ctl_title;
    private AppBarLayout abl_title;
    private RecyclerView rv_collection;
    private List<String> strList;
    private FloatingActionButton collect_fab;
    private CollectRecycleAdapter collect_adapter;
    private SharedPreferences collect_info_sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        initView();

        //初始化数据
        getInitData();

        setSupportActionBar(tool_bar);

        ActionBar actionBar = getSupportActionBar();
        //开启返回键
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rv_collection.setLayoutManager(gridLayoutManager);
        collect_adapter = new CollectRecycleAdapter(strList, this);
        collect_adapter.setType(0);
        rv_collection.setAdapter(collect_adapter);

        collect_info_sp = getSharedPreferences("collect_info",MODE_PRIVATE);
        HashSet<String> collect = (HashSet<String>)collect_info_sp.getStringSet("collect", null);

        if(collect!=null){
            for(String str: collect){
                Log.d("CollectionActivity", str);
            }
            collect_adapter.setCollect_set(collect);
            collect_adapter.setFunctionAble(true);
            collect_adapter.setType(0);
            collect_adapter.notifyDataSetChanged();
        }
    }

    private void initView() {
        tool_bar = (Toolbar) findViewById(R.id.tool_bar);
        ctl_title = (CollapsingToolbarLayout) findViewById(R.id.ctl_title);
        abl_title = (AppBarLayout) findViewById(R.id.abl_title);
        rv_collection = (RecyclerView) findViewById(R.id.rv_collection);
        collect_fab = (FloatingActionButton) findViewById(R.id.collect_fab);
        collect_fab.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //后退键的点击事件
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化数据
     */
    public void getInitData() {
        strList = new ArrayList<>();
            strList.add("查看附近");
            strList.add("表情制作");
            strList.add("付费音乐下载");
            strList.add("红包");
            strList.add("汇率");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.collect_fab:
                //实现收藏功能
                HashSet<String> collect_set = collect_adapter.getCollect_set();
                if(collect_set!=null){
                    for(String str: collect_set){
                        Log.d("CollectionActivity", str);
                    }
                    collect_adapter.setFunctionAble(true);
                    collect_adapter.notifyDataSetChanged();
                }

                Intent intent = new Intent(CollectionActivity.this,MainActivity.class);
                collect_info_sp = getSharedPreferences("collect_info",MODE_PRIVATE);
                SharedPreferences.Editor editor = collect_info_sp.edit().putStringSet("collect", collect_set);
                editor.apply();
                startActivity(intent);
                finish();
                break;
        }
    }
}
