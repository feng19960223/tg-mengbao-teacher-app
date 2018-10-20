package com.turingoal.mengbao.teacher.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.turingoal.common.base.TgBaseActivity;
import com.turingoal.mengbao.common.biz.domain.Comments;
import com.turingoal.mengbao.common.constants.TgBtsConstantYesNo;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 老师点评详情
 */
@Route(path = ConstantActivityPath.COMMENTS_DETAIL)
public class CommentsDetailActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvContent)
    TextView tvContent; // 老师点评内容
    @BindView(R.id.ivFlower)
    ImageView ivFlower;
    @BindView(R.id.tvDate)
    TextView tvDate; // 老师点评时间
    @BindView(R.id.tvReply)
    TextView tvReply; // 家长的回复內容
    @Autowired
    Comments comments; // 从list传递过来的item对象

    @Override
    protected int getLayoutID() {
        return R.layout.activity_comments_detail;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_comments_detail);
        tvName.setText(comments.getChildRealname());
        tvContent.setText(comments.getContent());
        ivFlower.setVisibility(TgBtsConstantYesNo.YES.equals(comments.getFlower()) ? View.VISIBLE : View.GONE);
        tvDate.setText(comments.getCreateTimeStr4DateTime());
        tvReply.setText(comments.getParentReply());
    }

    /**
     * OnClick
     */
    @OnClick(R.id.ivStart)
    public void onViewClicked() {
        defaultFinish();
    }
}
