package com.itcast.whw.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.itcast.whw.R;
import com.itcast.whw.activity.CollectionActivity;
import com.itcast.whw.activity.LoginActivity;
import com.itcast.whw.tool.DensityUtil;

/**
 * 首页
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    private ImageView iv_collection;
    private RelativeLayout relativeLayout;
    private static final String TAG = HomeFragment.class.getSimpleName();

    /**
     * 临时标记
     * true:用户有收藏记录
     * false:用户无收藏记录
     */
    private boolean flag = false;

    /**
     * 临时标记
     * true:用户已登录
     * false:用户未登录
     */
    private boolean loginFlag = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        if (flag) {
            //用户有收藏数据，显示该界面
            view = inflater.inflate(R.layout.fragment_home2, container, false);

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
                if(loginFlag){
                    //用户已登录，显示工具库，供用户收藏
                    startActivity(new Intent(getActivity(), CollectionActivity.class));
                }else{
                    //跳转登录界面
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                }
                break;
        }
    }
}
