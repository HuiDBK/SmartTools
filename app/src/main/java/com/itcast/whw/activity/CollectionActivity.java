package com.itcast.whw.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.itcast.whw.R;
import com.itcast.whw.adapter.CollectRecycleAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 工具收藏界面
 */
public class CollectionActivity extends AppCompatActivity {

    private Toolbar tool_bar;
    private CollapsingToolbarLayout ctl_title;
    private AppBarLayout abl_title;
    private RecyclerView rv_collection;
    private List<String> strList;

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
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rv_collection.setLayoutManager(gridLayoutManager);
        CollectRecycleAdapter adapter = new CollectRecycleAdapter(strList,this);
        rv_collection.setAdapter(adapter);
    }

    private void initView() {
        tool_bar = (Toolbar) findViewById(R.id.tool_bar);
        ctl_title = (CollapsingToolbarLayout) findViewById(R.id.ctl_title);
        abl_title = (AppBarLayout) findViewById(R.id.abl_title);
        rv_collection = (RecyclerView) findViewById(R.id.rv_collection);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //后退键的点击事件
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化数据
     */
    public void getInitData() {
        strList = new ArrayList<>();
        for(int i = 0;i < 10;i++){
            strList.add("校园外卖");
            strList.add("表情制作");
            strList.add("付费音乐下载");
            strList.add("红包");
            strList.add("汇率");
        }
    }
}
