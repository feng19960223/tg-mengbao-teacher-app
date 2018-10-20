package com.turingoal.mengbao.teacher.ui.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.turingoal.common.base.TgBaseViewHolder;
import com.turingoal.common.utils.GlideUtil;
import com.turingoal.mengbao.common.biz.domain.AlbumPhoto;
import com.turingoal.mengbao.teacher.R;

import java.util.List;

/**
 * 图片item的adapter
 */
public class PicturesItemAdapter extends BaseQuickAdapter<AlbumPhoto, TgBaseViewHolder> {
    public static int ImgUrlIndex = 0; // 用来记录点击的是第几张图片

    public PicturesItemAdapter(final List<AlbumPhoto> data) {
        super(R.layout.item_pictures_item, data);
    }

    @Override
    protected void convert(final TgBaseViewHolder helper, final AlbumPhoto albumPhoto) {
        GlideUtil.load(mContext, albumPhoto.getPhoto(), (ImageView) helper.getView(R.id.ivPicturesItem)); // 设置图片
    }
}
