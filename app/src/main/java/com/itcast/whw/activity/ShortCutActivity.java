package com.itcast.whw.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.itcast.whw.R;
import com.itcast.whw.adapter.ShortCutAdapter;

import java.util.ArrayList;
import java.util.List;

public class ShortCutActivity extends AppCompatActivity {

    private Toolbar toolBar;
    private RecyclerView recyclerView;
    private List<String> strList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_cut);
        initView();
        setSupportActionBar(toolBar);

        //初始化数据
        getInitData();

        //设置适配器
        ShortCutAdapter adapter = new ShortCutAdapter(strList);
        recyclerView.setAdapter(adapter);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void initView() {
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        recyclerView = findViewById(R.id.recyclerView);
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
