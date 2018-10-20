package com.turingoal.mengbao.teacher.ui.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseViewHolder;
import com.turingoal.mengbao.common.biz.domain.Homework;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;

/**
 * 亲子作业adapter
 */
public class HomeworkAdapter extends BaseQuickAdapter<Homework, TgBaseViewHolder> {
    public HomeworkAdapter() {
        super(R.layout.item_homework);
    }

    @Override
    protected void convert(final TgBaseViewHolder helper, final Homework homework) {
        helper.setText(R.id.tvTitle, homework.getTitle())
                .setText(R.id.tvContent, homework.getContent())
                .setText(R.id.tvDate, homework.getCreateTimeStr4DateTime())
                .setText(R.id.tvSource, homework.getUserRealname());
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                TgSystemHelper.handleIntentWithObj(ConstantActivityPath.HOMEWORK_DETAIL, "homework", homework); // 亲子作业详情页面
            }
        });
    }
}
