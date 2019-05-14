package com.itcast.whw.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itcast.whw.MainActivity;
import com.itcast.whw.R;
import com.itcast.whw.tool.JellyInterpolator;

/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private TextView mBtnLogin;

    private View progress;

    private View mInputLayout;

    private float mWidth, mHeight;

    private LinearLayout mName, mPsw;
    private TextView tv_register;
    private ImageView login_back;
    private LinearLayout input_layout_name;
    private LinearLayout input_layout_psw;
    private ProgressBar progressBar2;
    private TextView visitors_login;
    private TextView main_btn_login;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        mBtnLogin = (TextView) findViewById(R.id.main_btn_login);
        progress = findViewById(R.id.layout_progress);
        mInputLayout = findViewById(R.id.input_layout);
        mName = (LinearLayout) findViewById(R.id.input_layout_name);
        mPsw = (LinearLayout) findViewById(R.id.input_layout_psw);
        tv_register = findViewById(R.id.tv_register);
        login_back = findViewById(R.id.login_back);

        mBtnLogin.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        login_back.setOnClickListener(this);
        input_layout_name = (LinearLayout) findViewById(R.id.input_layout_name);
        input_layout_name.setOnClickListener(this);
        input_layout_psw = (LinearLayout) findViewById(R.id.input_layout_psw);
        input_layout_psw.setOnClickListener(this);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar2.setOnClickListener(this);
        visitors_login = (TextView) findViewById(R.id.visitors_login);
        visitors_login.setOnClickListener(this);
        main_btn_login = (TextView) findViewById(R.id.main_btn_login);
        main_btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_btn_login:
                //点击登录按钮，进行登录
                mWidth = mBtnLogin.getMeasuredWidth();
                mHeight = mBtnLogin.getMeasuredHeight();

                mName.setVisibility(View.INVISIBLE);
                mPsw.setVisibility(View.INVISIBLE);

                inputAnimator(mInputLayout, mWidth, mHeight);

                //校验登录信息
                verifyLoginInfo();

                break;
            case R.id.visitors_login:
                //游客登录
                sp = getSharedPreferences("visitors_info",MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit().putBoolean("isVisitors", true);
                editor.apply();
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                break;
            case R.id.tv_register:
                //点击注册按钮，跳转注册界面
                startActivity(new Intent(this, RegisterActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                break;
            case R.id.login_back:
                //点击返回按钮，关闭当前界面
                finish();
                break;
        }
    }

    /**
     * 校验登录信息
     */
    private void verifyLoginInfo() {

    }

    private void inputAnimator(final View view, float w, float h) {

        AnimatorSet set = new AnimatorSet();

        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view
                        .getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout,
                "scaleX", 1f, 0.5f);
        set.setDuration(1000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                progress.setVisibility(View.VISIBLE);
                progressAnimator(progress);
                mInputLayout.setVisibility(View.INVISIBLE);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(2000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.setVisibility(View.GONE);
                            }
                        });
                    }
                }).start();

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2);
        animator3.setDuration(1000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();

    }
}
