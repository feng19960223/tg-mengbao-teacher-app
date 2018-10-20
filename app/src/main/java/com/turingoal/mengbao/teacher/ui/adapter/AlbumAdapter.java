package com.turingoal.mengbao.teacher.ui.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.request.PostRequest;
import com.turingoal.android.photopicker.PhotoPicker;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseViewHolder;
import com.turingoal.common.bean.TgResponseBean;
import com.turingoal.common.utils.GlideUtil;
import com.turingoal.common.utils.TgDialogUtil;
import com.turingoal.common.utils.TgHttpCallback;
import com.turingoal.common.utils.TgHttpUtil;
import com.turingoal.mengbao.common.biz.domain.Album;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;
import com.turingoal.mengbao.teacher.constants.ConstantUrls;
import com.turingoal.mengbao.teacher.event.EventConsts;
import com.turingoal.mengbao.teacher.event.EventLogger;
import com.turingoal.mengbao.teacher.ui.activity.AlbumActivity;

import java.io.File;

/**
 * 相册adapter
 */
public class AlbumAdapter extends BaseQuickAdapter<Album, TgBaseViewHolder> {
    private static final int COUNT_MAX_TITLE = 10;
    private static final int COUNT_MAX_DESCRIPTION = 40;

    public AlbumAdapter() {
        super(R.layout.item_album);
    }

    @Override
    protected void convert(final TgBaseViewHolder helper, final Album album) {
        helper.setText(R.id.tvTitle, album.getTitle())
                .setText(R.id.tvDescription, album.getDescription())
                .setText(R.id.tvSize, "" + album.getPhotoSize());
        GlideUtil.load(mContext, album.getCoverImage(), (ImageView) helper.getView(R.id.ivCoverImage)); // 设置图片
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                TgSystemHelper.handleIntentWithObj(ConstantActivityPath.PICTURES, "album", album); // 亲子作业详情页面
            }
        });
        helper.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showFunDialog(album);
                return true;
            }
        });
    }

    /**
     * 功能选择
     */
    private void showFunDialog(final Album album) {
        new MaterialDialog.Builder(mContext)
                .title(album.getTitle())
                .titleGravity(GravityEnum.CENTER)
                .items(R.array.fun)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(final MaterialDialog dialog, final View itemView, final int position, final CharSequence text) {
                        if (text.equals(mContext.getString(R.string.item_fun_delete))) { // 删除
                            delete(album);
                        } else if (text.equals(mContext.getString(R.string.item_fun_title_rename))) { // 标题重命名
                            showTitleRenameDialog(album);
                        } else if (text.equals(mContext.getString(R.string.item_fun_description_rename))) { // 描述编辑
                            showDescriptionRenameDialog(album);
                        } else if (text.equals(mContext.getString(R.string.item_fun_cover_edit))) { // 修改封面
                            setCover(album);
                        }
                    }
                }).show();
    }

    /**
     * 刪除
     */
    private void delete(final Album album) {
        new MaterialDialog.Builder(mContext)
                .title(R.string.item_fun_delete)
                .content(R.string.album_delete_hint)
                .positiveText(R.string.yes)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull final MaterialDialog dialog, @NonNull final DialogAction which) {
                        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_ALBUM_DELETE, ((AlbumActivity) mContext).getHttpTaskKey());
                        request.params("id", album.getId());
                        request.execute(new TgHttpCallback(mContext) {
                            @Override
                            public void successHandler(final TgResponseBean result) {
                                if (result.isSuccess()) {
                                    mData.remove(album);
                                    notifyDataSetChanged();
                                } else {
                                    TgDialogUtil.showToast(result.getMsg()); // 弹出错误信息
                                }
                            }
                        });
                    }
                }).show();
    }


    /**
     * 标题重命名
     */
    private void showTitleRenameDialog(final Album album) {
        new MaterialDialog.Builder(mContext)
                .title(R.string.album_title_rename)
                .titleGravity(GravityEnum.CENTER)
                .positiveText(R.string.yes)
                .negativeText(R.string.cancel)
                .inputRange(1, COUNT_MAX_TITLE) // 1-10个字
                //前2个一个是hint一个是预输入的文字
                .input(mContext.getString(R.string.album_title_input), album.getTitle(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull final MaterialDialog dialog, final CharSequence input) {
                        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_ALBUM_TITLE_RENAME, ((AlbumActivity) mContext).getHttpTaskKey());
                        request.params("id", album.getId());
                        request.params("title", input.toString());
                        request.execute(new TgHttpCallback(mContext) {
                            @Override
                            public void successHandler(final TgResponseBean result) {
                                if (result.isSuccess()) {
                                    album.setTitle(input.toString());
                                    notifyDataSetChanged();
                                } else {
                                    TgDialogUtil.showToast(result.getMsg()); // 弹出错误信息
                                }
                            }
                        });
                    }
                }).show();
    }

    /**
     * 描述编辑
     */
    private void showDescriptionRenameDialog(final Album album) {
        new MaterialDialog.Builder(mContext)
                .title(R.string.album_description_rename)
                .titleGravity(GravityEnum.CENTER)
                .positiveText(R.string.yes)
                .negativeText(R.string.cancel)
                .inputRange(1, COUNT_MAX_DESCRIPTION) // 1-40个字
                //前2个一个是hint一个是预输入的文字
                .input(mContext.getString(R.string.album_description_input), album.getDescription(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull final MaterialDialog dialog, final CharSequence input) {
                        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_ALBUM_DESCRIPTION_RENAME, ((AlbumActivity) mContext).getHttpTaskKey());
                        request.params("id", album.getId());
                        request.params("description", input.toString());
                        request.execute(new TgHttpCallback(mContext) {
                            @Override
                            public void successHandler(final TgResponseBean result) {
                                if (result.isSuccess()) {
                                    album.setDescription(input.toString());
                                    notifyDataSetChanged();
                                } else {
                                    TgDialogUtil.showToast(result.getMsg()); // 弹出错误信息
                                }
                            }
                        });
                    }
                }).show();
    }

    /**
     * 修改封面
     */
    private void setCover(final Album album) {
        PhotoPicker.selectPic((AlbumActivity) mContext, 600, new PhotoPicker.PicCallBack() {
            @Override
            public void onPicSelected(final String[] strings) {
                EventLogger.logEvent(EventConsts.e_Id_Album);
                PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_ALBUM_COVER, ((AlbumActivity) mContext).getHttpTaskKey());
                request.params("id", album.getId());
                request.params("coverImage", new File(strings[0]));
                request.execute(new TgHttpCallback(mContext) {
                    @Override
                    public void successHandler(final TgResponseBean result) {
                        if (result.isSuccess()) {
                            album.setCoverImage(strings[0]);
                            notifyDataSetChanged();
                        } else {
                            TgDialogUtil.showToast(result.getMsg()); // 弹出错误信息
                        }
                    }
                });
            }
        });
    }
}
