package com.turingoal.mengbao.teacher.ui.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lzy.okgo.request.PostRequest;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseActivity;
import com.turingoal.common.bean.TgResponseBean;
import com.turingoal.common.utils.TgDialogUtil;
import com.turingoal.common.utils.TgHttpCallback;
import com.turingoal.common.utils.TgHttpUtil;
import com.turingoal.common.widget.ClearEditText;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;
import com.turingoal.mengbao.teacher.constants.ConstantUrls;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 新自己亲子作业activity
 */
@Route(path = ConstantActivityPath.HOMEWORK_ADD)
public class HomeworkAddActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvEnd)
    TextView tvEnd;
    @BindView(R.id.etTitle)
    ClearEditText etTitle; // title
    @BindView(R.id.tvTitleCount)
    TextView tvTitleCount; // title字数
    @BindView(R.id.etContent)
    ClearEditText etContent; // 内容
    @BindView(R.id.tvContentCount)
    TextView tvContentCount; // 内容字数
    private boolean isTitle = false;
    private boolean isContent = false;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_homework_add;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_homework_add);
        tvEnd.setVisibility(View.VISIBLE);
        tvEnd.setEnabled(false);
        etTitle.addTextChangedListener(titleTextWatcher);
        etContent.addTextChangedListener(contentTextWatcher);
        etTitle.setOnTextClearListener(new ClearEditText.OnTextClearListener() { // 清空按钮
            @Override
            public void textClear() {
                isTitle = false;
                tvEnd.setEnabled(false);
            }
        });
        etContent.setOnTextClearListener(new ClearEditText.OnTextClearListener() { // 清空按钮
            @Override
            public void textClear() {
                isContent = false;
                tvEnd.setEnabled(false);
            }
        });
    }

    /**
     * 标题EditText监听
     */
    private TextWatcher titleTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }

        @Override
        public void onTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }

        @Override
        public void afterTextChanged(final Editable editable) {
            if (editable.toString().trim().length() != 0) {
                isTitle = true;
            } else {
                isTitle = false;
            }
            tvTitleCount.setText(editable.toString().trim().length() + "/30");
            if (isTitle && isContent) {
                tvEnd.setEnabled(true);
            } else {
                tvEnd.setEnabled(false);
            }
        }
    };

    /**
     * 内容EditText监听
     */
    private TextWatcher contentTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }

        @Override
        public void onTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }

        @Override
        public void afterTextChanged(final Editable editable) {
            if (editable.toString().trim().length() != 0) {
                isContent = true;
            } else {
                isContent = false;
            }
            tvContentCount.setText(editable.toString().trim().length() + "/140");
            if (isTitle && isContent) {
                tvEnd.setEnabled(true);
            } else {
                tvEnd.setEnabled(false);
            }
        }
    };

    /**
     * 新增加作业
     */
    private void addRequest() {
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();
        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_HOMEWORK_ADD, getHttpTaskKey());
        request.params("title", title);
        request.params("content", content);
        request.params("userId", TgSystemHelper.getUserId());
        request.params("userCodeNum", TgSystemHelper.getUserCodeNum());
        request.params("userRealname", TgSystemHelper.getRealname());
        request.params("schoolId", TgSystemHelper.getSchoolId());
        request.params("schoolCodeNum", TgSystemHelper.getSchoolCodeNum());
        request.params("schoolTitle", TgSystemHelper.getSchoolName());
        request.params("classId", TgSystemHelper.getClassId());
        request.params("classCodeNum", TgSystemHelper.getClassCodeNum());
        request.params("classTitle", TgSystemHelper.getClassName());
        request.params("classGrade", TgSystemHelper.getClassGrade() );
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

    /**
     * OnClick
     */
    @OnClick({R.id.ivStart, R.id.tvEnd})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.ivStart: // 返回
                defaultFinish();
                break;
            case R.id.tvEnd: // 发布作业
                addRequest();
                break;
            default:
                break;
        }
    }
}
