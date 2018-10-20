package com.turingoal.mengbao.teacher.ui.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.lzy.okgo.request.PostRequest;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseActivity;
import com.turingoal.common.bean.TgResponseBean;
import com.turingoal.common.utils.GlideUtil;
import com.turingoal.common.utils.TgDialogUtil;
import com.turingoal.common.utils.TgHttpCallback;
import com.turingoal.common.utils.TgHttpUtil;
import com.turingoal.common.utils.TgSystemUtil;
import com.turingoal.common.widget.ClearEditText;
import com.turingoal.mengbao.common.biz.domain.Child;
import com.turingoal.mengbao.common.constants.TgConstantAttendanceType;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;
import com.turingoal.mengbao.teacher.constants.ConstantUrls;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 增加考勤记录
 */
@Route(path = ConstantActivityPath.ATTENDANCE_ADD)
public class AttendanceAddActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvEnd)
    TextView tvEnd; // 确定
    @BindView(R.id.ivChlidAvatar)
    ImageView ivChlidAvatar; // 孩子头像
    @BindView(R.id.tvChlidName)
    TextView tvChlidName; // 孩子名字
    @BindView(R.id.ivParentsAvatar)
    ImageView ivParentsAvatar; // 家长头像
    @BindView(R.id.tvParentsName)
    TextView tvParentsName; // 家长名字
    @BindView(R.id.rgCause)
    RadioGroup rgCause; // 原因
    @BindView(R.id.etRemakes)
    ClearEditText etRemakes; // 备注
    @BindView(R.id.tvCount)
    TextView tvCount; // 字数
    @Autowired
    Child child;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_attendance_add;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_attendance_add);
        tvEnd.setVisibility(View.VISIBLE);
        GlideUtil.loadImage(this, child.getAvatar(), ivChlidAvatar);
        tvChlidName.setText(child.getRealname());
        GlideUtil.loadImage(AttendanceAddActivity.this, child.getParentAvatar(), ivParentsAvatar);
        tvParentsName.setText(child.getUserRealname());
        etRemakes.addTextChangedListener(remakesTextWatcher);
    }

    /**
     * 备注EditText监听
     */
    private TextWatcher remakesTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }

        @Override
        public void onTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }

        @Override
        public void afterTextChanged(final Editable editable) {
            tvCount.setText(editable.toString().trim().length() + "/140");
        }
    };

    /**
     * OnClick
     */
    @OnClick({R.id.ivStart, R.id.tvEnd, R.id.ivCall, R.id.ivSms})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.ivStart: // 返回
                defaultFinish();
                break;
            case R.id.tvEnd: // 提交
                addRequest();
                break;
            case R.id.ivCall: // 电话
                TgSystemUtil.call(child.getCellPhoneNum());
                break;
            case R.id.ivSms:
                TgSystemUtil.sms(child.getCellPhoneNum());
                break;
            default:
                break;
        }
    }

    /**
     * 新增加考勤记录
     */
    private void addRequest() {
        // 得到选中的类型
        String typeStr = ((RadioButton) findViewById(rgCause.getCheckedRadioButtonId())).getText().toString();
        int type = TgConstantAttendanceType.LATE;
        if (typeStr.equals(getString(R.string.attendance2))) {
            type = TgConstantAttendanceType.LEAVE_EARLY;
        } else if (typeStr.equals(getString(R.string.attendance3))) {
            type = TgConstantAttendanceType.ABSENTEEISM;
        } else if (typeStr.equals(getString(R.string.attendance4))) {
            type = TgConstantAttendanceType.LEAVE;
        }
        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_ATTENDANCE_ADD, getHttpTaskKey());
        request.params("childId", child.getId());
        request.params("childCodeNum", child.getClassCodeNum());
        request.params("childRealname", child.getRealname());
        request.params("type", type);
        request.params("remakes", etRemakes.getText().toString().trim());
        request.params("schoolId", TgSystemHelper.getSchoolId());
        request.params("schoolCodeNum", TgSystemHelper.getSchoolCodeNum());
        request.params("schoolTitle", TgSystemHelper.getSchoolName());
        request.params("userId", TgSystemHelper.getUserId());
        request.params("userCodeNum", TgSystemHelper.getUserId());
        request.params("userRealname", TgSystemHelper.getRealname());
        request.params("classId", TgSystemHelper.getClassId());
        request.params("classCodeNum", TgSystemHelper.getClassCodeNum());
        request.params("classTitle", TgSystemHelper.getClassName());
        request.params("classGrade", TgSystemHelper.getClassGrade());
        request.execute(new TgHttpCallback(this) {
            @Override
            public void successHandler(final TgResponseBean result) {
                if (result.isSuccess()) {
                    defaultFinish();
                } else {
                    TgDialogUtil.showToast(result.getMsg()); // 登录失败，弹出错误信息
                }
            }
        });
    }
}
