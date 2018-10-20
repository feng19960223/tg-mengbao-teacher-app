package com.turingoal.mengbao.teacher.ui.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseViewHolder;
import com.turingoal.mengbao.common.biz.domain.Comments;
import com.turingoal.mengbao.common.constants.TgBtsConstantYesNo;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;

/**
 * 老师点评历史adapter
 */
public class CommentsRecordAdapter extends BaseQuickAdapter<Comments, TgBaseViewHolder> {
    public CommentsRecordAdapter() {
        super(R.layout.item_comments_record);
    }

    @Override
    protected void convert(final TgBaseViewHolder helper, final Comments comments) {
        helper.setText(R.id.tvContent, comments.getContent())
                .setText(R.id.tvReply, comments.getParentReply())
                .setGone(R.id.ivFlower, TgBtsConstantYesNo.YES.equals(comments.getFlower()))
                .setText(R.id.tvDate, comments.getCreateTimeStr4DateTime());
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                TgSystemHelper.handleIntentWithObj(ConstantActivityPath.COMMENTS_DETAIL, "comments", comments); // 亲子作业详情页面
            }
        });
    }
}
