package com.turingoal.mengbao.teacher.ui.activity;

import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseActivity;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 基础设置
 */
@Route(path = ConstantActivityPath.SETTING)
public class SettingActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_setting);
    }

    /**
     * OnClick
     */
    @OnClick({R.id.ivStart, R.id.tvAbout, R.id.tvAgreement, R.id.tvOut})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.ivStart: // 返回
                finish();
                break;
            case R.id.tvAbout: // 关于
                TgSystemHelper.handleIntent(ConstantActivityPath.ABOUT);
                break;
            case R.id.tvAgreement: // 协议
                TgSystemHelper.handleIntent(ConstantActivityPath.AGREEMENT);
                break;
            case R.id.tvOut: // 退出
                TgSystemHelper.logout(this); // 注销退出系统
                break;
            default:
                break;
        }
    }
}
