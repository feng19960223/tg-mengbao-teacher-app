package com.turingoal.mengbao.teacher.ui.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lzy.okgo.request.PostRequest;
import com.turingoal.common.app.TgApplication;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseActivity;
import com.turingoal.common.bean.TgResponseBean;
import com.turingoal.common.utils.TgDialogUtil;
import com.turingoal.common.utils.TgHttpCallback;
import com.turingoal.common.utils.TgHttpUtil;
import com.turingoal.mengbao.common.constants.TgConstantLoginType;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;
import com.turingoal.mengbao.teacher.constants.ConstantUrls;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 账号绑定
 */
@Route(path = ConstantActivityPath.BINDING_ACCOUNT)
public class BindingAccountActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvQQ)
    TextView tvQQ;
    @BindView(R.id.tvWeChat)
    TextView tvWeChat;
    @BindView(R.id.tvSina)
    TextView tvSina;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_binding_account;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_binding_account);
        tvQQ.setText(TgSystemHelper.isBindingQQ() ? getString(R.string.binding_yes) : getString(R.string.binding_no));
        tvWeChat.setText(TgSystemHelper.isBindingWeChat() ? getString(R.string.binding_yes) : getString(R.string.binding_no));
        tvSina.setText(TgSystemHelper.isBindingSina() ? getString(R.string.binding_yes) : getString(R.string.binding_no));
    }

    // 微信绑定
    private UMAuthListener weChatAuthListener = new UMAuthListener() {
        @Override
        public void onStart(final SHARE_MEDIA platform) {
        }

        @Override
        public void onComplete(final SHARE_MEDIA platform, final int action, final Map<String, String> data) {
            Log.d("UMAuthListener", "onComplete: " + platform);
            Log.d("UMAuthListener", "onComplete: " + data.toString());
            MobclickAgent.onProfileSignIn(platform.getName(), data.get("uid"));
            bindingAccountRequest(TgConstantLoginType.TYPE_WECHAT,data.get("uid"));
        }

        @Override
        public void onError(final SHARE_MEDIA platform, final int action, final Throwable t) {
            if (t != null) {
                Log.d("UMAuthListener", "onError: " + t.getMessage());
            }
        }

        @Override
        public void onCancel(final SHARE_MEDIA platform, final int action) {
        }
    };

    // 新浪绑定
    private UMAuthListener sinaAuthListener = new UMAuthListener() {
        @Override
        public void onStart(final SHARE_MEDIA platform) {
        }

        @Override
        public void onComplete(final SHARE_MEDIA platform, final int action, final Map<String, String> data) {
            Log.d("UMAuthListener", "onComplete: " + platform);
            Log.d("UMAuthListener", "onComplete: " + data.toString());
            MobclickAgent.onProfileSignIn(platform.getName(), data.get("uid"));
            bindingAccountRequest(TgConstantLoginType.TYPE_SINA,data.get("uid"));
        }

        @Override
        public void onError(final SHARE_MEDIA platform, final int action, final Throwable t) {
            if (t != null) {
                Log.d("UMAuthListener", "onError: " + t.getMessage());
            }
        }

        @Override
        public void onCancel(final SHARE_MEDIA platform, final int action) {
        }
    };

    // qq绑定
    private UMAuthListener qqAuthListener = new UMAuthListener() {
        @Override
        public void onStart(final SHARE_MEDIA platform) {
        }

        @Override
        public void onComplete(final SHARE_MEDIA platform, final int action, final Map<String, String> data) {
            Log.d("UMAuthListener", "onComplete: " + platform);
            Log.d("UMAuthListener", "onComplete: " + data.toString());
            MobclickAgent.onProfileSignIn(platform.getName(), data.get("uid"));
            bindingAccountRequest(TgConstantLoginType.TYPE_QQ,data.get("uid"));
        }

        @Override
        public void onError(final SHARE_MEDIA platform, final int action, final Throwable t) {
            if (t != null) {
                Log.d("UMAuthListener", "onError: " + t.getMessage());
            }
        }

        @Override
        public void onCancel(final SHARE_MEDIA platform, final int action) {
        }
    };

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 登录页面必须有这行代码，否则回调不正确
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 账号绑定uid
     */
    private void bindingAccountRequest(final int bindingType, final String uid) {
        String path = ConstantUrls.URL_USER_BINDING_SDK_QQ;
        if (TgConstantLoginType.isWeChat(bindingType)) {
            path = ConstantUrls.URL_USER_BINDING_SDK_WECHAT;
        } else if (TgConstantLoginType.isSina(bindingType)) {
            path = ConstantUrls.URL_USER_BINDING_SDK_SINA;
        }
        PostRequest request = TgHttpUtil.requestPost(path, getHttpTaskKey());
        request.params("userId", TgSystemHelper.getUserId());
        request.params("uid", uid);
        request.execute(new TgHttpCallback(null) {
            @Override
            public void successHandler(final TgResponseBean result) {
                if (result.isSuccess()) { // 成功
                    if (bindingType == 1) {
                        tvSina.setText(R.string.binding_yes);
                        TgApplication.getTgUserPreferences().setBindingSina(true);
                    } else if (bindingType == 2) {
                        tvWeChat.setText(R.string.binding_yes);
                        TgApplication.getTgUserPreferences().setBindingWeChat(true);
                    } else {
                        tvQQ.setText(R.string.binding_yes);
                        TgApplication.getTgUserPreferences().setBindingQQ(true);
                    }
                } else {
                    TgDialogUtil.showToast(result.getMsg()); // 登录失败，弹出错误信息
                }
            }
        });
    }

    /**
     * OnClick
     */
    @OnClick({R.id.ivStart, R.id.llQQ, R.id.llWeChat, R.id.llSina})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.ivStart:
                defaultFinish();
                break;
            case R.id.llQQ:
                UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.QQ, qqAuthListener);
                break;
            case R.id.llWeChat:
                UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.WEIXIN, weChatAuthListener);
                break;
            case R.id.llSina:
                UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.SINA, sinaAuthListener);
                break;
            default:
                break;
        }
    }
}
