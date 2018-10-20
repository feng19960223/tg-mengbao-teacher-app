package com.turingoal.mengbao.teacher.ui.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lzy.okgo.request.PostRequest;
import com.turingoal.android.photopicker.PhotoPicker;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseActivity;
import com.turingoal.common.bean.TgResponseBean;
import com.turingoal.common.utils.GlideUtil;
import com.turingoal.common.utils.TgDialogUtil;
import com.turingoal.common.utils.TgHttpCallback;
import com.turingoal.common.utils.TgHttpUtil;
import com.turingoal.common.utils.TgStringUtil;
import com.turingoal.common.widget.ClearEditText;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;
import com.turingoal.mengbao.teacher.constants.ConstantUrls;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 增加相册
 */
@Route(path = ConstantActivityPath.ALBUM_ADD)
public class AlbumAddActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvEnd)
    TextView tvEnd; // 确定
    @BindView(R.id.etTitle)
    ClearEditText etTitle; // 标题
    @BindView(R.id.tvTitleCount)
    TextView tvTitleCount; // 标题字数
    @BindView(R.id.etDescription)
    ClearEditText etDescription; // 描述
    @BindView(R.id.tvDescriptionCount)
    TextView tvDescriptionCount; // 标题字数
    @BindView(R.id.ivCover)
    ImageView ivCover; // 封面
    private boolean isTitle = false;
    private boolean isDescription = false;
    private String coverUrl = "";

    @Override
    protected int getLayoutID() {
        return R.layout.activity_album_add;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_album_add);
        tvEnd.setVisibility(View.VISIBLE);
        tvEnd.setEnabled(false);
        etTitle.addTextChangedListener(titleTextWatcher);
        etDescription.addTextChangedListener(descriptionTextWatcher);
        etTitle.setOnTextClearListener(new ClearEditText.OnTextClearListener() { // 清空按钮
            @Override
            public void textClear() {
                isTitle = false;
                tvEnd.setEnabled(false);
            }
        });
        etDescription.setOnTextClearListener(new ClearEditText.OnTextClearListener() { // 清空按钮
            @Override
            public void textClear() {
                isDescription = false;
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
            tvTitleCount.setText(editable.toString().trim().length() + "/10");
            if (isTitle && isDescription && !TgStringUtil.isEmpty(coverUrl)) {
                tvEnd.setEnabled(true);
            } else {
                tvEnd.setEnabled(false);
            }
        }
    };

    /**
     * 描述EditText监听
     */
    private TextWatcher descriptionTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }

        @Override
        public void onTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }

        @Override
        public void afterTextChanged(final Editable editable) {
            if (editable.toString().trim().length() != 0) {
                isDescription = true;
            } else {
                isDescription = false;
            }
            tvDescriptionCount.setText(editable.toString().trim().length() + "/40");
            if (isTitle && isDescription && !TgStringUtil.isEmpty(coverUrl)) {
                tvEnd.setEnabled(true);
            } else {
                tvEnd.setEnabled(false);
            }
        }
    };

    /**
     * 增加
     */
    private void addRequest() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_ALBUM_ADD, getHttpTaskKey());
        request.params("title", title);
        request.params("description", description);
        request.params("img", new File(coverUrl));
        request.params("userId", TgSystemHelper.getUserId());
        request.params("userCodeNum", TgSystemHelper.getUserCodeNum());
        request.params("userRealname", TgSystemHelper.getRealname());
        request.params("schoolId", TgSystemHelper.getSchoolId());
        request.params("schoolCodeNum", TgSystemHelper.getSchoolCodeNum());
        request.params("schoolTitle", TgSystemHelper.getSchoolName());
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

    /**
     * OnClick
     */
    @OnClick({R.id.ivStart, R.id.tvEnd, R.id.ivCover})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.ivStart: // 返回
                defaultFinish();
                break;
            case R.id.tvEnd: // 确定
                addRequest();
                break;
            case R.id.ivCover: // 封面图片
                PhotoPicker.selectPic(this, 600, new PhotoPicker.PicCallBack() {
                    @Override
                    public void onPicSelected(String[] strings) {
                        coverUrl = strings[0];
                        GlideUtil.load(AlbumAddActivity.this, strings[0], ivCover, R.drawable.ic_add_pictures); // 头像背景
                        if (isTitle && isDescription && !TgStringUtil.isEmpty(coverUrl)) {
                            tvEnd.setEnabled(true);
                        } else {
                            tvEnd.setEnabled(false);
                        }
                    }
                });
                break;
            default:
                break;
        }
    }
}
