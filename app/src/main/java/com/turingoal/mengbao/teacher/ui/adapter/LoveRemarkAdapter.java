package com.turingoal.mengbao.teacher.ui.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseViewHolder;
import com.turingoal.mengbao.common.biz.domain.LoveRemarks;
import com.turingoal.mengbao.common.constants.TgBtsConstantYesNo;
import com.turingoal.mengbao.common.constants.TgConstantLoveRemarksType;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;

/**
 * 爱心备注Adapter
 */

public class LoveRemarkAdapter extends BaseQuickAdapter<LoveRemarks, TgBaseViewHolder> {
    public LoveRemarkAdapter() {
        super(R.layout.item_love_remark);
    }

    @Override
    protected void convert(final TgBaseViewHolder helper, final LoveRemarks loveRemarks) {
        helper.setText(R.id.tvName, loveRemarks.getChildRealname())
                .setText(R.id.tvTitle, TgConstantLoveRemarksType.getUserTypeStr(loveRemarks.getType()))
                .setText(R.id.tvDate, loveRemarks.getCreateTimeStr4DateTime())
                .setText(R.id.tvDescription, loveRemarks.getContent())
                .setGone(R.id.cbReceived, TgBtsConstantYesNo.YES.equals(loveRemarks.getStatus()))
                .setBackgroundRes(R.id.llLoveRemark, getAttendanceColor(loveRemarks.getType()));
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                TgSystemHelper.handleIntentWithObj(ConstantActivityPath.LOVE_RECEIVED, "loveRemarks", loveRemarks);
            }
        });
    }

    /**
     * 不同Title，不同背景色
     */
    private int getAttendanceColor(final int type) {
        if (TgConstantLoveRemarksType.LEAVE == type) {
            return R.color.attendance1;
        } else if (TgConstantLoveRemarksType.MEDICINE == type) {
            return R.color.attendance2;
        } else if (TgConstantLoveRemarksType.DIET == type) {
            return R.color.attendance3;
        } else if (TgConstantLoveRemarksType.OTHERS == type) {
            return R.color.attendance4;
        } else {
            return R.color.colorPrimary;
        }
    }
}
