package com.turingoal.mengbao.teacher.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.request.PostRequest;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseActivity;
import com.turingoal.common.bean.TgResponseBean;
import com.turingoal.common.utils.TgDateUtil;
import com.turingoal.common.utils.TgDialogUtil;
import com.turingoal.common.utils.TgHttpCallback;
import com.turingoal.common.utils.TgHttpUtil;
import com.turingoal.common.utils.TgJsonUtil;
import com.turingoal.mengbao.common.biz.domain.Curriculum;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.bean.CurriculumBean;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;
import com.turingoal.mengbao.teacher.constants.ConstantUrls;
import com.turingoal.mengbao.teacher.ui.adapter.CoursesAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 课表
 */
@Route(path = ConstantActivityPath.COURSES)
public class CoursesActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvDay)
    TextView tvDay; // 课程表作用时间
    @BindView(R.id.rvCourses)
    RecyclerView rvCourses;
    private CoursesAdapter mAdapter = new CoursesAdapter();

    @Override
    protected int getLayoutID() {
        return R.layout.activity_courses;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_courses);
        tvDay.setText("" + TgDateUtil.date2String(TgDateUtil.getWeekFirst().getTime(), TgDateUtil.FORMAT_YYYY_MM_DD_ZH)
                + "-" + TgDateUtil.date2String(TgDateUtil.getWeekLast().getTime(), TgDateUtil.FORMAT_YYYY_MM_DD_ZH));
        initRecyclerAndAdapter();
        getData();
    }

    /**
     * 初始化recyclerView和adapter
     */
    private void initRecyclerAndAdapter() {
        rvCourses.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.openLoadAnimation(); // 动画
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM); // 动画
        rvCourses.setAdapter(mAdapter);
        mAdapter.setEmptyView(getEmptyView((ViewGroup) rvCourses.getParent()));
    }

    /**
     * 得到课表数据
     */
    private void getData() {
        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_COURSES, getHttpTaskKey());
        request.params("classId", TgSystemHelper.getClassId());
        request.execute(new TgHttpCallback(this) {
            @Override
            public void successHandler(final TgResponseBean result) {
                if (result.isSuccess()) {
                    if (result.getData() == null) {
                        return;
                    }
                    Curriculum curriculum = TgJsonUtil.jsonResultBean2Object(result, Curriculum.class);
                    if (curriculum != null) {
                        List<CurriculumBean> coursesBeanList = new ArrayList<>();
                        coursesBeanList.add(new CurriculumBean(1, curriculum.getAmContent1(), curriculum.getPmContent1(), curriculum.getUserIdAm1(), curriculum.getUserIdPm1(), curriculum.getAmUserRealname1(), curriculum.getPmUserRealname1()));
                        coursesBeanList.add(new CurriculumBean(2, curriculum.getAmContent2(), curriculum.getPmContent2(), curriculum.getUserIdAm2(), curriculum.getUserIdPm2(), curriculum.getAmUserRealname2(), curriculum.getPmUserRealname2()));
                        coursesBeanList.add(new CurriculumBean(3, curriculum.getAmContent3(), curriculum.getPmContent3(), curriculum.getUserIdAm3(), curriculum.getUserIdPm3(), curriculum.getAmUserRealname3(), curriculum.getPmUserRealname3()));
                        coursesBeanList.add(new CurriculumBean(4, curriculum.getAmContent4(), curriculum.getPmContent4(), curriculum.getUserIdAm4(), curriculum.getUserIdPm4(), curriculum.getAmUserRealname4(), curriculum.getPmUserRealname4()));
                        coursesBeanList.add(new CurriculumBean(5, curriculum.getAmContent5(), curriculum.getPmContent5(), curriculum.getUserIdAm5(), curriculum.getUserIdPm5(), curriculum.getAmUserRealname5(), curriculum.getPmUserRealname5()));
                        coursesBeanList.add(new CurriculumBean(6, curriculum.getAmContent6(), curriculum.getPmContent6(), curriculum.getUserIdAm6(), curriculum.getUserIdPm6(), curriculum.getAmUserRealname6(), curriculum.getPmUserRealname6()));
                        coursesBeanList.add(new CurriculumBean(7, curriculum.getAmContent7(), curriculum.getPmContent7(), curriculum.getUserIdAm7(), curriculum.getUserIdPm7(), curriculum.getAmUserRealname7(), curriculum.getPmUserRealname7()));
                        mAdapter.setNewData(coursesBeanList);
                        scrollToPosition();
                    }
                } else {
                    TgDialogUtil.showToast(result.getMsg()); // 登录失败，弹出错误信息
                }
            }
        });
    }

    /**
     * 将RecyclerView移动到指定位置
     */
    private void scrollToPosition() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); // 今天星期几
        if (dayOfWeek == 1) { // 1是星期日
            rvCourses.scrollToPosition(6); // 7(一周七天)-1
        } else {
            rvCourses.scrollToPosition(dayOfWeek - 2);
        }
    }

    /**
     * OnClick
     */
    @OnClick(R.id.ivStart)
    public void onViewClicked() {
        defaultFinish();
    }
}
