package com.turingoal.mengbao.teacher.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.PhotoView;
import com.turingoal.android.photopicker.manager.GlideApp;
import com.turingoal.common.app.TgApplication;
import com.turingoal.common.base.TgBaseActivity;
import com.turingoal.common.utils.GlideUtil;
import com.turingoal.common.utils.PermissionUtil;
import com.turingoal.common.utils.TgSDCardUtil;
import com.turingoal.mengbao.common.biz.domain.SchoolPhoto;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;
import com.turingoal.mengbao.umengsharesdk.StyleUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 学校风采照片详情
 */
@Route(path = ConstantActivityPath.SCHOOL_PHOTO_DETAIL)
public class SchoolPhotoDetailActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ivEnd)
    ImageView ivEnd;
    @BindView(R.id.ivPicturesDetail)
    PhotoView ivPicturesDetail; // 可以缩放图片
    @BindView(R.id.tvDescription)
    TextView tvDescription; // 描述
    private UMShareListener mShareListener; // 分享回调监听
    private ShareAction mShareAction; // 分享内容
    private static final int REQUEST_CODE_EXTERNAL_STORAGE = 1001; // 6.0动态申请权限用
    @Autowired
    SchoolPhoto schoolPhoto;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_school_photo_detail;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_school_photo_detail);
        ivEnd.setVisibility(View.VISIBLE);
        tvDescription.setText(schoolPhoto.getDiscription());
        GlideUtil.load(this, schoolPhoto.getPhoto(), ivPicturesDetail); // 不需要验证是不是空数据，空数据会显示加载错误图片
        mShareListener = new SchoolPhotoDetailActivity.CustomShareListener(this);
        /*增加自定义按钮的分享面板*/
        mShareAction = new ShareAction(this).setDisplayList(StyleUtil.getShareMedia(this, StyleUtil.TEXTANDIMAGE))
                .addButton(getString(R.string.umeng_sharebutton_copy), getString(R.string.umeng_sharebutton_copy), "umeng_socialize_copy", "umeng_socialize_copy")
                .addButton(getString(R.string.umeng_sharebutton_copyurl), getString(R.string.umeng_sharebutton_copyurl), "umeng_socialize_copyurl", "umeng_socialize_copyurl")
                .setShareboardclickCallback(new SchoolPhotoDetailActivity.CustomShareBoardlistener());
    }

    /**
     * 分享一张图片
     */
    private void shareImg() {
        ShareBoardConfig config = new ShareBoardConfig();
        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_CIRCULAR); // 圆角背景
        mShareAction.open(config);
    }

    /**
     * 6.0动态申请权限回调
     */
    @Override
    public void onRequestPermissionsResult(final int requestCode, final @NonNull String[] permissions, final @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_EXTERNAL_STORAGE) {
            downloadImg();
        }
    }

    /**
     * 下载一张图片
     */
    private void downloadImg() {
        if (!PermissionUtil.getExternalStoragePermissions(this, REQUEST_CODE_EXTERNAL_STORAGE)) { // 没有权限
            return;
        }
        GlideApp.with(TgApplication.getContext()).asBitmap().load(schoolPhoto.getPhoto()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(final Bitmap resource, final Transition<? super Bitmap> transition) {
                TgSDCardUtil.saveImageToGallery(SchoolPhotoDetailActivity.this, resource);
            }
        });
    }

    /**
     * OnClick
     *
     * @param view
     */
    @OnClick({R.id.ivStart, R.id.ivEnd, R.id.fabDownload})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivStart:
                defaultFinish();
                break;
            case R.id.ivEnd:
                shareImg();
                break;
            case R.id.fabDownload:
                downloadImg();
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
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(ClipData.newPlainText(null, getString(R.string.activity_pictures_detail)));
                if (clipboardManager.hasPrimaryClip())
                    Toast.makeText(SchoolPhotoDetailActivity.this, getString(R.string.copy_success), Toast.LENGTH_SHORT).show();
            } else if (snsPlatform.mShowWord.equals(getString(R.string.umeng_sharebutton_copyurl))) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(ClipData.newPlainText(null, schoolPhoto.getPhoto()));
                if (clipboardManager.hasPrimaryClip())
                    Toast.makeText(SchoolPhotoDetailActivity.this, getString(R.string.copyurl_success), Toast.LENGTH_SHORT).show();
            } else {
                UMImage image = new UMImage(SchoolPhotoDetailActivity.this, schoolPhoto.getDiscription()); // 网络图片
                UMImage thumb = new UMImage(SchoolPhotoDetailActivity.this, R.drawable.ic_launcher);
                image.compressStyle = UMImage.CompressStyle.SCALE; // 大小压缩，默认为大小压缩，适合普通很大的图
                image.compressStyle = UMImage.CompressStyle.QUALITY; // 质量压缩，适合长图的分享
                // 压缩格式设置
                image.compressFormat = Bitmap.CompressFormat.PNG; // 用户分享透明背景的图片可以设置这种方式，但是qq好友，微信朋友圈，不支持透明背景图片，会变成黑色
                image.setThumb(thumb);
                new ShareAction(SchoolPhotoDetailActivity.this).withText(getString(R.string.activity_pictures_detail))
                        .setPlatform(shareMedia)
                        .setCallback(mShareListener)
                        .withMedia(image).share();
            }
        }
    }

    /**
     * 分享回调
     */
    private class CustomShareListener implements UMShareListener {

        private WeakReference<SchoolPhotoDetailActivity> mActivity;

        private CustomShareListener(final SchoolPhotoDetailActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void onStart(final SHARE_MEDIA platform) {
            // 分享开始
        }

        @Override
        public void onResult(final SHARE_MEDIA platform) {
            if (platform == SHARE_MEDIA.WEIXIN_FAVORITE) { // 微信收藏
                Toast.makeText(mActivity.get(), mActivity.get().getString(R.string.favorite_success), Toast.LENGTH_SHORT).show();
            } else { // 分享
                if (platform != SHARE_MEDIA.MORE
                        && platform != SHARE_MEDIA.SMS
                        && platform != SHARE_MEDIA.EMAIL) {
                    Toast.makeText(mActivity.get(), mActivity.get().getString(R.string.share_success), Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onError(final SHARE_MEDIA platform, final Throwable t) { // 失败
            Toast.makeText(mActivity.get(), mActivity.get().getString(R.string.share_fail), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(final SHARE_MEDIA platform) {
            // 分享取消
        }
    }
}
