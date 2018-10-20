package com.turingoal.mengbao.teacher.ui.activity;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lzy.okgo.request.PostRequest;
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
 * 修改密码
 */
@Route(path = ConstantActivityPath.CHANGE_PASSWORD)
public class ChangePasswordActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.etPasswordOld)
    EditText etPasswordOld; // 旧密码
    @BindView(R.id.tilPasswordOld)
    TextInputLayout tilPasswordOld;
    @BindView(R.id.etPasswordNew)
    EditText etPasswordNew; // 新密码
    @BindView(R.id.tilPasswordNew)
    TextInputLayout tilPasswordNew;
    @BindView(R.id.etPasswordConfirm)
    EditText etPasswordConfirm; // 确认新密码
    @BindView(R.id.tilPasswordConfirm)
    TextInputLayout tilPasswordConfirm;
    @BindView(R.id.btnChange)
    Button btnChange; // 修改按钮
    private boolean isPasswordOldEnable = false; // 用户名是否正确
    private boolean isPasswordNewEnable = false; // 新密码是否正确
    private boolean isPasswordConfirmEnable = false; // 确认密码是否正确
    private static final int MAX_PASSWORD_LEN = 24; // 密码最大长度
    private static final int MIN_PASSWORD_LEN = 6; // 密码最小长度

    @Override
    protected int getLayoutID() {
        return R.layout.activity_change_password;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_change_password);
        tilPasswordOld.setPasswordVisibilityToggleEnabled(true); // 点击显示密码
        tilPasswordNew.setPasswordVisibilityToggleEnabled(true); // 点击显示密码
        tilPasswordConfirm.setPasswordVisibilityToggleEnabled(true); // 点击显示密码
        etPasswordOld.addTextChangedListener(passwordOldTextWatcher); // 旧密码内容监听
        etPasswordNew.addTextChangedListener(passwordNewTextWatcher); // 新密码内容监听
        etPasswordConfirm.addTextChangedListener(passwordConfirmTextWatcher); // 确认密码内容监听
    }

    /**
     * 更改密码
     */
    private void updataUserPass() {
        String oldPassword = etPasswordOld.getText().toString().trim(); // 原密码
        String newPassword = etPasswordNew.getText().toString().trim(); // 新密码
        String confirmPassword = etPasswordConfirm.getText().toString().trim(); // 确认密码
        if (oldPassword.length() < MIN_PASSWORD_LEN) {
            tilPasswordOld.setError(getText(R.string.string_password_min_hint));
            isPasswordOldEnable = false;
            btnChange.setEnabled(false); // 按钮不可点击
            return;
        }
        if (newPassword.length() < MIN_PASSWORD_LEN) {
            tilPasswordNew.setError(getText(R.string.string_password_min_hint));
            isPasswordNewEnable = false;
            btnChange.setEnabled(false); // 按钮不可点击
            return;
        }
        if (confirmPassword.length() < MIN_PASSWORD_LEN) {
            tilPasswordConfirm.setError(getText(R.string.string_password_min_hint));
            isPasswordConfirmEnable = false;
            btnChange.setEnabled(false); // 按钮不可点击
            return;
        }
        if (!newPassword.equals(confirmPassword)) { // 两次密码不同
            etPasswordConfirm.setText("");
            tilPasswordConfirm.setError(getString(R.string.string_password_equals_hint));
            isPasswordConfirmEnable = false;
            btnChange.setEnabled(false);
            return;
        }
        submitRequest();
    }

    /**
     * 更改密码请求
     */
    private void submitRequest() {
        String oldPassword = etPasswordOld.getText().toString().trim();
        String newPassword = etPasswordNew.getText().toString().trim();
        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_CHANGE_PASS, getHttpTaskKey());
        request.params("userId", TgSystemHelper.getUserId());
        request.params("oldUserPass", oldPassword);
        request.params("userPass", newPassword);
        request.execute(new TgHttpCallback(null) {
            @Override
            public void successHandler(final TgResponseBean result) {
                if (result.isSuccess()) {
                    TgDialogUtil.showToast(getString(R.string.change_password_hint)); // 修改成功，请重新登录
                    TgSystemHelper.logout(ChangePasswordActivity.this); // 注销并跳转到登录页面
                } else {
                    TgDialogUtil.showToast(result.getMsg()); // 登录失败，弹出错误信息
                }
            }
        });
    }

    /**
     * 密码EditText监听
     */
    private TextWatcher passwordOldTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }

        @Override
        public void onTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }

        @Override
        public void afterTextChanged(final Editable editable) {
            if (editable.toString().trim().length() != 0 && editable.toString().trim().length() < MAX_PASSWORD_LEN) { // 3-24个字
                isPasswordOldEnable = true;
            } else {
                isPasswordOldEnable = false;
            }
            if (editable.toString().trim().length() > MAX_PASSWORD_LEN) { // 字数大于最大限制，提示错误
                tilPasswordOld.setError(getString(R.string.string_password_max_hint));
            } else {
                tilPasswordOld.setErrorEnabled(false);
            }
            if (isPasswordOldEnable && isPasswordNewEnable && isPasswordConfirmEnable) { // 用户名和密码同时正确才可以点
                btnChange.setEnabled(true); // 启用提交按钮
            } else {
                btnChange.setEnabled(false); // 按钮不可点击
            }
        }
    };

    /**
     * 密码EditText监听
     */
    private TextWatcher passwordNewTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }

        @Override
        public void onTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }

        @Override
        public void afterTextChanged(final Editable editable) {
            if (editable.toString().trim().length() != 0 && editable.toString().trim().length() < MAX_PASSWORD_LEN) { // 3-24个字
                isPasswordNewEnable = true;
            } else {
                isPasswordNewEnable = false;
            }
            if (editable.toString().trim().length() > MAX_PASSWORD_LEN) { // 字数大于最大限制，提示错误
                tilPasswordNew.setError(getString(R.string.string_password_max_hint));
            } else {
                tilPasswordNew.setErrorEnabled(false);
            }
            if (isPasswordOldEnable && isPasswordNewEnable && isPasswordConfirmEnable) { // 用户名和密码同时正确才可以点
                btnChange.setEnabled(true); // 启用提交按钮
            } else {
                btnChange.setEnabled(false); // 按钮不可点击
            }
        }
    };

    /**
     * 密码EditText监听
     */
    private TextWatcher passwordConfirmTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }

        @Override
        public void onTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }

        @Override
        public void afterTextChanged(final Editable editable) {
            if (editable.toString().trim().length() != 0 && editable.toString().trim().length() < MAX_PASSWORD_LEN) { // 3-24个字
                isPasswordConfirmEnable = true;
            } else {
                isPasswordConfirmEnable = false;
            }
            if (editable.toString().trim().length() > MAX_PASSWORD_LEN) { // 字数大于最大限制，提示错误
                tilPasswordConfirm.setError(getString(R.string.string_password_max_hint));
            } else {
                tilPasswordConfirm.setErrorEnabled(false);
            }
            if (isPasswordOldEnable && isPasswordNewEnable && isPasswordConfirmEnable) { // 用户名和密码同时正确才可以点
                btnChange.setEnabled(true); // 启用提交按钮
            } else {
                btnChange.setEnabled(false); // 按钮不可点击
            }
        }
    };

    /**
     * OnClick
     */
    @OnClick({R.id.ivStart, R.id.btnChange})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.ivStart:
                defaultFinish(); // 返回
                break;
            case R.id.btnChange:
                updataUserPass();
                break;
            default:
                break;
        }
    }
}
