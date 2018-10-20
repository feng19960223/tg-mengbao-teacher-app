package com.turingoal.mengbao.teacher.ui.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.turingoal.common.base.TgBaseViewHolder;
import com.turingoal.common.utils.GlideUtil;
import com.turingoal.mengbao.common.biz.domain.Child;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.ui.fragment.RankFragment;

/**
 * 排行榜
 */

public class RankAdapter extends BaseQuickAdapter<Child, TgBaseViewHolder> {
    private int index = 4; // 从第几开始排序

    public RankAdapter() {
        super(R.layout.item_rank);
    }

    @Override
    protected void convert(final TgBaseViewHolder helper, final Child child) {
        helper.setText(R.id.tvBabyName, child.getRealname()) // 名字
                .setText(R.id.tvRank, String.format(mContext.getString(R.string.rank), mData.indexOf(child) + index)) // 第几名
                .setText(R.id.tvFlowerCount, "x " + child.getValueFlower()) // 花数量
                .setText(R.id.tvUpgradeCount, "x " + child.getValueGrowth()) // 火箭数量
                .setGone(R.id.tvFlowerCount, RankFragment.TYPE == RankFragment.TYPE_FLOWER)
                .setGone(R.id.tvUpgradeCount, RankFragment.TYPE == RankFragment.TYPE_UPGRADE);
        GlideUtil.loadImage(mContext, child.getAvatar(), (ImageView) helper.getView(R.id.ivAvatar)); // 头像
    }
}
