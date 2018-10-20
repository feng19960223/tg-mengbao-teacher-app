package com.turingoal.mengbao.teacher.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.turingoal.common.base.TgBaseViewHolder;
import com.turingoal.common.utils.TgStringUtil;
import com.turingoal.mengbao.common.biz.domain.Attendance;
import com.turingoal.mengbao.common.constants.TgConstantAttendanceType;
import com.turingoal.mengbao.teacher.R;

/**
 * 考勤
 */
public class AttendanceAdapter extends BaseQuickAdapter<Attendance, TgBaseViewHolder> {
    public AttendanceAdapter() {
        super(R.layout.item_attendance);
    }

    @Override
    protected void convert(final TgBaseViewHolder helper, final Attendance attendance) {
        helper.setText(R.id.tvName, attendance.getChildRealname())
                .setText(R.id.tvRemakes, attendance.getRemakes())
                .setText(R.id.tvStatus,  TgConstantAttendanceType.getUserTypeStr(attendance.getType()))
                .setGone(R.id.tvRemakes, !TgStringUtil.isEmpty(attendance.getRemakes()))
                .setBackgroundRes(R.id.llAttendance, getAttendanceColor(attendance.getType()));
    }

    /**
     * 不同考勤类型，不同背景色
     */
    private int getAttendanceColor(final int type) {
        if (TgConstantAttendanceType.LATE == type) {
            return R.color.attendance1;
        } else if (TgConstantAttendanceType.LEAVE_EARLY == type) {
            return R.color.attendance2;
        } else if (TgConstantAttendanceType.LEAVE == type) {
            return R.color.attendance3;
        } else if (TgConstantAttendanceType.ABSENTEEISM == type) {
            return R.color.attendance4;
        } else {
            return R.color.colorPrimary;
        }
    }
}
