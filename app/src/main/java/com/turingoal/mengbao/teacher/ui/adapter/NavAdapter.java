package com.turingoal.mengbao.teacher.ui.adapter;

import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.turingoal.common.app.TgApplication;
import com.turingoal.common.base.TgBaseViewHolder;
import com.turingoal.mengbao.teacher.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 侧滑索引A-Z
 */
public class NavAdapter extends BaseQuickAdapter<String, TgBaseViewHolder> {
    public static final String TOP_INDEX = "#"; // 特殊位置，点击会到最上面

    public NavAdapter() {
        super(R.layout.item_nav);
        List<String> strings = new ArrayList<>(); // 设置数据
        strings.add(TOP_INDEX);
        for (int i = 'A'; i <= 'Z'; i++) {
            strings.add("" + (char) i);
        }
        setNewData(strings);
    }

    @Override
    protected void convert(final TgBaseViewHolder helper, final String str) {
        int height = (TgApplication.getScreenHeight() - BarUtils.getActionBarHeight()) / (mData.size() + 1);
        ((TextView) helper.getView(R.id.tvNav)).setHeight(height); // 设置高度
        helper.setText(R.id.tvNav, str).addOnClickListener(R.id.tvNav); // 绑定字符串
    }
}
