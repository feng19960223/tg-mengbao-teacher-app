package com.turingoal.mengbao.teacher.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.promeg.pinyinhelper.Pinyin;
import com.lzy.okgo.request.PostRequest;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseActivity;
import com.turingoal.common.bean.TgResponseBean;
import com.turingoal.common.utils.TgDialogUtil;
import com.turingoal.common.utils.TgHttpCallback;
import com.turingoal.common.utils.TgHttpUtil;
import com.turingoal.common.utils.TgJsonUtil;
import com.turingoal.mengbao.common.biz.domain.Child;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;
import com.turingoal.mengbao.teacher.constants.ConstantUrls;
import com.turingoal.mengbao.teacher.ui.adapter.CommentsPeopleAdapter;

import java.text.CollationKey;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 班级点评人员
 */
@Route(path = ConstantActivityPath.COMMENTS_PEOPLE)
public class CommentsPeopleActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.searchView)
    SearchView searchView; // 搜索
    @BindView(R.id.rvCommentsPeople)
    RecyclerView rvCommentsPeople;
    private List<Child> mChildData = new ArrayList<>();
    private CommentsPeopleAdapter mAdapter = new CommentsPeopleAdapter();

    @Override
    protected int getLayoutID() {
        return R.layout.activity_comments_people;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_comments_people);
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
                    mAdapter.setNewData(getData(newText));
                } else { // 没有查询内容，显示全部
                    mAdapter.setNewData(mChildData);
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    /**
     * 按名字abc排序规则
     */
    private void sort(final List<Child> babyBeans) {
        Collections.sort(babyBeans, new Comparator<Child>() { // 排序规则
            @Override
            public int compare(final Child o1, final Child o2) {
                CollationKey key1 = Collator.getInstance().getCollationKey(Pinyin.toPinyin(o1.getRealname(), "")); // 名字拼音
                CollationKey key2 = Collator.getInstance().getCollationKey(Pinyin.toPinyin(o2.getRealname(), ""));
                return key1.compareTo(key2);
            }
        });
    }

    /**
     * 得到宝宝人员数据
     */
    private void getData() {
        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_COMMENTS_CHILD, getHttpTaskKey());
        request.params("classId", TgSystemHelper.getClassId());
        request.execute(new TgHttpCallback(this) {
            @Override
            public void successHandler(final TgResponseBean result) {
                if (result.isSuccess()) {
                    if (result.getData() == null) {
                        return;
                    }
                    List<Child> children = TgJsonUtil.jsonResultBean2List(result, Child.class);
                    if (children != null) {
                        mChildData.clear();
                        mChildData.addAll(children);
                        sort(mChildData);
                        mAdapter.setNewData(mChildData);
                    }
                } else {
                    TgDialogUtil.showToast(result.getMsg()); // 弹出错误信息
                }
            }
        });
    }

    /**
     * 得到符合查询的数据
     */
    private List<Child> getData(final String query) {
        List<Child> newData = new ArrayList<>();
        for (Child child : mChildData) {
            // 名字或电话搜索
            if (child.getRealname().contains(query)) {
                newData.add(child);
            }
        }
        return newData;
    }

    /**
     * 初始化recyclerView和adapter
     */
    private void initRecyclerAndAdapter() {
        rvCommentsPeople.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.isFirstOnly(false); // 来回都要动画
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM); // 动画
        rvCommentsPeople.setAdapter(mAdapter);
        mAdapter.setEmptyView(getEmptyView((ViewGroup) rvCommentsPeople.getParent()));
    }

    /**
     * OnClick
     */
    @OnClick(R.id.ivStart)
    public void onViewClicked() {
        defaultFinish();
    }
}
