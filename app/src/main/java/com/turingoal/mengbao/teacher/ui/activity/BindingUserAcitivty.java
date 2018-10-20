package com.turingoal.mengbao.teacher.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.lzy.okgo.request.PostRequest;
import com.turingoal.common.app.TgApplication;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseActivity;
import com.turingoal.common.bean.TgResponseBean;
import com.turingoal.common.utils.TgDialogUtil;
import com.turingoal.common.utils.TgHttpCallback;
import com.turingoal.common.utils.TgHttpUtil;
import com.turingoal.common.widget.ClearEditText;
import com.turingoal.mengbao.common.constants.TgConstantLoginType;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;
import com.turingoal.mengbao.teacher.constants.ConstantUrls;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 绑定用户
 */
@Route(path = ConstantActivityPath.BINDING_USER)
public class BindingUserAcitivty extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.etUsername)
    ClearEditText etUsername; // 用户
    @BindView(R.id.tilUsername)
    TextInputLayout tilUsername; // 用户控制
    @BindView(R.id.etPassword)
    EditText etPassword; // 密码
    @BindView(R.id.tilPassword)
    TextInputLayout tilPassword; // 密码控制
    @BindView(R.id.cbRememberPass)
    CheckBox cbRememberPass; // 记住密码
    @BindView(R.id.btnBinding)
    Button btnBinding; // 绑定
    @Autowired
    Bundle bundle;
    private static final int MAX_STRING_LEN = 24; // 用户名和密码最大长度
    private static final int MIN_USERNAME_LEN = 5; // 用户名最小长度
    private static final int MIN_PASSWORD_LEN = 6; // 密码最小长度
    private boolean isUsernameEnable = false; // 用户名是否正确
    private boolean isPasswordEnable = false; // 密码是否正确

    @Override
    protected int getLayoutID() {
        return R.layout.activity_binding_user;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_binding_user); //  Title
        cbRememberPass.setChecked(true); // 默认记住密码
        etUsername.addTextChangedListener(userNameTextWatcher); // 用户名内容监
        etPassword.addTextChangedListener(passwordTextWatcher); // 密码内容监听
        tilPassword.setPasswordVisibilityToggleEnabled(true); // 点击显示密码
        etUsername.setOnTextClearListener(new ClearEditText.OnTextClearListener() { // 点击了清空按鈕
            @Override
            public void textClear() {
                tilUsername.setErrorEnabled(false);
                isUsernameEnable = false;
                btnBinding.setEnabled(false);
            }
        });
    }

    /**
     * 用户名EditText监听
     */
    private TextWatcher userNameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }

        @Override
        public void onTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }

        @Override
        public void afterTextChanged(final Editable editable) {
            if (editable.toString().trim().length() != 0 && editable.toString().trim().length() < MAX_STRING_LEN) { // 3-24个字
                isUsernameEnable = true;
            } else {
                isUsernameEnable = false;
            }
            if (editable.toString().trim().length() > MAX_STRING_LEN) { // 字数大于最大限制，提示错误
                tilUsername.setError(getString(R.string.string_username_max_hint));
            } else {
                tilUsername.setErrorEnabled(false);
            }
            if (isUsernameEnable && isPasswordEnable) { // 用户名和密码同时正确才可以点
                btnBinding.setEnabled(true); // 启用提交按钮
            } else {
                btnBinding.setEnabled(false); // 按钮不可点击
            }
        }
    };

    /**
     * 密码EditText监听
     */
    private TextWatcher passwordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }

        @Override
        public void onTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }

        @Override
        public void afterTextChanged(final Editable editable) {
            if (editable.toString().trim().length() != 0 && editable.toString().trim().length() < MAX_STRING_LEN) { // 3-24个字
                isPasswordEnable = true;
            } else {
                isPasswordEnable = false;
            }
            if (editable.toString().trim().length() > MAX_STRING_LEN) { // 字数大于最大限制，提示错误
                tilPassword.setError(getString(R.string.string_password_max_hint));
            } else {
                tilPassword.setErrorEnabled(false);
            }
            if (isUsernameEnable && isPasswordEnable) { // 用户名和密码同时正确才可以点
                btnBinding.setEnabled(true); // 启用提交按钮
            } else {
                btnBinding.setEnabled(false); // 按钮不可点击
            }
        }
    };

    /**
     * 登录
     */
    private void login() {
        if (etUsername.getText().toString().trim().length() < MIN_USERNAME_LEN) {
            tilUsername.setError(getString(R.string.string_username_min_hint));
            isUsernameEnable = false;
            btnBinding.setEnabled(false); // 按钮不可点击
            return;
        }
        if (etPassword.getText().toString().trim().length() < MIN_PASSWORD_LEN) {
            tilPassword.setError(getString(R.string.string_password_min_hint));
            isPasswordEnable = false;
            btnBinding.setEnabled(false); // 按钮不可点击
            return;
        }
        loginRequest(bundle.getInt("loginType"), bundle.getString("uid")); // 登录
    }

    /**
     * 登录网络请求
     */
    private void loginRequest(final int loginType, final String uid) {
        final String username = etUsername.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        String path = ConstantUrls.URL_SDK_BINDING_USER_QQ;
        if (TgConstantLoginType.isWeChat(loginType)) {
            path = ConstantUrls.URL_SDK_BINDING_USER_WECHAT;
        } else if (TgConstantLoginType.isSina(loginType)) {
            path = ConstantUrls.URL_SDK_BINDING_USER_SINA;
        }
        PostRequest request = TgHttpUtil.requestPost(path, getHttpTaskKey());
        request.params("uid", uid);
        request.params("username", username);
        request.params("password", password);
        request.execute(new TgHttpCallback(null) {
            @Override
            public void successHandler(final TgResponseBean result) {
                if (result.isSuccess()) { // 成功
                    TgSystemHelper.setUserInfo(result);
                    TgApplication.getTgUserPreferences().setRememberPassword(cbRememberPass.isChecked()); // 是否勾选了记住密码
                    TgApplication.getTgUserPreferences().setUsername(username); // 保存用户名
                    TgApplication.getTgUserPreferences().setLoginPassword(password); // 保存密码
                    TgSystemHelper.handleIntentAndFinish(ConstantActivityPath.MAIN, BindingUserAcitivty.this); // 跳转到主页面,关闭当前页面
                } else {
                    TgDialogUtil.showToast(result.getMsg()); // 登录失败，弹出错误信息
                }
            }
        });
    }

    /**
     * OnClick
     */
    @OnClick({R.id.ivStart, R.id.btnBinding})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.ivStart:
                defaultFinish();
                break;
            case R.id.btnBinding: // 登录
                login();
                break;
            default:
                break;
        }
    }
}
