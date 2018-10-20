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
import com.turingoal.mengbao.teacher.ui.adapter.AttendancePeopleAdapter;

import java.text.CollationKey;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 选择考勤人员
 */
@Route(path = ConstantActivityPath.ATTENDANCE_PEOPLE)
public class AttendancePeopleActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle; // title
    @BindView(R.id.searchView)
    SearchView searchView; // 搜索
    @BindView(R.id.rvAttendancePeople)
    RecyclerView rvAttendancePeople;
    private List<Child> childArrayList = new ArrayList<>(); // 宝宝数据
    private AttendancePeopleAdapter mAdapter = new AttendancePeopleAdapter(); // adapter

    @Override
    protected int getLayoutID() {
        return R.layout.activity_attendance_people;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_attendance_people);
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
                    mAdapter.setNewData(childArrayList);
                }
                return false;
            }
        });
        getData();
    }

    /**
     * 按名字abc排序规则
     */
    private void sort(final List<Child> children) {
        Collections.sort(children, new Comparator<Child>() { // 排序规则
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
        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_ATTENDANCE_CHILD, getHttpTaskKey());
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
                        sort(children);
                        childArrayList.addAll(children);
                        mAdapter.setNewData(children);
                    }
                } else {
                    TgDialogUtil.showToast(result.getMsg()); // 登录失败，弹出错误信息
                }
            }
        });
    }

    /**
     * 得到符合查询的数据
     */
    private List<Child> getDataByQuery(final String query) {
        List<Child> newData = new ArrayList<>();
        for (Child child : childArrayList) {
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
        rvAttendancePeople.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.isFirstOnly(false); // 来回都要动画
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM); // 动画
        rvAttendancePeople.setAdapter(mAdapter);
        mAdapter.setEmptyView(getEmptyView((ViewGroup) rvAttendancePeople.getParent()));
    }

    /**
     * OnClick
     */
    @OnClick(R.id.ivStart)
    public void onViewClicked() {
        defaultFinish();
    }
}
