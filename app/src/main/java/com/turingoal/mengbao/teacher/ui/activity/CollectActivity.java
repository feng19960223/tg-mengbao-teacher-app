package com.turingoal.mengbao.teacher.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.turingoal.common.base.TgBaseActivity;
import com.turingoal.common.constants.TgConstantGetDataType;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.bean.CollectBean;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;
import com.turingoal.mengbao.teacher.ui.adapter.CollectAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 收藏
 */
@Route(path = ConstantActivityPath.COLLECT)
public class CollectActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.searchView)
    SearchView searchView; // 搜索
    @BindView(R.id.rvCollect)
    RecyclerView rvCollect;
    private List<CollectBean> mCollectData = new ArrayList<>();
    private CollectAdapter mAdapter = new CollectAdapter(); // adapter

    @Override
    protected int getLayoutID() {
        return R.layout.activity_collect;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_collect);
        initRecyclerAndAdapter();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(final String query) {
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(final String newText) {
                if (!TextUtils.isEmpty(newText)) { // 有查询内容，显示符合查询的数据
                    mAdapter.setNewData(getDataByQuery(newText));
                } else { // 没有查询内容，显示全部
                    mAdapter.setNewData(mCollectData);
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(TgConstantGetDataType.INIT);
    }

    /**
     * 获取收藏数据
     */
    private void getData(final int getDataType) {
        // lastId正确的获得发，防止删除
        String lastId = getDataType == TgConstantGetDataType.INIT ? "-1" : mAdapter.getData().get(mAdapter.getData().size() - 1).getId();
        mAdapter.setNewData(mCollectData);
    }

    /**
     * 得到符合查询的数据
     */
    private List<CollectBean> getDataByQuery(final String query) {
        List<CollectBean> newData = new ArrayList<>();
        for (CollectBean collectBean : mCollectData) {
            if (collectBean.getTitle().contains(query)) {
                newData.add(collectBean);
            }
        }
        return newData;
    }

    /**
     * 初始化recyclerView和adapter
     */
    private void initRecyclerAndAdapter() {
        rvCollect.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() { // 加载更多
            @Override
            public void onLoadMoreRequested() {
                getData(TgConstantGetDataType.LOAD_MORE);
            }
        }, rvCollect);
        mAdapter.isFirstOnly(false); // 来回都要动画
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM); // 动画
        rvCollect.setAdapter(mAdapter);
        mAdapter.setEmptyView(getEmptyView((ViewGroup) rvCollect.getParent()));
    }

    /**
     * OnClick
     */
    @OnClick(R.id.ivStart)
    public void onViewClicked() {
        defaultFinish();
    }
}
