package com.turingoal.mengbao.teacher.ui.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseViewHolder;
import com.turingoal.common.utils.TgStringUtil;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.bean.FunItemBean;
import com.turingoal.mengbao.teacher.event.EventConsts;
import com.turingoal.mengbao.teacher.event.EventLogger;

/**
 * 主菜单功能adapter
 */

public class MainFunAdapter extends BaseQuickAdapter<FunItemBean, TgBaseViewHolder> {
    public MainFunAdapter() {
        super(R.layout.item_fun);
    }

    @Override
    protected void convert(final TgBaseViewHolder helper, final FunItemBean funItemBean) {
        helper.setImageResource(R.id.ivImage, funItemBean.getResId()) // 图片
                .setText(R.id.tvTitle, funItemBean.getTitle()) // title
                .setText(R.id.tvCount, funItemBean.getCount()) // 数量
                .setVisible(R.id.tvCount, !TgStringUtil.isEmpty(funItemBean.getCount())); // 是否显示消息数量
        // item点击事件
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventLogger.logEvent(EventConsts.e_Id_Fun_Path, EventConsts.e_Key_Fun_Path, funItemBean.getPath()); // 页面统计
                TgSystemHelper.handleIntent(funItemBean.getPath());
            }
        });
    }
}
