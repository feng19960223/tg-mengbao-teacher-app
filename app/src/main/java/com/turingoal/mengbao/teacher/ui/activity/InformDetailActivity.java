package com.turingoal.mengbao.teacher.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.lzy.okgo.request.PostRequest;
import com.turingoal.common.base.TgBaseActivity;
import com.turingoal.common.bean.TgResponseBean;
import com.turingoal.common.utils.TgHttpCallback;
import com.turingoal.common.utils.TgHttpUtil;
import com.turingoal.mengbao.common.biz.domain.NoticeRecord;
import com.turingoal.mengbao.common.constants.TgBtsConstantYesNo;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;
import com.turingoal.mengbao.teacher.constants.ConstantUrls;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 通知详情
 */
@Route(path = ConstantActivityPath.INFORM_DETAIL)
public class InformDetailActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvInformTitle)
    TextView tvInformTitle; // 通知详情title
    @BindView(R.id.tvDate)
    TextView tvDate; //  通知详情时间
    @BindView(R.id.ivTop)
    ImageView ivTop; // 置顶
    @BindView(R.id.tvContent)
    TextView tvContent; //  通知详情内容
    @Autowired
    NoticeRecord noticeRecord; // 从list传递过来的item对象

    @Override
    protected int getLayoutID() {
        return R.layout.activity_inform_detail;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_inform_detail);
        ivTop.setVisibility(TgBtsConstantYesNo.YES.equals(noticeRecord.getSticky()) ? View.VISIBLE : View.GONE);
        tvInformTitle.setText(noticeRecord.getNoticeTitle());
        tvDate.setText(noticeRecord.getCreateTimeStr4DateTime());
        tvContent.setText(noticeRecord.getContent());
        readRequest();
    }

    /**
     * 读一条消息
     */
    private void readRequest() {
        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_INFORM_READ, getHttpTaskKey());
        request.params("id", noticeRecord.getId());
        request.execute(new TgHttpCallback(this) {
            @Override
            public void successHandler(final TgResponseBean result) {
                // 成功失败不影响
            }
        });
    }

    /**
     * OnClick
     */
    @OnClick(R.id.ivStart)
    public void onViewClicked() {
        defaultFinish();
    }
}
