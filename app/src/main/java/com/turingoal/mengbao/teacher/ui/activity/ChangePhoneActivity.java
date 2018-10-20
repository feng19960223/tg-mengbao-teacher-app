package com.turingoal.mengbao.teacher.ui.activity;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lzy.okgo.request.PostRequest;
import com.turingoal.common.app.TgApplication;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseActivity;
import com.turingoal.common.bean.TgResponseBean;
import com.turingoal.common.utils.TgHttpCallback;
import com.turingoal.common.utils.TgHttpUtil;
import com.turingoal.common.widget.ClearEditText;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;
import com.turingoal.mengbao.teacher.constants.ConstantUrls;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 忘记密码
 */
@Route(path = ConstantActivityPath.CHANGE_PHONE)
public class ChangePhoneActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.etPhone)
    ClearEditText etPhone; // 电话
    @BindView(R.id.tilPhone)
    TextInputLayout tilPhone; // 电话控制
    @BindView(R.id.btnNext)
    Button btnNext; // 下一步
    private static final int PHONE_LEN = 11; // 手机号码长度

    @Override
    protected int getLayoutID() {
        return R.layout.activity_change_phone;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TgApplication.deleteChangeActivity(this);
    }

    @Override
    protected void initialized() {
        TgApplication.addChangeActivity(this);
        tvTitle.setText(R.string.activity_change_phone);
        etPhone.addTextChangedListener(phoneTextWatcher); // 用户名内容监听
        etPhone.setOnTextClearListener(new ClearEditText.OnTextClearListener() {
            @Override
            public void textClear() {
                btnNext.setEnabled(false); // 按钮不可点击
            }
        });
    }

    /**
     * 密码EditText监听
     */
    private TextWatcher phoneTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }

        @Override
        public void onTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }

        @Override
        public void afterTextChanged(final Editable editable) {
            if (editable.toString().trim().length() == PHONE_LEN) { // 11个数字
                btnNext.setEnabled(true); // 启用下一步按钮
            } else {
                btnNext.setEnabled(false); // 按钮不可点击
            }
            if (editable.toString().trim().length() > PHONE_LEN) { // 字数大于最大限制，提示错误
                tilPhone.setError(getString(R.string.string_phone_max_hint));
            } else {
                tilPhone.setErrorEnabled(false);
            }
        }
    };

    /**
     * 发送验证码，下一步验证验证码
     */
    private void nextRequest() {
        final String phone = etPhone.getText().toString().trim();
        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_VERIFICATION_PHONE, getHttpTaskKey());
        request.params("userId",TgSystemHelper.getUserId());
        request.params("phone", phone);
        request.execute(new TgHttpCallback(null) {
            @Override
            public void successHandler(final TgResponseBean result) {
                if (result.isSuccess()) { // 成功
                    TgSystemHelper.handleIntentWithObj(ConstantActivityPath.CHANGE_VERIFICATION, "phone", phone);
                } else { // 失败，将错误信息显示到tilPhone上
                    tilPhone.setError(result.getMsg());
                }
            }
        });
    }

    /**
     * OnClick
     */
    @OnClick({R.id.ivStart, R.id.btnNext})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.ivStart: // 返回
                defaultFinish();
                break;
            case R.id.btnNext:
                nextRequest();
                break;
            default:
                break;
        }
    }
}
