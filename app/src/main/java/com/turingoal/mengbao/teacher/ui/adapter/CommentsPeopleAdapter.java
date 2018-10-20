package com.turingoal.mengbao.teacher.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseViewHolder;
import com.turingoal.common.utils.GlideUtil;
import com.turingoal.mengbao.common.biz.domain.Child;
import com.turingoal.mengbao.common.constants.TgBtsConstantYesNo;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;

/**
 * 考勤
 */
public class CommentsPeopleAdapter extends BaseQuickAdapter<Child, TgBaseViewHolder> {
    public CommentsPeopleAdapter() {
        super(R.layout.item_comments_people);
    }

    @Override
    protected void convert(final TgBaseViewHolder helper, final Child child) {
        helper.setText(R.id.tvName, child.getRealname())
                .setGone(R.id.tvNow, TgBtsConstantYesNo.NO.equals(child.getCommentStatus())); // 如果今天已经点评，这隐藏，显示下面的“今日已评”
        GlideUtil.loadImage(mContext, child.getAvatar(), (ImageView) helper.getView(R.id.ivAvatar)); // 设置图片
        helper.getView(R.id.tvNow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                TgSystemHelper.handleIntentWithObj(ConstantActivityPath.COMMENTS_ADD, "child", child);
            }
        });
        helper.getView(R.id.tvRecord).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                TgSystemHelper.handleIntentWithObj(ConstantActivityPath.COMMENTS_RECORD, "child", child);
            }
        });
    }
}
