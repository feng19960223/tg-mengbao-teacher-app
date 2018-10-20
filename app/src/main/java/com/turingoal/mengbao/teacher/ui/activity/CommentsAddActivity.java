package com.turingoal.mengbao.teacher.ui.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.turingoal.common.widget.ClearEditText;
import com.turingoal.mengbao.common.biz.domain.Child;
import com.turingoal.mengbao.common.constants.TgBtsConstantYesNo;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;
import com.turingoal.mengbao.teacher.constants.ConstantUrls;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 新增加点评
 */
@Route(path = ConstantActivityPath.COMMENTS_ADD)
public class CommentsAddActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvEnd)
    TextView tvEnd; // 确定
    @BindView(R.id.tvFlowerHint)
    TextView tvFlowerHint; // 剩余花朵
    @BindView(R.id.ivAvatar)
    ImageView ivAvatar; // 头像
    @BindView(R.id.tvName)
    TextView tvName; // 名字
    @BindView(R.id.etContent)
    ClearEditText etContent; // 内容
    @BindView(R.id.tvContentCount)
    TextView tvContentCount; // 内容字数
    @BindView(R.id.llFlower)
    LinearLayout llFlower; // 送花试图控制
    @BindView(R.id.cbFlower)
    CheckBox cbFlower; // 送花
    @Autowired
    Child child;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_comments_add;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_comments_add);
        tvEnd.setVisibility(View.VISIBLE);
        tvEnd.setEnabled(false);
        GlideUtil.loadImage(this, child.getAvatar(), ivAvatar);
        tvName.setText(child.getRealname());
        etContent.addTextChangedListener(contentTextWatcher);
        etContent.setOnTextClearListener(new ClearEditText.OnTextClearListener() { // 清空按钮
            @Override
            public void textClear() {
                tvEnd.setEnabled(false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    /**
     * 得到老师剩余的点评花朵数量
     */
    private void getData() {
        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_COMMENTS_FLOWER, getHttpTaskKey());
        request.params("userIdTeacher", TgSystemHelper.getUserId());
        request.execute(new TgHttpCallback(this) {
            @Override
            public void successHandler(final TgResponseBean result) {
                if (result.isSuccess()) {
                    if (result.getData() == null) {
                        return;
                    }
                    Map<String, Object> map = (Map<String, Object>) result.getData();
                    int flowerCount = (Integer) map.get("count");
                    if (flowerCount == 0) {
                        tvFlowerHint.setVisibility(View.GONE);
                        llFlower.setVisibility(View.GONE);
                    } else {
                        tvFlowerHint.setText(String.format(getString(R.string.comment_flower_hint1), flowerCount));
                        tvFlowerHint.setVisibility(View.VISIBLE);
                        llFlower.setVisibility(View.VISIBLE);
                    }
                } else {
                    TgDialogUtil.showToast(result.getMsg()); // 弹出错误信息
                }
            }
        });
    }

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
                tvEnd.setEnabled(true);
            } else {
                tvEnd.setEnabled(false);
            }
            tvContentCount.setText(editable.toString().trim().length() + "/140");
        }
    };

    /**
     * 增加
     */
    private void addRequest() {
        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_COMMENTS_ADD, getHttpTaskKey());
        request.params("content", etContent.getText().toString().trim());
        request.params("userIdTeacher", TgSystemHelper.getUserId());
        request.params("userCodeNumTeacher", TgSystemHelper.getUserCodeNum());
        request.params("userRealnameTeacher", TgSystemHelper.getRealname());
        request.params("userIdParent", child.getUserId());
        // request.params("userCodeNumParent", child.getUserCodeNum());
        request.params("userRealnameParent", child.getUserRealname());
        request.params("childId", child.getId());
        request.params("childCodeNum", child.getCodeNum());
        request.params("childRealname", child.getRealname());
        request.params("schoolId", TgSystemHelper.getSchoolId());
        request.params("schoolCodeNum", TgSystemHelper.getSchoolCodeNum());
        request.params("schoolTitle", TgSystemHelper.getSchoolName());
        request.params("classId", TgSystemHelper.getClassId());
        request.params("classCodeNum", TgSystemHelper.getClassCodeNum());
        request.params("classTitle", TgSystemHelper.getClassName());
        request.params("classGrade", TgSystemHelper.getClassGrade());
        if (cbFlower.isChecked()) {
            request.params("flower", TgBtsConstantYesNo.YES);
        } else {
            request.params("flower", TgBtsConstantYesNo.NO);
        }
        request.execute(new TgHttpCallback(this) {
            @Override
            public void successHandler(final TgResponseBean result) {
                if (result.isSuccess()) {
                    defaultFinish();
                } else {
                    TgDialogUtil.showToast(result.getMsg()); // 弹出错误信息
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
            case R.id.tvEnd: // 确定
                addRequest();
                break;
            default:
                break;
        }
    }
}
