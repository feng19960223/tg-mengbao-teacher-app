package com.turingoal.mengbao.teacher.ui.adapter;

import android.graphics.Color;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseViewHolder;
import com.turingoal.common.utils.TgStringUtil;
import com.turingoal.mengbao.common.biz.domain.NoticeRecord;
import com.turingoal.mengbao.common.constants.TgBtsConstantYesNo;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;

/**
 * 通知adapter
 */
public class InformAdapter extends BaseQuickAdapter<NoticeRecord, TgBaseViewHolder> {
    public InformAdapter() {
        super(R.layout.item_inform);
    }

    @Override
    protected void convert(final TgBaseViewHolder helper, final NoticeRecord noticeRecord) {
        helper.setText(R.id.tvTitle, noticeRecord.getNoticeTitle())
                .setText(R.id.tvContent, TgStringUtil.replaceBlank(noticeRecord.getContent()))
                .setText(R.id.tvDate, noticeRecord.getCreateTimeStr4DateTime())
                .setTextColor(R.id.tvTitle, TgBtsConstantYesNo.YES.equals(noticeRecord.getStatus()) ? Color.parseColor("#8A8A8A") : Color.parseColor("#212121"))
                .setGone(R.id.ivTop, TgBtsConstantYesNo.YES.equals(noticeRecord.getSticky()));
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                noticeRecord.setStatus(TgBtsConstantYesNo.YES);
                TgSystemHelper.handleIntentWithObj(ConstantActivityPath.INFORM_DETAIL, "noticeRecord", noticeRecord); // 亲子作业详情页面
            }
        });
    }
}
