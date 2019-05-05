package com.itcast.whw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itcast.whw.R;

/**
 * 注册界面
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_register;
    private LinearLayout input_layout_name;
    private LinearLayout input_layout_psw;
    private ProgressBar progressBar2;
    private TextView main_btn_login;
    private ImageView register_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        tv_register = (TextView) findViewById(R.id.tv_register);
        input_layout_name = (LinearLayout) findViewById(R.id.input_layout_name);
        input_layout_psw = (LinearLayout) findViewById(R.id.input_layout_psw);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        main_btn_login = (TextView) findViewById(R.id.main_btn_login);
        register_back = findViewById(R.id.register_back);

        register_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_back:
                //点击了返回按钮，回到登录界面
                startActivity(new Intent(this,LoginActivity.class));
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                finish();
                break;
        }
    }
}
