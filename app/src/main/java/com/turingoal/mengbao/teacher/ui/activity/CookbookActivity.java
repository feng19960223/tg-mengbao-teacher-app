package com.turingoal.mengbao.teacher.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.request.PostRequest;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseActivity;
import com.turingoal.common.bean.TgResponseBean;
import com.turingoal.common.utils.TgDateUtil;
import com.turingoal.common.utils.TgDialogUtil;
import com.turingoal.common.utils.TgHttpCallback;
import com.turingoal.common.utils.TgHttpUtil;
import com.turingoal.common.utils.TgJsonUtil;
import com.turingoal.mengbao.common.biz.domain.Cookbook;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.bean.CookbookBean;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;
import com.turingoal.mengbao.teacher.constants.ConstantUrls;
import com.turingoal.mengbao.teacher.ui.adapter.CookbookAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 食谱
 */
@Route(path = ConstantActivityPath.COOKBOOK)
public class CookbookActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvDay)
    TextView tvDay; // 食谱表作用时间
    @BindView(R.id.rvCookbook)
    RecyclerView rvCookbook;
    private CookbookAdapter mAdapter = new CookbookAdapter();

    @Override
    protected int getLayoutID() {
        return R.layout.activity_cookbook;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_cookbook);
        tvDay.setText("" + TgDateUtil.date2String(TgDateUtil.getWeekFirst().getTime(), TgDateUtil.FORMAT_YYYY_MM_DD_ZH)
                + "-" + TgDateUtil.date2String(TgDateUtil.getWeekLast().getTime(), TgDateUtil.FORMAT_YYYY_MM_DD_ZH));
        initRecyclerAndAdapter();
        getData();
    }

    /**
     * 初始化recyclerView和adapter
     */
    private void initRecyclerAndAdapter() {
        rvCookbook.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.isFirstOnly(false); // 来回都要动画
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM); // 动画
        rvCookbook.setAdapter(mAdapter);
        mAdapter.setEmptyView(getEmptyView((ViewGroup) rvCookbook.getParent()));
    }

    /**
     * 食谱数据网络请求
     */
    private void getData() {
        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_COOKBOOK, getHttpTaskKey());
        request.params("schoolId", TgSystemHelper.getSchoolId());
        request.execute(new TgHttpCallback(this) {
            @Override
            public void successHandler(final TgResponseBean result) {
                if (result.isSuccess()) {
                    if (result.getData() == null) {
                        return;
                    }
                    Cookbook cookbook = TgJsonUtil.jsonResultBean2Object(result, Cookbook.class);
                    if (cookbook != null) {
                        List<CookbookBean> cookbookBeanList = new ArrayList<>();
                        cookbookBeanList.add(new CookbookBean(1, cookbook.getBreakfast1(), cookbook.getEarlyLunch1(), cookbook.getLunch1(), cookbook.getEarlyDinner1(), cookbook.getDinner1()));
                        cookbookBeanList.add(new CookbookBean(2, cookbook.getBreakfast2(), cookbook.getEarlyLunch2(), cookbook.getLunch2(), cookbook.getEarlyDinner2(), cookbook.getDinner2()));
                        cookbookBeanList.add(new CookbookBean(3, cookbook.getBreakfast3(), cookbook.getEarlyLunch3(), cookbook.getLunch3(), cookbook.getEarlyDinner3(), cookbook.getDinner3()));
                        cookbookBeanList.add(new CookbookBean(4, cookbook.getBreakfast4(), cookbook.getEarlyLunch4(), cookbook.getLunch4(), cookbook.getEarlyDinner4(), cookbook.getDinner4()));
                        cookbookBeanList.add(new CookbookBean(5, cookbook.getBreakfast5(), cookbook.getEarlyLunch5(), cookbook.getLunch5(), cookbook.getEarlyDinner5(), cookbook.getDinner5()));
                        cookbookBeanList.add(new CookbookBean(6, cookbook.getBreakfast6(), cookbook.getEarlyLunch6(), cookbook.getLunch6(), cookbook.getEarlyDinner6(), cookbook.getDinner6()));
                        cookbookBeanList.add(new CookbookBean(7, cookbook.getBreakfast7(), cookbook.getEarlyLunch7(), cookbook.getLunch7(), cookbook.getEarlyDinner7(), cookbook.getDinner7()));
                        mAdapter.setNewData(cookbookBeanList);
                        scrollToPosition();
                    }
                } else {
                    TgDialogUtil.showToast(result.getMsg()); // 登录失败，弹出错误信息
                }
            }
        });
    }

    /**
     * 将RecyclerView移动到指定位置
     */
    private void scrollToPosition() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); // 今天星期几
        if (dayOfWeek == 1) { // 1是星期日
            rvCookbook.scrollToPosition(6); // 7(一周七天)-1
        } else {
            rvCookbook.scrollToPosition(dayOfWeek - 2);
        }
    }

    /**
     * OnClick
     */
    @OnClick(R.id.ivStart)
    public void onViewClicked() {
        defaultFinish();
    }
}
