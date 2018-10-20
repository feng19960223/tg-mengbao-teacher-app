package com.turingoal.mengbao.teacher.ui.activity;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.jkb.vcedittext.VerificationCodeEditText;
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

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 更换手机号码的验证码
 */
@Route(path = ConstantActivityPath.CHANGE_VERIFICATION)
public class ChangeVerificationActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvPhone)
    TextView tvPhone; // 手机号
    @BindView(R.id.etVerification)
    VerificationCodeEditText etVerification; // 验证码
    @BindView(R.id.btnNext)
    Button btnNext; // 下一步
    @BindView(R.id.tvAgain)
    TextView tvAgain; // 重新发送验证码
    @Autowired
    String phone; // 手机号码
    private static final int TIME_LENG = 60 * 1000; // 60秒后可以重新发送验证码
    private static final int TIME_INTERVAL = 1 * 1000; // 倒计时时间间隔
    private static final int VERIFICATION_LEN = 6; // 验证码长度
    private boolean isCountDownFinish = false; // 倒计时是否完成，默认没有完成

    @Override
    protected int getLayoutID() {
        return R.layout.activity_change_verification;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TgApplication.deleteChangeActivity(this);
    }

    @Override
    protected void initialized() {
        TgApplication.addChangeActivity(this);
        new VerificationCountDownTimer(TIME_LENG, TIME_INTERVAL).start(); // 开始倒计时
        tvTitle.setText(R.string.activity_change_verification);
        tvPhone.setText("+" + phone); // 手机号
        etVerification.addTextChangedListener(verificationTextWatcher);
    }

    /**
     * 验证码EditText监听
     */
    private TextWatcher verificationTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }

        @Override
        public void onTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }

        @Override
        public void afterTextChanged(final Editable editable) {
            if (editable.toString().trim().length() == VERIFICATION_LEN) { // 6个数字
                btnNext.setEnabled(true); // 启用下一步按钮
            } else {
                btnNext.setEnabled(false); // 按钮不可点击
            }
        }
    };

    /**
     * 重新请求验证码
     */
    private void verificationRequest() {
        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_VERIFICATION_PHONE, getHttpTaskKey());
        request.params("userId",TgSystemHelper.getUserId());
        request.params("phone", phone);
        request.execute(new TgHttpCallback(null) {
            @Override
            public void successHandler(final TgResponseBean result) {
                new VerificationCountDownTimer(TIME_LENG, TIME_INTERVAL).start(); // 开始倒计时
                if (result.isSuccess()) { // 成功
                    TgDialogUtil.showToast(getString(R.string.verification_again_success));
                } else {
                    TgDialogUtil.showToast(result.getMsg());
                }
            }
        });
    }

    /**
     * 倒计时内部类
     */
    class VerificationCountDownTimer extends CountDownTimer {

        public VerificationCountDownTimer(final long millisInFuture, final long countDownInterval) { // 总毫秒数，时间间隔
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            isCountDownFinish = true; // 倒计时完成
            tvAgain.setText(getString(R.string.verification_again_finish)); // 点击重新获取验证码
        }

        @Override
        public void onTick(final long millisUntilFinished) { // 剩余毫秒数
            tvAgain.setText(String.format(getString(R.string.verification_again), millisUntilFinished / TIME_INTERVAL)); // 每秒-1
        }
    }

    /**
     * 验证码是否正确
     */
    private void verification() {
        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_USERID_PHONE_VERIFICATION, getHttpTaskKey());
        request.params("userId", TgSystemHelper.getUserId());
        request.params("phone", phone);
        request.params("verification", etVerification.getText().toString().trim());
        request.execute(new TgHttpCallback(null) {
            @Override
            public void successHandler(final TgResponseBean result) {
                Map<String, Object> map = (Map<String, Object>) result.getData();
                boolean results = (Boolean) map.get("results");
                if (results) {
                    Toast.makeText(ChangeVerificationActivity.this, getString(R.string.verification_success), Toast.LENGTH_SHORT).show();
                    TgApplication.getTgUserPreferences().setPhone(phone);
                    TgApplication.clearChangeActivitys();
                } else {
                    Toast.makeText(ChangeVerificationActivity.this, getString(R.string.verification_error), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /**
     * OnClick
     */
    @OnClick({R.id.ivStart, R.id.btnNext, R.id.tvAgain})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.ivStart: // 返回
                defaultFinish();
                break;
            case R.id.btnNext: // 下一步
                verification();
                break;
            case R.id.tvAgain: // 再次获取验证码
                if (isCountDownFinish) {
                    verificationRequest(); // 再次请求验证码，没有收到验证码
                }
                break;
            default:
                break;
        }
    }
}
