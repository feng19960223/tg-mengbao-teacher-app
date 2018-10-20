package com.turingoal.mengbao.teacher.ui.activity;

import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.lzy.okgo.request.PostRequest;
import com.turingoal.common.base.TgBaseActivity;
import com.turingoal.common.bean.TgResponseBean;
import com.turingoal.common.utils.TgDialogUtil;
import com.turingoal.common.utils.TgHttpCallback;
import com.turingoal.common.utils.TgHttpUtil;
import com.turingoal.mengbao.common.biz.domain.LoveRemarks;
import com.turingoal.mengbao.common.constants.TgBtsConstantYesNo;
import com.turingoal.mengbao.common.constants.TgConstantLoveRemarksType;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;
import com.turingoal.mengbao.teacher.constants.ConstantUrls;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 收到爱心备注
 */
@Route(path = ConstantActivityPath.LOVE_RECEIVED)
public class LoveReceivedActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvEnd)
    TextView tvEnd;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvStatus)
    TextView tvStatus;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvDescription)
    TextView tvDescription;
    @Autowired
    LoveRemarks loveRemarks;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_love_received;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_love_received);
        tvEnd.setText(R.string.received);
        tvName.setText(loveRemarks.getChildRealname());
        tvStatus.setText(TgConstantLoveRemarksType.getUserTypeStr(loveRemarks.getType()));
        tvDate.setText(loveRemarks.getCreateTimeStr4DateTime());
        tvDescription.setText(loveRemarks.getContent());
        if (TgBtsConstantYesNo.NO.equals(loveRemarks.getStatus())) {
            tvEnd.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 收到爱心备注
     */
    private void request() {
        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_LOVE_RECEIVED, getHttpTaskKey());
        request.params("id", loveRemarks.getId());
        request.execute(new TgHttpCallback(this) {
            @Override
            public void successHandler(final TgResponseBean result) {
                if (result.isSuccess()) {
                    defaultFinish();
                } else {
                    TgDialogUtil.showToast(result.getMsg()); // 登录失败，弹出错误信息
                }
            }
        });
    }

    /**
     * OnClick
     */
    @OnClick({R.id.ivStart, R.id.tvEnd})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.ivStart:
                defaultFinish();
                break;
            case R.id.tvEnd:
                request();
                break;
            default:
                break;
        }
    }
}
