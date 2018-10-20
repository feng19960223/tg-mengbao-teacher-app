package com.turingoal.mengbao.teacher.ui.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.request.PostRequest;
import com.turingoal.common.app.TgApplication;
import com.turingoal.common.app.TgSystemConfig;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseFragment;
import com.turingoal.common.bean.TgResponseBean;
import com.turingoal.common.utils.GlideUtil;
import com.turingoal.common.utils.TgDialogUtil;
import com.turingoal.common.utils.TgHttpCallback;
import com.turingoal.common.utils.TgHttpUtil;
import com.turingoal.common.utils.TgJsonUtil;
import com.turingoal.common.utils.TgStringUtil;
import com.turingoal.common.utils.TgSystemUtil;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.bean.User;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;
import com.turingoal.mengbao.teacher.constants.ConstantUrls;
import com.turingoal.mengbao.umengsharesdk.StyleUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的
 */
public class PersonalFragment extends TgBaseFragment {
    @BindView(R.id.tvName)
    TextView tvName; // 家长名字
    @BindView(R.id.tvResume)
    TextView tvResume; // 家长签名
    @BindView(R.id.ivAvatar)
    ImageView ivAvatar; // 家长头像
    private UMShareListener mShareListener; // 分享回调监听
    private ShareAction mShareAction; // 分享内容

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_personal;
    }

    @Override
    protected void initialized() {
        mShareListener = new CustomShareListener(this);
        /*增加自定义按钮的分享面板*/
        // 分享描述
        mShareAction = new ShareAction(getActivity()).setDisplayList(StyleUtil.getShareMedia(getActivity(), StyleUtil.WEB11))
                .addButton(getString(R.string.umeng_sharebutton_copy), getString(R.string.umeng_sharebutton_copy), "umeng_socialize_copy", "umeng_socialize_copy")
                .addButton(getString(R.string.umeng_sharebutton_copyurl), getString(R.string.umeng_sharebutton_copyurl), "umeng_socialize_copyurl", "umeng_socialize_copyurl")
                .setShareboardclickCallback(new CustomShareBoardlistener());
    }

    @Override
    public void onResume() {
        super.onResume();
        GlideUtil.loadImage(getContext(), TgSystemHelper.getAvatar(), ivAvatar); // 头像
        tvName.setText(TgStringUtil.getSelectStr(TgSystemHelper.getRealname(), getString(R.string.default_teacher_name)));
        tvResume.setText(TgStringUtil.getSelectStr(TgSystemHelper.getResume(), getString(R.string.default_resume)));
        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_USER_DETAIL, getHttpTaskKey());
        request.params("id", TgSystemHelper.getUserId());
        request.execute(new TgHttpCallback(null) {
            @Override
            public void successHandler(final TgResponseBean result) {
                if (result.isSuccess()) {
                    if (result.getData() == null) {
                        return;
                    }
                    User user = TgJsonUtil.jsonResultBean2Object(result, User.class);
                    if (user != null) {
                        TgApplication.getTgUserPreferences().setAvatar(user.getAvatar());
                        TgApplication.getTgUserPreferences().setRealname(user.getRealname());
                        TgApplication.getTgUserPreferences().setResume(user.getSignature());
                        GlideUtil.loadImage(getContext(), TgSystemHelper.getAvatar(), ivAvatar); // 头像
                        tvName.setText(TgStringUtil.getSelectStr(TgSystemHelper.getRealname(), getString(R.string.default_teacher_name)));
                        tvResume.setText(TgStringUtil.getSelectStr(TgSystemHelper.getResume(), getString(R.string.default_resume)));
                    }
                } else {
                    TgDialogUtil.showToast(result.getMsg()); // 弹出错误信息
                }
            }
        });
    }

    /**
     * OnClick
     */
    @OnClick({R.id.ivAvatar, R.id.tvCollect, R.id.tvEmail, R.id.tvShare, R.id.tvSetting})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.ivAvatar: // 头像
                TgSystemHelper.handleIntent(ConstantActivityPath.INFO_TEACHER);
                break;
            case R.id.tvCollect: // 收藏
                TgSystemHelper.handleIntent(ConstantActivityPath.COLLECT);
                break;
            case R.id.tvEmail: // 园长邮箱
                TgSystemUtil.mail(getContext(), TgSystemHelper.getSchoolEmail()); // 发送邮件
                break;
            case R.id.tvShare: // 分享应用
                // 打开分享面板
                ShareBoardConfig config = new ShareBoardConfig();
                config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_CIRCULAR); // 圆角背景
                mShareAction.open(config);
                break;
            case R.id.tvSetting: // 设置
                TgSystemHelper.handleIntent(ConstantActivityPath.SETTING);
                break;
            default:
                break;
        }
    }


    /**
     * 分享item点击事件
     */
    private class CustomShareBoardlistener implements ShareBoardlistener {
        @Override
        public void onclick(final SnsPlatform snsPlatform, final SHARE_MEDIA shareMedia) {
            if (snsPlatform.mShowWord.equals(getString(R.string.umeng_sharebutton_copy))) {
                ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(ClipData.newPlainText(null, getString(R.string.app_description)));
                if (clipboardManager.hasPrimaryClip())
                    Toast.makeText(getContext(), getString(R.string.copy_success), Toast.LENGTH_SHORT).show();
            } else if (snsPlatform.mShowWord.equals(getString(R.string.umeng_sharebutton_copyurl))) {
                ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(ClipData.newPlainText(null, TgSystemConfig.APP_DOWNLOAD_URL));
                if (clipboardManager.hasPrimaryClip())
                    Toast.makeText(getContext(), getString(R.string.copyurl_success), Toast.LENGTH_SHORT).show();
            } else {
                UMWeb web = new UMWeb(TgSystemConfig.APP_DOWNLOAD_URL);
                web.setTitle(getString(R.string.app_name));
                web.setDescription(getString(R.string.app_description));
                web.setThumb(new UMImage(getActivity(), R.drawable.ic_launcher));
                new ShareAction(getActivity()).withMedia(web)
                        .setPlatform(shareMedia)
                        .setCallback(mShareListener)
                        .share();
            }
        }
    }


    /**
     * 分享回调
     */
    private static class CustomShareListener implements UMShareListener {

        private WeakReference<PersonalFragment> mFragment;

        private CustomShareListener(final PersonalFragment fragment) {
            mFragment = new WeakReference<>(fragment);
        }

        @Override
        public void onStart(final SHARE_MEDIA platform) {
            // 分享开始
        }

        @Override
        public void onResult(final SHARE_MEDIA platform) {
            if (platform == SHARE_MEDIA.WEIXIN_FAVORITE) { // 微信收藏
                Toast.makeText(mFragment.get().getContext(), mFragment.get().getContext().getString(R.string.favorite_success), Toast.LENGTH_SHORT).show();
            } else { // 分享
                if (platform != SHARE_MEDIA.MORE
                        && platform != SHARE_MEDIA.SMS
                        && platform != SHARE_MEDIA.EMAIL) {
                    Toast.makeText(mFragment.get().getContext(), mFragment.get().getContext().getString(R.string.share_success), Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onError(final SHARE_MEDIA platform, final Throwable t) { // 失败
            Toast.makeText(mFragment.get().getContext(), mFragment.get().getContext().getString(R.string.share_fail), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(final SHARE_MEDIA platform) {
            // 分享取消
        }
    }
}
