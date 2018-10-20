package com.turingoal.mengbao.teacher.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseViewHolder;
import com.turingoal.common.utils.GlideUtil;
import com.turingoal.mengbao.common.biz.domain.SchoolPhoto;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;

/**
 * 学校风采照片Adapter
 */

public class SchoolPhotoAdapter extends BaseQuickAdapter<SchoolPhoto, TgBaseViewHolder> {
    public SchoolPhotoAdapter() {
        super(R.layout.item_school_photo);
    }

    @Override
    protected void convert(final TgBaseViewHolder helper, final SchoolPhoto schoolPhoto) {
        helper.setText(R.id.tvDescription, schoolPhoto.getDiscription());
        GlideUtil.load(mContext, schoolPhoto.getPhoto(), (ImageView) helper.getView(R.id.ivPhoto)); // 设置图片
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TgSystemHelper.handleIntentWithObj(ConstantActivityPath.SCHOOL_PHOTO_DETAIL,"schoolPhoto",schoolPhoto);
            }
        });
    }
}
