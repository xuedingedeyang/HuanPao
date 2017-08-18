package com.yang.huanpao.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.yang.huanpao.R;
import com.yang.huanpao.base.BaseActivity;
import com.yang.huanpao.bean.User;
import com.yang.huanpao.config.Const;
import com.yang.huanpao.event.UserModel;
import com.yang.huanpao.event.i.LoginListener;
import com.yang.huanpao.util.SharePreferencesUtil;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by yang on 2017/8/10.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private EditText edit_account,edit_password;
    private Button btn_login,btn_register,login_by_qq;
    Tencent mTencent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edit_account = (EditText) findViewById(R.id.edit_account);
        edit_password = (EditText) findViewById(R.id.edit_password);
        edit_account.setText("123456");
        edit_password.setText("123456");
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        login_by_qq = (Button) findViewById(R.id.login_by_qq);
        login_by_qq.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                String username = edit_account.getText().toString();
                String password = edit_password.getText().toString();
                UserModel.getInstance().login(this, username, password, new LoginListener() {
                    @Override
                    public void onSuccess(User user) {
                        toast("登录成功");
                        SharePreferencesUtil.put(LoginActivity.this,"isLogin",true);
                        startActivity(MainActivity2.class,null,true);
                    }

                    @Override
                    public void onFailure(BmobException e) {
                        if (!isNetWorkConnected()){
                            toast("无法连接网络");
                        }else {
                            toast("登录失败，请重试  " + e.getMessage());
                        }
                    }
                });
                break;
            case R.id.btn_register:
                edit_account.setText("");
                edit_account.setText("");
                startActivity(RegisterActivity.class,null,false);
                break;
            case R.id.login_by_qq:
                mTencent = Tencent.createInstance(Const.Tencent_APP_ID,this.getApplicationContext());
                if (!mTencent.isSessionValid()){
                    mTencent.login(this, "all", new IUiListener() {
                        @Override
                        public void onComplete(Object o) {

                        }

                        @Override
                        public void onError(UiError uiError) {
                            log("qq登录失败");
                        }

                        @Override
                        public void onCancel() {
                            log("QQ登录取消");
                        }
                    });
                }
                break;
        }
    }
}
