package com.turingoal.mengbao.teacher.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
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
import com.turingoal.common.utils.TgStringUtil;
import com.turingoal.mengbao.common.constants.ConstantUserType;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.bean.AddressBookSectionBean;
import com.turingoal.mengbao.teacher.bean.User;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;
import com.turingoal.mengbao.teacher.constants.ConstantUrls;
import com.turingoal.mengbao.teacher.ui.adapter.AddressBookAdapter;
import com.turingoal.mengbao.teacher.ui.adapter.NavAdapter;

import java.text.CollationKey;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 通讯录
 */
@Route(path = ConstantActivityPath.ADDRESS_BOOK)
public class AddressBookActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.searchView)
    SearchView searchView; // 搜索
    @BindView(R.id.rvAddressBook)
    RecyclerView rvAddressBook;
    @BindView(R.id.rvNav)
    RecyclerView rvNav; // 索引栏
    private List<AddressBookSectionBean> mAddressBookData = new ArrayList<>(); // 通讯录数据
    private AddressBookAdapter mAdapter = new AddressBookAdapter(); // adapter
    private NavAdapter navAdapter = new NavAdapter(); // 索引栏adapter
    private Map<String, Integer> navMap = new HashMap<>(); // 索引值,有顺序

    @Override
    protected int getLayoutID() {
        return R.layout.activity_address_book;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_address_book);
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
                    mAdapter.setNewData(mAddressBookData);
                }
                return false;
            }
        });
        getData();
    }

    /**
     * 获取通讯录数据
     */
    private void getData() {
        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_ADDRESS_BOOK, getHttpTaskKey());
        request.params("schoolId", TgSystemHelper.getSchoolId());
        request.params("classId", TgSystemHelper.getClassId());
        request.execute(new TgHttpCallback(this) {
            @Override
            public void successHandler(final TgResponseBean result) {
                if (result.isSuccess()) {
                    if (result.getData() == null) {
                        return;
                    }
                    List<User> users = TgJsonUtil.jsonResultBean2List(result, User.class);
                    if (users != null) {
                        mAdapter.setNewData(getAddressBookData(users));
                    }
                } else {
                    TgDialogUtil.showToast(result.getMsg()); // 登录失败，弹出错误信息
                }
            }
        });
    }

    /**
     * 得到adapter可以分组完成的数据
     */
    private List<AddressBookSectionBean> getAddressBookData(final List<User> addressBookBeans) {
        List<User> schoolList = new ArrayList<>(); // 幼儿园
        List<User> teacherList = new ArrayList<>(); // 老师
        List<User> parentsList = new ArrayList<>(); // 家长
        // 1分 园长，老师，家长
        for (User addressBookBean : addressBookBeans) {
            if (TgStringUtil.isEmpty(addressBookBean.getRealname()) // 名字
                    || TgStringUtil.isEmpty(addressBookBean.getCellphoneNum())) { // 电话
                continue;
            }
            if (ConstantUserType.SCHOOL.equals(addressBookBean.getUserType())) {
                schoolList.add(addressBookBean);
            } else if (ConstantUserType.TEACHER.equals(addressBookBean.getUserType())) {
                teacherList.add(addressBookBean);
            } else {
                parentsList.add(addressBookBean);
            }
        }
        // 2排 园长拼音排序，老师拼音排序，家长拼音排序，
        sort(schoolList);
        sort(teacherList);
        sort(parentsList);
        // 3组 园长头，园长，老师头，老师，家长头，家长
        mAddressBookData.clear();
        int pos = 0;
        if (schoolList.size() > 0) { // 幼儿园
            mAddressBookData.add(new AddressBookSectionBean(true, ConstantUserType.getUserTypeStr(ConstantUserType.SCHOOL)));
            pos++;
            for (User user : schoolList) {
                pos++;
                mAddressBookData.add(new AddressBookSectionBean(user));
            }
        }
        if (teacherList.size() > 0) { // 老师
            mAddressBookData.add(new AddressBookSectionBean(true, ConstantUserType.getUserTypeStr(ConstantUserType.TEACHER)));
            pos++;
            for (User user : teacherList) {
                pos++;
                mAddressBookData.add(new AddressBookSectionBean(user));
            }
        }
        if (parentsList.size() > 0) { // 家长
            mAddressBookData.add(new AddressBookSectionBean(true, ConstantUserType.getUserTypeStr(ConstantUserType.PARENT)));
            pos++;
            navMap.clear();
            for (User user : parentsList) {
                String pyName = Pinyin.toPinyin(TgStringUtil.getStr(user.getRealname()), ""); // 得到名字拼音
                navMap.put(String.valueOf(pyName.toUpperCase().charAt(0)), pos); // 索引写值
                pos++;
                mAddressBookData.add(new AddressBookSectionBean(user));
            }
        }
        return mAddressBookData;
    }

    /**
     * 按名字abc排序规则
     */
    private void sort(final List<User> users) {
        Collections.sort(users, new Comparator<User>() { // 排序规则
            @Override
            public int compare(final User o1, final User o2) {
                CollationKey key1 = Collator.getInstance().getCollationKey(Pinyin.toPinyin(o1.getRealname(), "")); // 名字拼音
                CollationKey key2 = Collator.getInstance().getCollationKey(Pinyin.toPinyin(o2.getRealname(), ""));
                return key1.compareTo(key2);
            }
        });
    }

    /**
     * 得到符合查询的数据
     */
    private List<AddressBookSectionBean> getDataByQuery(final String query) {
        List<AddressBookSectionBean> newData = new ArrayList<>();
        for (AddressBookSectionBean addressBookBean : mAddressBookData) {
            if (addressBookBean.t == null) { // 如果是头布局
                continue; // 不显示头部
            }
            // 名字或电话搜索
            if (addressBookBean.t.getRealname().contains(query) || addressBookBean.t.getCellphoneNum().contains(query)) {
                newData.add(addressBookBean);
            }
        }
        int pos = 0;
        navMap.clear();
        for (AddressBookSectionBean addressBookBean : newData) {
            String pyName = Pinyin.toPinyin(TgStringUtil.getStr(addressBookBean.t.getRealname()), ""); // 得到名字拼音
            navMap.put(String.valueOf(pyName.toUpperCase().charAt(0)), pos);
            pos++;
        }
        return newData;
    }

    /**
     * 初始化recyclerView和adapter
     */
    private void initRecyclerAndAdapter() {
        rvAddressBook.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.isFirstOnly(false); // 来回都要动画
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM); // 动画
        rvAddressBook.setAdapter(mAdapter);
        mAdapter.setEmptyView(getEmptyView((ViewGroup) rvAddressBook.getParent()));
        rvNav.setLayoutManager(new LinearLayoutManager(this)); // 索引栏
        rvNav.setAdapter(navAdapter);
        // 点击索引位置，通讯录跳转到指定位置
        navAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(final BaseQuickAdapter adapter, final View view, final int position) {
                String str = (String) adapter.getItem(position);
                if (NavAdapter.TOP_INDEX.equals(str)) { // 最上面
                    rvAddressBook.scrollToPosition(0);
                    return;
                }
                if (navMap != null) { // 点击其他
                    if (navMap.containsKey(str)) {
                        rvAddressBook.smoothScrollToPosition(navMap.get(str));
                    }
                }
            }
        });
    }

    /**
     * OnClick
     */
    @OnClick(R.id.ivStart)
    public void onViewClicked() {
        defaultFinish();
    }
}
