package com.turingoal.mengbao.teacher.ui.activity;

import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.turingoal.common.base.TgBaseActivity;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 用户协议
 */
@Route(path = ConstantActivityPath.AGREEMENT)
public class AgreementActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle; // Title

    @Override
    protected int getLayoutID() {
        return R.layout.activity_agreement;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_agreement);
    }

    /**
     * OnClick
     */
    @OnClick(R.id.ivStart)
    public void onViewClicked() {
        defaultFinish();
    }
}
