package com.turingoal.mengbao.teacher.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseViewHolder;
import com.turingoal.common.utils.GlideUtil;
import com.turingoal.mengbao.common.biz.domain.Child;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;

/**
 * 考勤
 */
public class AttendancePeopleAdapter extends BaseQuickAdapter<Child, TgBaseViewHolder> {
    public AttendancePeopleAdapter() {
        super(R.layout.item_attendance_people);
    }

    @Override
    protected void convert(final TgBaseViewHolder helper, final Child child) {
        helper.setText(R.id.tvName, child.getRealname());
        GlideUtil.loadImage(mContext, child.getAvatar(), (ImageView) helper.getView(R.id.ivAvatar)); // 设置图片
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TgSystemHelper.handleIntentWithObj(ConstantActivityPath.ATTENDANCE_ADD, "child", child);
            }
        });
    }
}
