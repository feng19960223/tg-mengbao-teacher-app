package com.turingoal.mengbao.teacher.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseViewHolder;
import com.turingoal.common.utils.GlideUtil;
import com.turingoal.common.utils.TgStringUtil;
import com.turingoal.mengbao.common.biz.domain.HomeworkRecord;
import com.turingoal.mengbao.common.constants.TgBtsConstantYesNo;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;

/**
 * 亲子作业adapter
 */
public class HomeworkYesAdapter extends BaseQuickAdapter<HomeworkRecord, TgBaseViewHolder> {
    public HomeworkYesAdapter() {
        super(R.layout.item_homework_yes);
    }

    @Override
    protected void convert(final TgBaseViewHolder helper, final HomeworkRecord homeworkRecord) {
        helper.setText(R.id.tvName, homeworkRecord.getChildRealname())
                .setGone(R.id.ivFlower, TgBtsConstantYesNo.YES.equals(homeworkRecord.getFlower()))
                .setText(R.id.tvScore, getScore(homeworkRecord.getScore()))
                .setText(R.id.tvAppraise, TgStringUtil.getSelectStr(homeworkRecord.getContent(), mContext.getString(R.string.homework_no_appraise)));
        GlideUtil.loadImage(mContext, homeworkRecord.getChildAvatar(), (ImageView) helper.getView(R.id.ivAvatar)); // 设置图片
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                TgSystemHelper.handleIntentWithObj(ConstantActivityPath.HOMEWORK_FLOWER, "homeworkRecord", homeworkRecord);
            }
        });
    }

    /**
     * 分数转化
     */
    private String getScore(final int score) {
        String scoreStr;
        switch (score) {
            case 1:
                scoreStr = "0.5";
                break;
            case 2:
                scoreStr = "1.0";
                break;
            case 3:
                scoreStr = "1.5";
                break;
            case 4:
                scoreStr = "2.0";
                break;
            case 5:
                scoreStr = "2.5";
                break;
            case 6:
                scoreStr = "3.0";
                break;
            case 7:
                scoreStr = "3.5";
                break;
            case 8:
                scoreStr = "4.0";
                break;
            case 9:
                scoreStr = "4.5";
                break;
            case 10:
                scoreStr = "5.0";
                break;
            default:
                scoreStr = "0.0";
                break;
        }
        return scoreStr;
    }
}
