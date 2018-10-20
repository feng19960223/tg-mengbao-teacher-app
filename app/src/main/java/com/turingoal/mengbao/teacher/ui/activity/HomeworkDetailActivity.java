package com.turingoal.mengbao.teacher.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseActivity;
import com.turingoal.mengbao.common.biz.domain.Homework;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 亲子作业详情
 */
@Route(path = ConstantActivityPath.HOMEWORK_DETAIL)
public class HomeworkDetailActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvHomeworkTitle)
    TextView tvHomeworkTitle;
    @BindView(R.id.tvSource)
    TextView tvSource; // 来源
    @BindView(R.id.tvDate)
    TextView tvDate; // 时间
    @BindView(R.id.tvContent)
    TextView tvContent; // 内容
    @BindView(R.id.btnYes)
    Button btnYes; // 已完成
    @BindView(R.id.btnNo)
    Button btnNo; // 未完成
    @Autowired
    Homework homework;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_homework_detail;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_homework_detail);
        tvHomeworkTitle.setText(homework.getTitle());
        tvSource.setText(homework.getUserRealname());
        tvDate.setText(homework.getCreateTimeStr4DateTime());
        tvContent.setText(homework.getContent());
        btnYes.setText(String.format(getString(R.string.homework_yes), homework.getCompleted()));
        btnNo.setText(String.format(getString(R.string.homework_no), homework.getUndone()));
    }

    /**
     * OnClick
     */
    @OnClick({R.id.ivStart, R.id.btnYes, R.id.btnNo})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.ivStart: // 返回
                defaultFinish();
                break;
            case R.id.btnYes: // 已经完成作业
                TgSystemHelper.handleIntentWithObj(ConstantActivityPath.HOMEWORK_YES, "homework", homework);
                break;
            case R.id.btnNo: // 没有完成作业
                TgSystemHelper.handleIntentWithObj(ConstantActivityPath.HOMEWORK_NO, "homework", homework);
                break;
            default:
                break;
        }
    }
}
