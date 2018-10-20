package com.turingoal.mengbao.teacher.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.turingoal.common.base.TgBaseViewHolder;
import com.turingoal.common.utils.TgStringUtil;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.bean.CookbookBean;

/**
 * 食谱
 */

public class CookbookAdapter extends BaseQuickAdapter<CookbookBean, TgBaseViewHolder> {
    public CookbookAdapter() {
        super(R.layout.item_cookbook);
    }

    @Override
    protected void convert(final TgBaseViewHolder helper, final CookbookBean cookbookBean) {
        helper.setBackgroundRes(R.id.tvDay, getWeekColor(mData.indexOf(cookbookBean))) // 星期不同，颜色不同
                .setText(R.id.tvDay, getWeekString(mData.indexOf(cookbookBean))) // 星期几
                .setText(R.id.tvFood1, cookbookBean.getBreakfast()) // 如果有饭，显示饭，没饭，显示原因
                .setText(R.id.tvFood2, cookbookBean.getEarlyLunch())
                .setText(R.id.tvFood3, cookbookBean.getLunch())
                .setText(R.id.tvFood4, cookbookBean.getEarlyDinner())
                .setText(R.id.tvFood5, cookbookBean.getDinner())
                .setGone(R.id.llFood1, !TgStringUtil.isEmpty(cookbookBean.getBreakfast())) // 如果没有饭，则不显示
                .setGone(R.id.llFood2, !TgStringUtil.isEmpty(cookbookBean.getEarlyLunch()))
                .setGone(R.id.llFood3, !TgStringUtil.isEmpty(cookbookBean.getLunch()))
                .setGone(R.id.llFood4, !TgStringUtil.isEmpty(cookbookBean.getEarlyDinner()))
                .setGone(R.id.llFood5, !TgStringUtil.isEmpty(cookbookBean.getDinner()));
    }

    /**
     * 得到星期几
     */
    private String getWeekString(final int index) {
        String weekStr;
        switch (index) {
            case 1:
                weekStr = mContext.getString(R.string.monday);
                break;
            case 2:
                weekStr = mContext.getString(R.string.tuesday);
                break;
            case 3:
                weekStr = mContext.getString(R.string.wednesday);
                break;
            case 4:
                weekStr = mContext.getString(R.string.thursday);
                break;
            case 5:
                weekStr = mContext.getString(R.string.friday);
                break;
            case 6:
                weekStr = mContext.getString(R.string.saturday);
                break;
            case 7:
                weekStr = mContext.getString(R.string.sunday);
                break;
            default:
                weekStr = mContext.getString(R.string.sunday);
                break;

        }
        return weekStr;
    }

    /**
     * 不同星期，不同背景色
     */
    private int getWeekColor(final int index) {
        int color;
        switch (index) {
            case 1:
                color = R.color.monday;
                break;
            case 2:
                color = R.color.tuesday;
                break;
            case 3:
                color = R.color.wednesday;
                break;
            case 4:
                color = R.color.thursday;
                break;
            case 5:
                color = R.color.friday;
                break;
            case 6:
                color = R.color.saturday;
                break;
            case 7:
                color = R.color.sunday;
                break;
            default:
                color = R.color.sunday;
                break;

        }
        return color;
    }
}
