package com.turingoal.mengbao.teacher.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lzy.okgo.request.PostRequest;
import com.turingoal.android.photopicker.PhotoPicker;
import com.turingoal.common.app.TgApplication;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseActivity;
import com.turingoal.common.bean.TgResponseBean;
import com.turingoal.common.utils.GlideUtil;
import com.turingoal.common.utils.TgDialogUtil;
import com.turingoal.common.utils.TgHttpCallback;
import com.turingoal.common.utils.TgHttpUtil;
import com.turingoal.common.utils.TgStringUtil;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;
import com.turingoal.mengbao.teacher.constants.ConstantUrls;
import com.turingoal.mengbao.teacher.event.EventConsts;
import com.turingoal.mengbao.teacher.event.EventLogger;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 宝宝个人信息
 */
@Route(path = ConstantActivityPath.INFO_TEACHER)
public class InfoTeacherActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ivAvatarBg)
    ImageView ivAvatarBg; // 头像背景，高斯模糊
    @BindView(R.id.ivAvatar)
    ImageView ivAvatar; // 头像
    @BindView(R.id.tvResume)
    TextView tvResume; // 个性签名
    @BindView(R.id.tvTeacherName)
    TextView tvTeacherName; // 名字
    @BindView(R.id.tvPhone)
    TextView tvPhone; // 电话

    @Override
    protected int getLayoutID() {
        return R.layout.activity_info_teacher;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_info_teacher);
        tvTeacherName.setText(TgSystemHelper.getRealname()); // 姓名
        GlideUtil.loadImage(InfoTeacherActivity.this, TgSystemHelper.getAvatar(), ivAvatar); // 头像
        GlideUtil.loadBlur(InfoTeacherActivity.this, TgSystemHelper.getAvatar(), ivAvatarBg); // 头像背景
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvResume.setText(TgStringUtil.getSelectStr(TgSystemHelper.getResume(), getString(R.string.default_resume))); // 个性签名
        String phone = TgSystemHelper.getPhone(); // 手机号码
        String phoneNumber = phone;
        if (phone.length() == 11) {
            phoneNumber = phone.substring(0, 3) + "****" + phone.substring(7, phone.length()); // 中间4位替换为*号
        }
        tvPhone.setText(phoneNumber); // 手机号码
    }

    /**
     * 头像
     */
    private void avatar() {
        EventLogger.logEvent(EventConsts.e_Id_Avatar);
        PhotoPicker.selectPic(this, 600, new PhotoPicker.PicCallBack() {
            @Override
            public void onPicSelected(final String[] paths) {
                PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_USER_AVATAR, getHttpTaskKey());
                request.params("id", TgSystemHelper.getUserId());
                request.params("avatar", new File(paths[0]));
                request.execute(new TgHttpCallback(InfoTeacherActivity.this) {
                    @Override
                    public void successHandler(final TgResponseBean result) {
                        if (result.isSuccess()) {
                            TgApplication.getTgUserPreferences().setAvatar(paths[0]);
                            GlideUtil.loadImage(InfoTeacherActivity.this, paths[0], ivAvatar); // 头像
                            GlideUtil.loadBlur(InfoTeacherActivity.this, paths[0], ivAvatarBg); // 头像背景
                        } else {
                            TgDialogUtil.showToast(result.getMsg()); // 登录失败，弹出错误信息
                        }
                    }
                });
            }
        });
    }

    /**
     * OnClick
     */
    @OnClick({R.id.ivStart, R.id.flAvatar, R.id.tvResume, R.id.llTeacherPhone, R.id.llBindingAccount, R.id.tvChangePassword, R.id.tvQuit})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.ivStart: // 返回
                defaultFinish();
                break;
            case R.id.flAvatar: // 头像
                avatar();
                break;
            case R.id.tvResume: // 个性签名
                TgSystemHelper.handleIntent(ConstantActivityPath.INFO_TEACHER_RESUME);
                break;
            case R.id.llTeacherPhone: // 电话
                TgSystemHelper.handleIntent(ConstantActivityPath.CHANGE_PHONE);
                break;
            case R.id.llBindingAccount:
                TgSystemHelper.handleIntent(ConstantActivityPath.BINDING_ACCOUNT);
                break;
            case R.id.tvChangePassword: // 修改密码
                TgSystemHelper.handleIntent(ConstantActivityPath.CHANGE_PASSWORD);
                break;
            case R.id.tvQuit: // 退出
                TgSystemHelper.logout(this); // 注销退出系统
                break;
            default:
                break;
        }
    }
}
