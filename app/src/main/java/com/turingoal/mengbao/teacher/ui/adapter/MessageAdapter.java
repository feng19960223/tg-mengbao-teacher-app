package com.turingoal.mengbao.teacher.ui.adapter;

import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.request.PostRequest;
import com.turingoal.common.base.TgBaseViewHolder;
import com.turingoal.common.bean.TgResponseBean;
import com.turingoal.common.utils.TgDialogUtil;
import com.turingoal.common.utils.TgHttpCallback;
import com.turingoal.common.utils.TgHttpUtil;
import com.turingoal.mengbao.common.biz.domain.Message;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantUrls;
import com.turingoal.mengbao.teacher.ui.activity.MessageActivity;

/**
 * 通知adapter
 */
public class MessageAdapter extends BaseQuickAdapter<Message, TgBaseViewHolder> {
    public MessageAdapter() {
        super(R.layout.item_message);
    }

    @Override
    protected void convert(final TgBaseViewHolder helper, final Message message) {
        helper.setText(R.id.tvTitle, message.getType())
                .setText(R.id.tvContent, message.getContent())
                .setText(R.id.tvDate, message.getCreateTimeStr4DateTime());
        // 长按删除
        helper.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                delete(message);
                return true;
            }
        });
    }

    /**
     * 刪除
     */
    private void delete(final Message message) {
        new MaterialDialog.Builder(mContext)
                .title(R.string.item_fun_delete)
                .content(R.string.message_delete_hint)
                .positiveText(R.string.yes)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull final MaterialDialog dialog, @NonNull final DialogAction which) {
                        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_MESSAGE_DELETE, ((MessageActivity) mContext).getHttpTaskKey());
                        request.params("id", message.getId());
                        request.execute(new TgHttpCallback(mContext) {
                            @Override
                            public void successHandler(final TgResponseBean result) {
                                if (result.isSuccess()) {
                                    mData.remove(message);
                                    notifyDataSetChanged();
                                } else {
                                    TgDialogUtil.showToast(result.getMsg()); // 弹出错误信息
                                }
                            }
                        });
                    }
                }).show();
    }
}
