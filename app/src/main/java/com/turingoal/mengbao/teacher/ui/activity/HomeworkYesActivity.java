package com.turingoal.mengbao.teacher.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.promeg.pinyinhelper.Pinyin;
import com.lzy.okgo.request.PostRequest;
import com.turingoal.common.base.TgBaseActivity;
import com.turingoal.common.bean.TgResponseBean;
import com.turingoal.common.utils.TgDialogUtil;
import com.turingoal.common.utils.TgHttpCallback;
import com.turingoal.common.utils.TgHttpUtil;
import com.turingoal.common.utils.TgJsonUtil;
import com.turingoal.mengbao.common.biz.domain.Homework;
import com.turingoal.mengbao.common.biz.domain.HomeworkRecord;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;
import com.turingoal.mengbao.teacher.constants.ConstantUrls;
import com.turingoal.mengbao.teacher.ui.adapter.HomeworkYesAdapter;

import java.text.CollationKey;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 完成作业人员
 */
@Route(path = ConstantActivityPath.HOMEWORK_YES)
public class HomeworkYesActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.rvHomeworkPeople)
    RecyclerView rvHomeworkPeople;
    @Autowired
    Homework homework;
    private List<HomeworkRecord> mHomeworkRecordData = new ArrayList<>();
    private HomeworkYesAdapter mAdapter = new HomeworkYesAdapter();

    @Override
    protected int getLayoutID() {
        return R.layout.activity_homework_yes;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_homework_yes);
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
                    mAdapter.setNewData(mHomeworkRecordData);
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
    private void sort(final List<HomeworkRecord> homeworkRecords) {
        Collections.sort(homeworkRecords, new Comparator<HomeworkRecord>() { // 排序规则
            @Override
            public int compare(final HomeworkRecord o1, final HomeworkRecord o2) {
                CollationKey key1 = Collator.getInstance().getCollationKey(Pinyin.toPinyin(o1.getChildRealname(), "")); // 名字拼音
                CollationKey key2 = Collator.getInstance().getCollationKey(Pinyin.toPinyin(o2.getChildRealname(), ""));
                return key1.compareTo(key2);
            }
        });
    }

    /**
     * 加载完成作业数据
     */
    private void getData() {
        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_HOMEWORK_RECORD_YES, getHttpTaskKey());
        request.params("homeworkId", homework.getId());
        request.execute(new TgHttpCallback(this) {
            @Override
            public void successHandler(final TgResponseBean result) {
                if (result.isSuccess()) {
                    if (result.getData() == null) {
                        return;
                    }
                    List<HomeworkRecord> homeworkRecordList = TgJsonUtil.jsonResultBean2List(result, HomeworkRecord.class);
                    if (homeworkRecordList != null) {
                        mHomeworkRecordData.clear();
                        mHomeworkRecordData.addAll(homeworkRecordList);
                        sort(mHomeworkRecordData);
                        mAdapter.setNewData(mHomeworkRecordData);
                    }
                } else {
                    mAdapter.loadMoreFail(); // 获取更多数据失败，点击重试
                    TgDialogUtil.showToast(result.getMsg()); // 弹出错误信息
                }
            }
        });
    }

    /**
     * 得到符合查询的数据
     */
    private List<HomeworkRecord> getDataByQuery(final String query) {
        List<HomeworkRecord> newData = new ArrayList<>();
        for (HomeworkRecord homeworkRecord : mHomeworkRecordData) {
            // 名字或电话搜索
            if (homeworkRecord.getChildRealname().contains(query)) {
                newData.add(homeworkRecord);
            }
        }
        return newData;
    }

    /**
     * 初始化recyclerView和adapter
     */
    private void initRecyclerAndAdapter() {
        rvHomeworkPeople.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.isFirstOnly(false); // 来回都要动画
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM); // 动画
        rvHomeworkPeople.setAdapter(mAdapter);
        mAdapter.setEmptyView(getEmptyView((ViewGroup) rvHomeworkPeople.getParent()));
    }

    /**
     * OnClick
     */
    @OnClick(R.id.ivStart)
    public void onViewClicked() {
        defaultFinish();
    }
}
