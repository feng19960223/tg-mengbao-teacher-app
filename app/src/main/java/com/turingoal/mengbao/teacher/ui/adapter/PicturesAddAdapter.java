package com.turingoal.mengbao.teacher.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.turingoal.android.photopicker.PhotoPicker;
import com.turingoal.common.base.TgBaseViewHolder;
import com.turingoal.common.utils.GlideUtil;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.ui.activity.PicturesAddActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 发布图片item的adapter
 */
public class PicturesAddAdapter extends BaseItemDraggableAdapter<String, TgBaseViewHolder> {
    public static final String PICTURES_ADD = "pictures_add"; // 加号

    public PicturesAddAdapter() {
        super(R.layout.item_pictures_add, new ArrayList<String>());
        List<String> imgUrls = new ArrayList<>();
        imgUrls.add(PICTURES_ADD);
        setNewData(imgUrls);
    }

    @Override
    protected void convert(final TgBaseViewHolder helper, final String imgUrl) {
        if (PICTURES_ADD.equals(imgUrl)) {
            helper.setImageResource(R.id.ivPicturesItem, R.drawable.ic_add_pictures);
            helper.setGone(R.id.ivPicturesDelete, false);
        } else {
            GlideUtil.load(mContext, imgUrl, (ImageView) helper.getView(R.id.ivPicturesItem)); // 设置图片
            helper.setGone(R.id.ivPicturesDelete, true);
        }
        // 加号
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (PICTURES_ADD.equals(imgUrl)) {
                    List<String> strings = mData.subList(0, mData.size() - 1);
                    String[] paths = strings.size() == 0 ? null : strings.toArray(new String[strings.size()]); // 去重复
                    PhotoPicker.selectPics((PicturesAddActivity) mContext, paths, new PhotoPicker.PicCallBack() {
                        @Override
                        public void onPicSelected(final String[] strings) {
                            List<String> pathList = new ArrayList<>();
                            pathList.addAll(Arrays.asList(strings)); // UnsupportedOperationException
                            pathList.add(PICTURES_ADD);
                            setNewData(pathList);
                        }
                    });
                }
            }
        });
        // 删除
        helper.getView(R.id.ivPicturesDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                mData.remove(imgUrl);
                notifyDataSetChanged();
            }
        });
    }
}