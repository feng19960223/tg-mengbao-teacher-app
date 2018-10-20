package com.turingoal.mengbao.teacher.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录
 */
@Route(path = ConstantActivityPath.LOGIN)
public class LoginActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ivStart)
    ImageView ivStart; // 返回，登录页面没有返回，需要设置不可见
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
    @BindView(R.id.btnLogin)
    Button btnLogin; // 登陆
    @BindView(R.id.loginWeChat)
    ImageView loginWeChat; // 微信登录
    @BindView(R.id.loginSina)
    ImageView loginSina; // 新浪登录
    @BindView(R.id.loginQQ)
    ImageView loginQQ; // QQ登录
    @Autowired
    String phone; // 从找回密码或这是注册传过来的手机号码
    private static final int MAX_STRING_LEN = 24; // 用户名和密码最大长度
    private static final int MIN_USERNAME_LEN = 5; // 用户名最小长度
    private static final int MIN_PASSWORD_LEN = 6; // 密码最小长度
    private boolean isUsernameEnable = true; // 用户名是否正确
    private boolean isPasswordEnable = true; // 密码是否正确

    @Override
    protected int getLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initialized() {
        // 权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] mPermissionList = new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_LOGS,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.SET_DEBUG_APP,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.GET_ACCOUNTS,
                    Manifest.permission.WRITE_APN_SETTINGS};
            requestPermissions(mPermissionList, 123);
        }
        TgApplication.addForgetActivity(this); // 忘记密码路线添加
        ivStart.setVisibility(View.INVISIBLE); // 返回，登录页面没有返回，需要设置不可见
        tvTitle.setText(R.string.activity_login); //  Title
        // 如果没有安装相应的应用，就将按钮隐藏
        if (!UMShareAPI.get(this).isInstall(this, SHARE_MEDIA.WEIXIN)) {
            loginWeChat.setVisibility(View.GONE);
        }
        if (!UMShareAPI.get(this).isInstall(this, SHARE_MEDIA.SINA)) {
            loginSina.setVisibility(View.GONE);
        }
        if (!UMShareAPI.get(this).isInstall(this, SHARE_MEDIA.QQ)) {
            loginQQ.setVisibility(View.GONE);
        }
        etUsername.setText(TgSystemHelper.getUsername()); // 用户名
        if (TgSystemHelper.isRememberPassword()) { // 以前用户是否选择了记住密码
            etPassword.setText(TgSystemHelper.getLoginPassword()); // 密码
        }
        cbRememberPass.setChecked(true); // 默认记住密码
        etUsername.addTextChangedListener(userNameTextWatcher); // 用户名内容监
        etPassword.addTextChangedListener(passwordTextWatcher); // 密码内容监听
        tilPassword.setPasswordVisibilityToggleEnabled(true); // 点击显示密码
        etUsername.setOnTextClearListener(new ClearEditText.OnTextClearListener() { // 点击了清空按鈕
            @Override
            public void textClear() {
                tilUsername.setErrorEnabled(false);
                isUsernameEnable = false;
                btnLogin.setEnabled(false);
            }
        });
        if (!TextUtils.isEmpty(phone)) { // 如果从找回密码或者注册跳转过来的会有值，从欢迎页面过来没有值
            etUsername.setText(phone);
        }
        if (TextUtils.isEmpty(etUsername.getText().toString().trim())) { // 用户名没有内容
            isUsernameEnable = false;
        }
        if (TextUtils.isEmpty(etPassword.getText().toString().trim())) { // 密码没有内容
            isPasswordEnable = false;
        }
        if (isUsernameEnable && isPasswordEnable) { // 用户名和密码同时正确才可以点
            btnLogin.setEnabled(true); // 启用提交按钮
        } else {
            btnLogin.setEnabled(false); // 按钮不可点击
        }
        etUsername.setSelection(etUsername.getText().toString().trim().length()); // 光标移动到文本框末尾
        etPassword.setSelection(etPassword.getText().toString().trim().length()); // 光标移动到文本框末尾
    }

    /**
     * OnClick
     */
    @OnClick({R.id.btnLogin, R.id.tvForget, R.id.loginWeChat, R.id.loginSina, R.id.loginQQ})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.btnLogin: // 登录
                login();
                break;
            case R.id.tvForget: // 忘记密码
                TgSystemHelper.handleIntent(ConstantActivityPath.FORGET);
                break;
            case R.id.loginWeChat: // 第三方微信登录
                UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.WEIXIN, weChatAuthListener);
                break;
            case R.id.loginSina: // 第三方新浪登录
                UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.SINA, sinaAuthListener);
                break;
            case R.id.loginQQ: // 第三方qq登录
                UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.QQ, qqAuthListener);
                break;
            default:
                break;
        }
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
                btnLogin.setEnabled(true); // 启用提交按钮
            } else {
                btnLogin.setEnabled(false); // 按钮不可点击
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
                btnLogin.setEnabled(true); // 启用提交按钮
            } else {
                btnLogin.setEnabled(false); // 按钮不可点击
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
            btnLogin.setEnabled(false); // 按钮不可点击
            return;
        }
        if (etPassword.getText().toString().trim().length() < MIN_PASSWORD_LEN) {
            tilPassword.setError(getString(R.string.string_password_min_hint));
            isPasswordEnable = false;
            btnLogin.setEnabled(false); // 按钮不可点击
            return;
        }
        loginRequest(); // 登录
    }

    /**
     * 登录网络请求
     */
    private void loginRequest() {
        final String username = etUsername.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_LOGIN, getHttpTaskKey());
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
                    TgSystemHelper.handleIntentAndFinish(ConstantActivityPath.MAIN, LoginActivity.this); // 跳转到主页面,关闭当前页面
                } else {
                    TgDialogUtil.showToast(result.getMsg()); // 登录失败，弹出错误信息
                }
            }
        });
    }

    // 微信登录
    private UMAuthListener weChatAuthListener = new UMAuthListener() {
        @Override
        public void onStart(final SHARE_MEDIA platform) {
        }

        @Override
        public void onComplete(final SHARE_MEDIA platform, final int action, final Map<String, String> data) {
            MobclickAgent.onProfileSignIn(platform.getName(), data.get("uid")); // 统计
            isFirstLogin(TgConstantLoginType.TYPE_WECHAT, data.get("uid"));
        }

        @Override
        public void onError(final SHARE_MEDIA platform, final int action, final Throwable t) {
            if (t != null) {
                Log.d("UMAuthListener", "onError: " + t.getMessage());
            }
        }

        @Override
        public void onCancel(final SHARE_MEDIA platform, final int action) {
        }
    };

    // 新浪登录
    private UMAuthListener sinaAuthListener = new UMAuthListener() {
        @Override
        public void onStart(final SHARE_MEDIA platform) {
        }

        @Override
        public void onComplete(final SHARE_MEDIA platform, final int action, final Map<String, String> data) {
            MobclickAgent.onProfileSignIn(platform.getName(), data.get("uid"));
            isFirstLogin(TgConstantLoginType.TYPE_SINA, data.get("uid"));
        }

        @Override
        public void onError(final SHARE_MEDIA platform, final int action, final Throwable t) {
            if (t != null) {
                Log.d("UMAuthListener", "onError: " + t.getMessage());
            }
        }

        @Override
        public void onCancel(final SHARE_MEDIA platform, final int action) {
        }
    };

    // qq登录
    private UMAuthListener qqAuthListener = new UMAuthListener() {
        @Override
        public void onStart(final SHARE_MEDIA platform) {
        }

        @Override
        public void onComplete(final SHARE_MEDIA platform, final int action, final Map<String, String> data) {
            MobclickAgent.onProfileSignIn(platform.getName(), data.get("uid"));
            isFirstLogin(TgConstantLoginType.TYPE_QQ, data.get("uid"));
        }

        @Override
        public void onError(final SHARE_MEDIA platform, final int action, final Throwable t) {
            if (t != null) {
                Log.d("UMAuthListener", "onError: " + t.getMessage());
            }
        }

        @Override
        public void onCancel(final SHARE_MEDIA platform, final int action) {
        }
    };

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 登录页面必须有这行代码，否则回调不正确
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 是不是上来就第三方登录，如果是，要到绑定账号页面，绑定账号
     */
    private void isFirstLogin(final int loginType, final String uid) {
        String path = ConstantUrls.URL_SDK_QQ;
        if (TgConstantLoginType.isWeChat(loginType)) {
            path = ConstantUrls.URL_SDK_WECHAT;
        } else if (TgConstantLoginType.isSina(loginType)) {
            path = ConstantUrls.URL_SDK_SINA;
        }
        PostRequest request = TgHttpUtil.requestPost(path, getHttpTaskKey());
        request.params("uid", uid);
        request.execute(new TgHttpCallback(null) {
            @Override
            public void successHandler(final TgResponseBean result) {
                if (result.isSuccess()) { // 成功
                    Map<String, Object> map = (Map<String, Object>) result.getData();
                    boolean isFirst = (Boolean) map.get("isFirst");
                    if (isFirst) { // 第一次直接用第三方登录，绑定账号页面
                        Bundle bundle = new Bundle();
                        bundle.putInt("loginType", loginType);
                        bundle.putString("uid", uid);
                        TgSystemHelper.handleIntentWithBundle(ConstantActivityPath.BINDING_USER, "bundle", bundle); // 跳转到绑定账号页面,关闭当前页面
                    } else {
                        loginBySDK(loginType, uid);
                    }
                } else {
                    TgDialogUtil.showToast(result.getMsg()); // 登录失败，弹出错误信息
                }
            }
        });
    }

    /**
     * 第三方登录得到信息
     */
    private void loginBySDK(final int loginType, final String uid) {
        String path = ConstantUrls.URL_LOGIN_UID_QQ;
        if (TgConstantLoginType.isWeChat(loginType)) {
            path = ConstantUrls.URL_LOGIN_UID_WECHAT;
        } else if (TgConstantLoginType.isSina(loginType)) {
            path = ConstantUrls.URL_LOGIN_UID_SINA;
        }
        PostRequest request = TgHttpUtil.requestPost(path, getHttpTaskKey());
        request.params("uid", uid);
        request.execute(new TgHttpCallback(null) {
            @Override
            public void successHandler(final TgResponseBean result) {
                if (result.isSuccess()) { // 成功
                    TgSystemHelper.setUserInfo(result);
                    TgSystemHelper.handleIntentAndFinish(ConstantActivityPath.MAIN, LoginActivity.this); // 跳转到主页面,关闭当前页面
                } else {
                    TgDialogUtil.showToast(result.getMsg()); // 登录失败，弹出错误信息
                }
            }
        });
    }
}
