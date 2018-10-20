package com.turingoal.mengbao.teacher.ui.fragment;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.request.PostRequest;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseFragment;
import com.turingoal.common.bean.TgResponseBean;
import com.turingoal.common.utils.GlideUtil;
import com.turingoal.common.utils.TgDialogUtil;
import com.turingoal.common.utils.TgHttpCallback;
import com.turingoal.common.utils.TgHttpUtil;
import com.turingoal.common.utils.TgJsonUtil;
import com.turingoal.mengbao.common.biz.domain.Child;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantUrls;
import com.turingoal.mengbao.teacher.ui.adapter.RankAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 排名
 */
public class RankFragment extends TgBaseFragment {
    @BindView(R.id.tvSwitchUpgrade)
    TextView tvSwitchUpgrade; // 经验总榜，默认选择
    @BindView(R.id.tvSwitchFlower)
    TextView tvSwitchFlower; // 红花总榜
    @BindView(R.id.ivAvatar1)
    ImageView ivAvatar1; // 第1名头像
    @BindView(R.id.tvFlowerCount1)
    TextView tvFlowerCount1; // 第1名总数
    @BindView(R.id.ivAvatar2)
    ImageView ivAvatar2; // 第2名头像
    @BindView(R.id.tvFlowerCount2)
    TextView tvFlowerCount2; // 第2名总数
    @BindView(R.id.ivAvatar3)
    ImageView ivAvatar3; // 第3名头像
    @BindView(R.id.tvFlowerCount3)
    TextView tvFlowerCount3; // 第3名总数
    @BindView(R.id.rvRank)
    RecyclerView rvRank; // 其他排名
    public static final int TYPE_UPGRADE = 1; // 经验总榜排行
    public static final int TYPE_FLOWER = 2; // 红花总榜排行
    public static int TYPE = TYPE_UPGRADE; // 默认本周
    private List<Child> mRankData = new ArrayList<>(); // 排名数据
    private RankAdapter mAdapter = null; // adapter
    private Child rank1; // 冠
    private Child rank2; // 亚
    private Child rank3; // 季

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_rank;
    }

    @Override
    protected void initialized() {
        initRecyclerAndAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        rankRequest();
    }

    /**
     * OnClick
     */
    @OnClick({R.id.btnSwitchUpgrade, R.id.btnSwitchFlower})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.btnSwitchUpgrade:
                if (TYPE != TYPE_UPGRADE) {
                    TYPE = TYPE_UPGRADE;
                    tvSwitchUpgrade.setEnabled(true);
                    tvSwitchFlower.setEnabled(false);
                    rankRequest();
                }
                break;
            case R.id.btnSwitchFlower:
                if (TYPE != TYPE_FLOWER) {
                    TYPE = TYPE_FLOWER;
                    tvSwitchUpgrade.setEnabled(false);
                    tvSwitchFlower.setEnabled(true);
                    rankRequest();
                }
                break;
            default:
                break;
        }
        Drawable typeDrawable = getResources().getDrawable(R.drawable.ic_upgrade); // 火箭图片
        if (TYPE == TYPE_FLOWER) {
            typeDrawable = getResources().getDrawable(R.drawable.ic_flower); // 花朵图片
        }
        typeDrawable.setBounds(0, 0, typeDrawable.getMinimumWidth(), typeDrawable.getMinimumHeight()); // 必须加上这一句，否则不显示图片
        tvFlowerCount1.setCompoundDrawables(typeDrawable, null, null, null);
        tvFlowerCount2.setCompoundDrawables(typeDrawable, null, null, null);
        tvFlowerCount3.setCompoundDrawables(typeDrawable, null, null, null);
    }

    /**
     * 初始化recyclerView和adapter
     */
    private void initRecyclerAndAdapter() {
        rvRank.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new RankAdapter();
        mAdapter.openLoadAnimation(); // 动画
        mAdapter.setEmptyView(getEmptyView((ViewGroup) rvRank.getParent()));
        rvRank.setAdapter(mAdapter);
    }

    /**
     * 排行数据网络请求,type = 1,请求经验总榜排行，type = 2,请求红花总榜排行
     */
    private void rankRequest() {
        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_RANK, getHttpTaskKey());
        request.params("classId", TgSystemHelper.getClassId());
        request.params("type", TYPE);
        request.execute(new TgHttpCallback(null) {
            @Override
            public void successHandler(final TgResponseBean result) {
                if (result.isSuccess()) {
                    if (result.getData() == null) {
                        return;
                    }
                    List<Child> users = TgJsonUtil.jsonResultBean2List(result, Child.class);
                    if (users != null) {
                        mRankData.clear();
                        mRankData.addAll(users);
                        dataSort(); // 排序
                        setRank123(); // 设置冠亚季
                        mAdapter.setNewData(mRankData);
                    }
                } else {
                    TgDialogUtil.showToast(result.getMsg()); // 登录失败，弹出错误信息
                }
            }
        });
    }

    /**
     * 给map按一定顺序排序
     */
    private void dataSort() {
        if (mRankData != null) {
            Collections.sort(mRankData, new Comparator<Child>() { // 排序规则
                @Override
                public int compare(final Child o1, final Child o2) {
                    return TYPE == TYPE_UPGRADE ? o2.getValueGrowth() - o1.getValueGrowth() : o2.getValueFlower() - o1.getValueFlower(); // 大的排前面
                }
            });
        }
    }

    /**
     * 设置冠亚季数据，从list中提取出来，并删除
     */
    private void setRank123() {
        if (mRankData != null && mRankData.size() > 3) {
            rank1 = mRankData.get(0);
            GlideUtil.loadImage(getContext(), rank1.getAvatar(), ivAvatar1);
            tvFlowerCount1.setText("x " + (TYPE == TYPE_UPGRADE ? rank1.getValueGrowth() : rank1.getValueFlower()));
            mRankData.remove(rank1);
            rank2 = mRankData.get(0);
            GlideUtil.loadImage(getContext(), rank2.getAvatar(), ivAvatar2);
            tvFlowerCount2.setText("x " + (TYPE == TYPE_UPGRADE ? rank2.getValueGrowth() : rank2.getValueFlower()));
            mRankData.remove(rank2);
            rank3 = mRankData.get(0);
            GlideUtil.loadImage(getContext(), rank3.getAvatar(), ivAvatar3);
            tvFlowerCount3.setText("x " + (TYPE == TYPE_UPGRADE ? rank3.getValueGrowth() : rank3.getValueFlower()));
            mRankData.remove(rank3);
        }
    }
}
