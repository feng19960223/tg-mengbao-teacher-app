package com.turingoal.mengbao.teacher.ui.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.turingoal.common.base.TgBaseViewHolder;
import com.turingoal.common.utils.GlideUtil;
import com.turingoal.mengbao.common.biz.domain.HomeworkRecord;
import com.turingoal.mengbao.teacher.R;

/**
 * 考勤
 */
public class HomeworkNoAdapter extends BaseQuickAdapter<HomeworkRecord, TgBaseViewHolder> {
    public HomeworkNoAdapter() {
        super(R.layout.item_homework_no);
    }

    @Override
    protected void convert(final TgBaseViewHolder helper, final HomeworkRecord homeworkRecord) {
        helper.setText(R.id.tvName, homeworkRecord.getChildRealname());
        GlideUtil.loadImage(mContext, homeworkRecord.getChildAvatar(), (ImageView) helper.getView(R.id.ivAvatar)); // 设置图片
    }
}
