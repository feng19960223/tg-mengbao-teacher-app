package com.turingoal.mengbao.teacher.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.turingoal.common.base.TgBaseViewHolder;
import com.turingoal.common.utils.GlideUtil;
import com.turingoal.common.utils.TgSystemUtil;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.bean.AddressBookSectionBean;
import com.turingoal.mengbao.teacher.bean.User;

/**
 * 通讯录分组
 */
public class AddressBookAdapter extends BaseSectionQuickAdapter<AddressBookSectionBean, TgBaseViewHolder> {

    public AddressBookAdapter() {
        super(R.layout.item_address_book, R.layout.item_address_book_head, null);
    }

    @Override
    protected void convertHead(final TgBaseViewHolder helper, final AddressBookSectionBean item) {
        helper.setText(R.id.tvHeader, item.header); // 头
    }

    @Override
    protected void convert(final TgBaseViewHolder helper, final AddressBookSectionBean item) {
        final User user = item.t; // 内容
        helper.setText(R.id.tvName, user.getRealname()); // 名字
        GlideUtil.loadImage(mContext, user.getAvatar(), (ImageView) helper.getView(R.id.ivAvatar)); // 头像
        helper.getView(R.id.ivCall).setOnClickListener(new View.OnClickListener() { // 打电话
            @Override
            public void onClick(final View view) {
                TgSystemUtil.call(user.getCellphoneNum());
            }
        });
        helper.getView(R.id.ivSms).setOnClickListener(new View.OnClickListener() { // 发短信
            @Override
            public void onClick(final View view) {
                TgSystemUtil.sms(user.getCellphoneNum());
            }
        });
    }
}
