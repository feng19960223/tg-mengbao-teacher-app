package com.turingoal.mengbao.teacher.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.lzy.okgo.request.PostRequest;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseActivity;
import com.turingoal.common.bean.TgResponseBean;
import com.turingoal.common.utils.GlideUtil;
import com.turingoal.common.utils.TgDialogUtil;
import com.turingoal.common.utils.TgHttpCallback;
import com.turingoal.common.utils.TgHttpUtil;
import com.turingoal.mengbao.common.biz.domain.HomeworkRecord;
import com.turingoal.mengbao.common.constants.TgBtsConstantYesNo;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;
import com.turingoal.mengbao.teacher.constants.ConstantUrls;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 已完成作业的送花
 */
@Route(path = ConstantActivityPath.HOMEWORK_FLOWER)
public class HomeworkFlowerActivity extends TgBaseActivity {
    @BindView(R.id.ivStart)
    ImageView ivStart;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvEnd)
    TextView tvEnd; // 送花
    @BindView(R.id.tvFlowerHint)
    TextView tvFlowerHint; // 老师剩余花朵提示
    @BindView(R.id.tvHomeworkTitle)
    TextView tvHomeworkTitle; // 作业标题
    @BindView(R.id.tvSource)
    TextView tvSource; // 作业来源
    @BindView(R.id.tvDate)
    TextView tvDate; // 作业时间
    @BindView(R.id.tvContent)
    TextView tvContent; // 作业内容
    @BindView(R.id.ivAvatar)
    ImageView ivAvatar; // 头像
    @BindView(R.id.tvName)
    TextView tvName; // 孩子名字
    @BindView(R.id.ivFlower)
    ImageView ivFlower; // 是否已经送过花了
    @BindView(R.id.tvScore)
    TextView tvScore; // 来源
    @BindView(R.id.tvAppraise)
    TextView tvAppraise; // 家长回复
    @Autowired
    HomeworkRecord homeworkRecord;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_homework_flower;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_homework_flower);
        tvEnd.setText(R.string.homework_flower);
        tvEnd.setVisibility(TgBtsConstantYesNo.NO.equals(homeworkRecord.getFlower()) ? View.VISIBLE : View.GONE); // 如果没送过花，肯定显示
        tvHomeworkTitle.setText(homeworkRecord.getHomeworkTitle());
        tvSource.setText(homeworkRecord.getUserRealname());
        tvDate.setText(homeworkRecord.getCreateTimeStr4DateTime());
        tvContent.setText(homeworkRecord.getHomeworkContent());
        GlideUtil.loadImage(this, homeworkRecord.getChildAvatar(), ivAvatar); // 设置图片
        tvName.setText(homeworkRecord.getChildRealname());
        ivFlower.setVisibility(TgBtsConstantYesNo.YES.equals(homeworkRecord.getFlower()) ? View.VISIBLE : View.GONE); // 如果已经送过花，显示小花图标
        tvScore.setText(getScore(homeworkRecord.getScore())); // 分数
        tvAppraise.setText(homeworkRecord.getContent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TgBtsConstantYesNo.NO.equals(homeworkRecord.getFlower())) {
            getData();
        }
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

    /**
     * 得到老师剩余花的数量
     */
    private void getData() {
        if (TgBtsConstantYesNo.NO.equals(homeworkRecord.getFlower())) { // 没送过花
            PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_HOMEWORK_RECORD_FLOWER_COUNT, getHttpTaskKey());
            request.params("userId", TgSystemHelper.getUserId());
            request.execute(new TgHttpCallback(this) {
                @Override
                public void successHandler(final TgResponseBean result) {
                    if (result.isSuccess()) {
                        if (result.getData() == null) {
                            return;
                        }
                        Map<String, Object> map = (Map<String, Object>) result.getData();
                        int flowerCount = (Integer) map.get("count");
                        if (flowerCount == 0) {
                            tvFlowerHint.setText(R.string.homework_flower_hint2);
                            tvEnd.setVisibility(View.GONE); // 没有花朵，则隐藏送花按钮
                        } else {
                            tvFlowerHint.setText(String.format(getString(R.string.homework_flower_hint3), flowerCount));
                        }
                    } else {
                        TgDialogUtil.showToast(result.getMsg()); // 弹出错误信息
                    }
                }
            });
        }
    }

    /**
     * 送花
     */
    private void request() {
        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_HOMEWORK_RECORD_FLOWER, getHttpTaskKey());
        request.params("id", homeworkRecord.getId());
        request.execute(new TgHttpCallback(this) {
            @Override
            public void successHandler(final TgResponseBean result) {
                if (result.isSuccess()) {
                    defaultFinish();
                } else {
                    TgDialogUtil.showToast(result.getMsg()); // 弹出错误信息
                }
            }
        });
    }

    /**
     * OnClick
     */
    @OnClick({R.id.ivStart, R.id.tvEnd})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.ivStart:
                defaultFinish();
                break;
            case R.id.tvEnd:
                request();
                break;
            default:
                break;
        }
    }
}
