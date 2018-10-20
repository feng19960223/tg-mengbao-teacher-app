package com.turingoal.mengbao.teacher.ui.fragment;

import com.github.chrisbanes.photoview.PhotoView;
import com.turingoal.common.base.TgBaseFragment;
import com.turingoal.common.utils.GlideUtil;
import com.turingoal.mengbao.common.biz.domain.AlbumPhoto;
import com.turingoal.mengbao.teacher.R;

import butterknife.BindView;

/**
 * 班级图片fragment
 */
public class PhotoFragment extends TgBaseFragment {
    @BindView(R.id.ivPicturesDetail)
    PhotoView ivPicturesDetail; // 可以缩放图片

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_pictures_detail;
    }

    @Override
    protected void initialized() {
        AlbumPhoto albumPhoto = (AlbumPhoto) getArguments().getSerializable("albumPhoto"); // 图片
        GlideUtil.load(getContext(), albumPhoto.getPhoto(), ivPicturesDetail); // 不需要验证是不是空数据，空数据会显示加载错误图片
    }
}