package com.turingoal.mengbao.teacher.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.request.PostRequest;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseActivity;
import com.turingoal.common.bean.TgResponseBean;
import com.turingoal.common.utils.TgDateTimePickUtil;
import com.turingoal.common.utils.TgDateUtil;
import com.turingoal.common.utils.TgDialogUtil;
import com.turingoal.common.utils.TgHttpCallback;
import com.turingoal.common.utils.TgHttpUtil;
import com.turingoal.common.utils.TgJsonUtil;
import com.turingoal.mengbao.common.biz.domain.Attendance;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;
import com.turingoal.mengbao.teacher.constants.ConstantUrls;
import com.turingoal.mengbao.teacher.ui.adapter.AttendanceAdapter;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 考勤
 */
@Route(path = ConstantActivityPath.ATTENDANCE)
public class AttendanceActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ivEnd)
    ImageView ivEnd; // 增加一条考勤
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.rvAttendance)
    RecyclerView rvAttendance;
    private Date currentDate = new Date(); // 当前选中时间
    private AttendanceAdapter mAdapter = new AttendanceAdapter(); // adapter
    private static final int DAY_MILLIS = 1000 * 60 * 60 * 24; //一天的毫秒值

    @Override
    protected int getLayoutID() {
        return R.layout.activity_attendance;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_attendance);
        ivEnd.setVisibility(View.VISIBLE); // 直接考勤记录按钮可见
        ivEnd.setImageResource(R.drawable.ic_add_black_24dp);
        tvTime.setText(TgDateUtil.date2String(currentDate, TgDateUtil.FORMAT_YYYY_MM_DD_WEEK_ZH));
        initRecyclerAndAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    /**
     * 得到考勤数据
     */
    private void getData() {
        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_ATTENDANCE, getHttpTaskKey());
        request.params("attendanceDate", TgDateUtil.date2String(currentDate, TgDateUtil.FORMAT_YYYY_MM_DD));
        request.params("classId", TgSystemHelper.getClassId());
        request.execute(new TgHttpCallback(this) {
            @Override
            public void successHandler(final TgResponseBean result) {
                if (result.isSuccess()) {
                    if (result.getData() == null) {
                        return;
                    }
                    List<Attendance> attendances = TgJsonUtil.jsonResultBean2List(result, Attendance.class);
                    if (attendances != null) {
                        mAdapter.setNewData(attendances);
                    }
                } else {
                    TgDialogUtil.showToast(result.getMsg()); // 登录失败，弹出错误信息
                }
            }
        });
    }

    /**
     * 初始化recyclerView和adapter
     */
    private void initRecyclerAndAdapter() {
        rvAttendance.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.isFirstOnly(false); // 来回都要动画
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM); // 动画
        rvAttendance.setAdapter(mAdapter);
        mAdapter.setEmptyView(getEmptyView((ViewGroup) rvAttendance.getParent()));
    }

    /**
     * OnClick
     */
    @OnClick({R.id.ivStart, R.id.ivEnd, R.id.flTime})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.ivStart:
                defaultFinish();
                break;
            case R.id.ivEnd: // 增加考勤记录
                TgSystemHelper.handleIntent(ConstantActivityPath.ATTENDANCE_PEOPLE);
                break;
            case R.id.flTime:
                TgDateTimePickUtil timeSelector = new TgDateTimePickUtil(this, currentDate, TgDateUtil.DEFAULT_START_DATE, new Date(), null);
                timeSelector.dateTimePickDialog(new TgDateTimePickUtil.OnSubmitSelectDateListener() {
                    public void onSumbitSelect(final Date date) {
                        if (currentDate.getTime() / DAY_MILLIS == date.getTime() / DAY_MILLIS) { //用户选择了当天
                            return;
                        }
                        currentDate.setTime(date.getTime());
                        tvTime.setText(TgDateUtil.date2String(currentDate, TgDateUtil.FORMAT_YYYY_MM_DD_WEEK_ZH));
                        getData(); // 选择日期
                    }
                });
                break;
            default:
                break;
        }
    }
}
