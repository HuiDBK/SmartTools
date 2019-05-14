package com.itcast.whw.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.itcast.whw.R;
import com.itcast.whw.activity.CollectionActivity;
import com.itcast.whw.activity.LoginActivity;
import com.itcast.whw.adapter.CollectRecycleAdapter;
import com.itcast.whw.tool.DensityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 首页
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    private ImageView iv_collection;
    private RelativeLayout relativeLayout;
    private static final String TAG = HomeFragment.class.getSimpleName();

    private SharedPreferences visitors_sp;
    private SharedPreferences collect_info_sp;
    private List<String> collected_list;

    /**
     * 临时标记
     * true:游客用户已登录
     * false:游客用户未登录
     */
    private boolean isVisitors;

    /**
     * 临时标记
     * true:用户有收藏记录
     * false:用户无收藏记录
     */
    private boolean flag = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        collect_info_sp = getActivity().getSharedPreferences("collect_info",Context.MODE_PRIVATE);
        View view;
        visitors_sp = getActivity().getSharedPreferences("visitors_info",Context.MODE_PRIVATE);
        //取出游客登录标志
        isVisitors = visitors_sp.getBoolean("isVisitors", false);

        //取出收藏数据
        Set<String> collect = collect_info_sp.getStringSet("collect", null);

        if(collect!=null){
            flag =(!collect.isEmpty());
            Log.d(TAG, "CollectionActivity:" + flag);
        }
        if (flag) {
            //用户有收藏数据，显示该界面
            view = inflater.inflate(R.layout.fragment_home2, container, false);
            RecyclerView recycler_collected = view.findViewById(R.id.recycler_collected);
            FloatingActionButton collect_delete = view.findViewById(R.id.collect_delete);
            collected_list = new ArrayList<>();
            for(String collected : collect){
                collected_list.add(collected);
            }
            CollectRecycleAdapter collectAdapter = new CollectRecycleAdapter(collected_list,getActivity(),collect_delete);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
            recycler_collected.setLayoutManager(gridLayoutManager);
            collectAdapter.setType(1);
            recycler_collected.setAdapter(collectAdapter);
        } else {
            //暂无收藏，显示该界面
            view = inflater.inflate(R.layout.fragment_home, container, false);
            //初始化控件
            initView(view);
            //初始化动画
            initAnimator();
        }

        return view;
    }

    /**
     * 初始化动画效果
     */
    private void initAnimator() {
        //如果用户没有收藏信息，显示暂无收藏(动画)
        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(iv_collection, "rotation", 0, 360);
        //计算平移距离(相对布局宽度 - 收藏图标宽度 - 左边距 - 右边距)
        int scaleX = DensityUtil.pxToDip(getContext(), 220 - 50 - 5 - 5);//将px转为换dp
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(iv_collection, "translationX", scaleX);

        //通过动画集合组合播放
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(rotationAnimator, translationAnimator);
        //设置动画时间
        animatorSet.setDuration(1500);
        //开始播放
        animatorSet.start();
    }

    private void initView(View view) {
        iv_collection = (ImageView) view.findViewById(R.id.iv_collection);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);

        iv_collection.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * 监听点击
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_collection:
                //点击收藏图标，判断用户是否登录，若未登录，提示登录
                if(isVisitors){
                    //用户已登录，显示工具库，供用户收藏
                    startActivity(new Intent(getActivity(), CollectionActivity.class));
                    getActivity().finish();
                }else{
                    //跳转登录界面
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                    getActivity().finish();
                }
                break;
        }
    }
}
