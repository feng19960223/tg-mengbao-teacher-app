package com.turingoal.mengbao.teacher.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.turingoal.android.photopicker.manager.GlideApp;
import com.turingoal.common.app.TgApplication;
import com.turingoal.common.base.TgBaseActivity;
import com.turingoal.common.utils.PermissionUtil;
import com.turingoal.common.utils.TgSDCardUtil;
import com.turingoal.mengbao.common.biz.domain.AlbumGroup;
import com.turingoal.mengbao.common.biz.domain.AlbumPhoto;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;
import com.turingoal.mengbao.teacher.ui.adapter.PicturesItemAdapter;
import com.turingoal.mengbao.teacher.ui.adapter.PicturesViewPagerAdapter;
import com.turingoal.mengbao.teacher.ui.fragment.PhotoFragment;
import com.turingoal.mengbao.umengsharesdk.StyleUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 班级照片详情页面
 */
@Route(path = ConstantActivityPath.PICTURES_DETAIL)
public class PicturesDetailActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle; // title
    @BindView(R.id.ivEnd)
    ImageView ivEnd;
    @BindView(R.id.vpPictures)
    ViewPager vpPictures; // 图片
    @BindView(R.id.tvCount)
    TextView tvCount; // 计数
    @Autowired
    AlbumGroup albumGroup; // 照片
    private static final int REQUEST_CODE_EXTERNAL_STORAGE = 1001; // 6.0动态申请权限用
    private List<AlbumPhoto> albumPhotos; // 图片数据list
    private int index = PicturesItemAdapter.ImgUrlIndex;  // 显示那张图片
    private ArrayList<PhotoFragment> fragments; // 图片fragment
    private AlbumPhoto albumPhoto; // 当前图片的url
    private UMShareListener mShareListener; // 分享回调监听
    private ShareAction mShareAction; // 分享内容

    @Override
    protected int getLayoutID() {
        return R.layout.activity_pictures_detail;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_pictures_detail);
        ivEnd.setVisibility(View.VISIBLE);
        albumPhotos = albumGroup.getAlbumPhotos(); // 图片数据list
        albumPhoto = albumPhotos.get(index); // 当前图片的url
        tvCount.setText("" + (index + 1) + "/" + albumPhotos.size());
        fragments = new ArrayList<>();
        for (int i = 0; i < albumPhotos.size(); i++) {
            PhotoFragment fragment = new PhotoFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("albumPhoto", albumPhotos.get(i));
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        vpPictures.setAdapter(new PicturesViewPagerAdapter(getSupportFragmentManager(), fragments));
        vpPictures.setCurrentItem(index);
        // viewpager滑动事件监听
        vpPictures.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(final int position) {
                albumPhoto = albumPhotos.get(position); // 当前url
                tvCount.setText("" + (position + 1) + "/" + albumPhotos.size());
            }

            @Override
            public void onPageScrollStateChanged(final int state) {
            }
        });
        mShareListener = new CustomShareListener(this);
        /*增加自定义按钮的分享面板*/
        mShareAction = new ShareAction(this).setDisplayList(StyleUtil.getShareMedia(this, StyleUtil.TEXTANDIMAGE))
                .addButton(getString(R.string.umeng_sharebutton_copy), getString(R.string.umeng_sharebutton_copy), "umeng_socialize_copy", "umeng_socialize_copy")
                .addButton(getString(R.string.umeng_sharebutton_copyurl), getString(R.string.umeng_sharebutton_copyurl), "umeng_socialize_copyurl", "umeng_socialize_copyurl")
                .setShareboardclickCallback(new CustomShareBoardlistener());
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
     * 下载当前图片
     */
    private void downloadImg() {
        if (!PermissionUtil.getExternalStoragePermissions(this, REQUEST_CODE_EXTERNAL_STORAGE)) { // 没有权限
            return;
        }
        GlideApp.with(TgApplication.getContext()).asBitmap().load(albumPhoto).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(final Bitmap resource, final Transition<? super Bitmap> transition) {
                TgSDCardUtil.saveImageToGallery(PicturesDetailActivity.this, resource);
            }
        });
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
     * 分享item点击事件
     */
    private class CustomShareBoardlistener implements ShareBoardlistener {
        @Override
        public void onclick(final SnsPlatform snsPlatform, final SHARE_MEDIA shareMedia) {
            if (snsPlatform.mShowWord.equals(getString(R.string.umeng_sharebutton_copy))) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(ClipData.newPlainText(null, getString(R.string.activity_pictures_detail)));
                if (clipboardManager.hasPrimaryClip())
                    Toast.makeText(PicturesDetailActivity.this, getString(R.string.copy_success), Toast.LENGTH_SHORT).show();
            } else if (snsPlatform.mShowWord.equals(getString(R.string.umeng_sharebutton_copyurl))) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(ClipData.newPlainText(null, albumGroup.getDescription()));
                if (clipboardManager.hasPrimaryClip())
                    Toast.makeText(PicturesDetailActivity.this, getString(R.string.copyurl_success), Toast.LENGTH_SHORT).show();
            } else {
                UMImage image = new UMImage(PicturesDetailActivity.this, albumPhoto.getPhoto()); // 网络图片
                UMImage thumb = new UMImage(PicturesDetailActivity.this, R.drawable.ic_launcher);
                image.compressStyle = UMImage.CompressStyle.SCALE; // 大小压缩，默认为大小压缩，适合普通很大的图
                image.compressStyle = UMImage.CompressStyle.QUALITY; // 质量压缩，适合长图的分享
                // 压缩格式设置
                image.compressFormat = Bitmap.CompressFormat.PNG; // 用户分享透明背景的图片可以设置这种方式，但是qq好友，微信朋友圈，不支持透明背景图片，会变成黑色
                image.setThumb(thumb);
                new ShareAction(PicturesDetailActivity.this).withText(getString(R.string.activity_pictures_detail))
                        .setPlatform(shareMedia)
                        .withMedia(image)
                        .setCallback(mShareListener).share();
            }
        }
    }

    /**
     * 分享回调
     */
    private class CustomShareListener implements UMShareListener {

        private WeakReference<PicturesDetailActivity> mActivity;

        private CustomShareListener(final PicturesDetailActivity activity) {
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

    /**
     * OnClick
     */
    @OnClick({R.id.ivStart, R.id.fabDownload, R.id.ivEnd})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.ivStart: // 返回
                defaultFinish();
                break;
            case R.id.fabDownload: // 保存到图片
                downloadImg();
                break;
            case R.id.ivEnd: // 分享
                shareImg();
                break;
            default:
                break;
        }
    }
}
