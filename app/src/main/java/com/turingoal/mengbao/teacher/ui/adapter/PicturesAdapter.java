package com.turingoal.mengbao.teacher.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.request.PostRequest;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseViewHolder;
import com.turingoal.common.bean.TgResponseBean;
import com.turingoal.common.utils.TgDialogUtil;
import com.turingoal.common.utils.TgHttpCallback;
import com.turingoal.common.utils.TgHttpUtil;
import com.turingoal.mengbao.common.biz.domain.AlbumGroup;
import com.turingoal.mengbao.common.biz.domain.AlbumPhoto;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;
import com.turingoal.mengbao.teacher.constants.ConstantUrls;
import com.turingoal.mengbao.teacher.ui.activity.PicturesActivity;

/**
 * 照片分组
 */
public class PicturesAdapter extends BaseQuickAdapter<AlbumGroup, TgBaseViewHolder> {

    public PicturesAdapter() {
        super(R.layout.item_pictures);
    }

    @Override
    protected void convert(final TgBaseViewHolder helper, final AlbumGroup albumGroup) {
        helper.setText(R.id.tvSource, albumGroup.getDescription()) // 头
                .setText(R.id.tvDate, albumGroup.getCreateTimeStr4DateTime()); // 时间
        RecyclerView rvPicturesItem = helper.getView(R.id.rvPicturesItem);
        rvPicturesItem.setLayoutManager(new GridLayoutManager(mContext, 4));
        PicturesItemAdapter adapter = new PicturesItemAdapter(albumGroup.getAlbumPhotos());
        rvPicturesItem.setAdapter(adapter);
        // 点击查看
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(final BaseQuickAdapter adapter, final View view, final int position) {
                PicturesItemAdapter.ImgUrlIndex = position;
                TgSystemHelper.handleIntentWithObj(ConstantActivityPath.PICTURES_DETAIL, "albumGroup", albumGroup);
            }
        });
        // 长按删除
        adapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final BaseQuickAdapter adapter, final View view, final int position) {
                delete(adapter, position, albumGroup);
                return true;
            }
        });
    }

    /**
     * 刪除
     */
    private void delete(final BaseQuickAdapter adapter, final int position, final AlbumGroup albumGroup) {
        new MaterialDialog.Builder(mContext)
                .title(R.string.item_fun_delete)
                .content(R.string.pictures_delete_hint)
                .positiveText(R.string.yes)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull final MaterialDialog dialog, @NonNull final DialogAction which) {
                        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_ALBUM_PHOTO_DELETE, ((PicturesActivity) mContext).getHttpTaskKey());
                        request.params("photoId", ((AlbumPhoto) adapter.getData().get(position)).getId());
                        request.params("groupId", albumGroup.getId());
                        request.execute(new TgHttpCallback(mContext) {
                            @Override
                            public void successHandler(final TgResponseBean result) {
                                if (result.isSuccess()) {
                                    adapter.getData().remove(position);
                                    if (adapter.getItemCount() == 0) { // 如果是最后一张图片，删除title
                                        mData.remove(albumGroup); // 如果是最后一张图片，删除title
                                        notifyDataSetChanged();
                                    }
                                    adapter.notifyDataSetChanged();
                                } else {
                                    TgDialogUtil.showToast(result.getMsg()); // 弹出错误信息
                                }
                            }
                        });
                    }
                }).show();
    }
}
