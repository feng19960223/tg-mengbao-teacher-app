package com.turingoal.mengbao.teacher.ui.activity;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
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
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;
import com.turingoal.mengbao.teacher.constants.ConstantUrls;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置新密码
 */
@Route(path = ConstantActivityPath.NEW_PASSWORD)
public class NewPasswordActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.etPassword)
    EditText etPassword; // 密码
    @BindView(R.id.tilPassword)
    TextInputLayout tilPassword; // 密码控制
    @BindView(R.id.btnSave)
    Button btnSave; // 保存按钮
    private static final int MAX_PASSWORD_LEN = 24; // 密码最大长度
    private static final int MIN_PASSWORD_LEN = 6; // 密码最小长度
    @Autowired
    String phone; // 手机号码

    @Override
    protected int getLayoutID() {
        return R.layout.activity_newpassword;
    }

    @Override
    protected void initialized() {
        TgApplication.addForgetActivity(this);
        tvTitle.setText(R.string.activity_newPassword); // title
        etPassword.addTextChangedListener(passwordTextWatcher); // 密码内容监听
        tilPassword.setPasswordVisibilityToggleEnabled(true); // 点击显示密码
    }

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
            if (editable.toString().trim().length() > MIN_PASSWORD_LEN && editable.toString().trim().length() < MAX_PASSWORD_LEN) { // 3-24个字
                btnSave.setEnabled(true); // 启用提交按钮
            } else {
                btnSave.setEnabled(false); // 按钮不可点击
            }
            if (editable.toString().trim().length() > MAX_PASSWORD_LEN) { // 字数大于最大限制，提示错误
                tilPassword.setError(getString(R.string.string_password_max_hint));
            } else {
                tilPassword.setErrorEnabled(false);
            }
        }
    };

    /**
     * 保存
     */
    private void save() {
        if (etPassword.getText().toString().trim().length() < MIN_PASSWORD_LEN) {
            tilPassword.setError(getString(R.string.string_password_min_hint));
            btnSave.setEnabled(false); // 按钮不可点击
            return;
        }
        saveRequest();
    }

    /**
     * 设置密码
     */
    private void saveRequest() {
        final String newPassword = etPassword.getText().toString().trim();
        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_PASSWORD_PHONE, getHttpTaskKey());
        request.params("phone", phone);
        request.params("password", newPassword);
        request.execute(new TgHttpCallback(null) {
            @Override
            public void successHandler(final TgResponseBean result) {
                if (result.isSuccess()) { // 成功
                    TgApplication.clearForgetActivitys(); // 清空找回密码路线的activity
                    TgSystemHelper.handleIntentWithObj(ConstantActivityPath.LOGIN, "phone", phone);
                } else {
                    TgDialogUtil.showToast(result.getMsg());
                }
            }
        });
    }

    /**
     * OnClick
     */
    @OnClick({R.id.ivStart, R.id.btnSave})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.ivStart:
                defaultFinish();
                break;
            case R.id.btnSave:
                save();
                break;
            default:
                break;
        }
    }
}
