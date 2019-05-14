package com.itcast.whw;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.TextBadgeItem;
import com.itcast.whw.activity.BaseActivity;
import com.itcast.whw.activity.CollectionActivity;
import com.itcast.whw.activity.SearchActivity;
import com.itcast.whw.activity.ShortCutActivity;
import com.itcast.whw.adapter.SectionPageAdapter;
import com.itcast.whw.fragment.HomeFragment;
import com.itcast.whw.fragment.MineFragment;
import com.itcast.whw.fragment.ToolFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * APP主界面
 */
public class MainActivity extends BaseActivity {

    private Toolbar toolBar;
    private ViewPager viewPager;
    private BottomNavigationBar bottom_navigation_bar;
    private boolean isEmptyCollect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        HashSet<Map<String,Integer>> collect_info = (HashSet<Map<String,Integer>>)intent.getSerializableExtra("collect_info");

        if(collect_info!=null){
            isEmptyCollect = collect_info.isEmpty();
        }

        initView();
        setSupportActionBar(toolBar);
        //去除ToolBar的标题
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        List<Fragment> fragmentList = new ArrayList<>();

        HomeFragment homeFragment = new HomeFragment();
        ToolFragment toolFragment = new ToolFragment();
        MineFragment mineFragment = new MineFragment();

        fragmentList.add(homeFragment);
        fragmentList.add(toolFragment);
        fragmentList.add(mineFragment);
        viewPager.setAdapter(new SectionPageAdapter(getSupportFragmentManager(), fragmentList));
        //监听ViewPager页面变化
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            /**
             * 在屏幕滚动过程中不断调用
             * @param position 页面翻动成功后，i表示其当前页面
             * @param positionOffset 当前页面滑动比例
             * @param positionOffsetPixels 当前页面滑动像素
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }


            /**
             * 翻动页面调用此方法
             * @param position 代表哪个页面被选中
             */
            @Override
            public void onPageSelected(int position) {
                bottom_navigation_bar.selectTab(position);
            }

            /**
             * 这个方法在手指操作屏幕的时候发生变化。有三个值：0（END）,1(PRESS) , 2(UP) 。
             * 当用手指滑动翻页时，手指按下去的时候会触发这个方法，state值为1，手指抬起时，
             * 如果发生了滑动（即使很小），这个值会变为2，然后最后变为0 。总共执行这个方法三次。
             * 一种特殊情况是手指按下去以后一点滑动也没有发生，这个时候只会调用这个方法两次，state值分别是1,0 。
             * @param state
             */
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        MenuItem menuItem = menu.findItem(R.id.searchView);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        //搜索图标是否显示在搜索框内
        searchView.setIconifiedByDefault(true);
        //设置搜索框展开时是否显示提交按钮，可不显示
        searchView.setSubmitButtonEnabled(true);
        //让键盘的回车键设置成搜索
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        //搜索框是否展开，false表示展开
        searchView.setIconified(false);
        searchView.onActionViewExpanded();
        searchView.clearFocus();
        //设置提示词
        searchView.setQueryHint("搜索功能...");
        EditText editText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        editText.setFocusable(false);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.shortcut://快捷方式
                startActivity(new Intent(this,ShortCutActivity.class));
                break;
            case R.id.all_functions://快捷方式
                startActivity(new Intent(this,CollectionActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        bottom_navigation_bar = findViewById(R.id.bottom_navigation_bar);

        /**
         * 导航基础设置 包括按钮选中效果 导航栏背景色等
         */
        bottom_navigation_bar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        })
                .setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        //.setActiveColor("#ffffff")//选中颜色
        //.setInActiveColor("#2B2B2B")//未选中颜色
        //.setBarBackgroundColor("#ffffff");//导航栏背景色
        TextBadgeItem badgeItem = new TextBadgeItem()
                .setBorderWidth(2)
                .setTextColor(Color.BLACK)
                .setBackgroundColor(Color.RED)
                .setText("99");
        /**
         *添加导航按钮
         */
        bottom_navigation_bar
                .addItem(new BottomNavigationItem(R.mipmap.ic_launcher, "首页"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_launcher, "分类"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_launcher, "测试"))
                .initialise();//initialise 一定要放在 所有设置的最后一项
    }
}
