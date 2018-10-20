package com.turingoal.mengbao.teacher.ui.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.request.PostRequest;
import com.turingoal.common.app.TgApplication;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseFragment;
import com.turingoal.common.bean.TgResponseBean;
import com.turingoal.common.utils.TgDialogUtil;
import com.turingoal.common.utils.TgHttpCallback;
import com.turingoal.common.utils.TgHttpUtil;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.bean.FunItemBean;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;
import com.turingoal.mengbao.teacher.constants.ConstantUrls;
import com.turingoal.mengbao.teacher.ui.adapter.MainFunAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 主页
 */

public class MainFragment extends TgBaseFragment {
    @BindView(R.id.ivStart)
    ImageView ivStart;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.rvFun)
    RecyclerView rvFun; // 主菜单
    private MainFunAdapter mAdapter = new MainFunAdapter(); // adapter
    private int spanCount = 3; // 瀑布流列数,功能列数
    private List<FunItemBean> funItemBeanList = new ArrayList<>(); // 功能菜单数据list
    private final int index = 2; // 显示通知数的item

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initialized() {
        ivStart.setVisibility(View.GONE);
        tvTitle.setText(R.string.app_name);
        initRecyclerAndAdapter();
        initData();
    }

    /**
     * 初始化菜单数据
     */
    private void initData() {
        funItemBeanList.add(new FunItemBean(R.drawable.ic_courses, getString(R.string.fun_courses), ConstantActivityPath.COURSES)); // 课表
        funItemBeanList.add(new FunItemBean(R.drawable.ic_cookbook, getString(R.string.fun_cookbook), ConstantActivityPath.COOKBOOK)); // 食谱
        funItemBeanList.add(new FunItemBean(R.drawable.ic_address_book, getString(R.string.fun_address_book), ConstantActivityPath.ADDRESS_BOOK)); // 通讯录
        funItemBeanList.add(new FunItemBean(R.drawable.ic_attendance, getString(R.string.fun_attendance), ConstantActivityPath.ATTENDANCE)); // 考勤
        funItemBeanList.add(new FunItemBean(R.drawable.ic_class_pictures, getString(R.string.fun_class_pictures), ConstantActivityPath.ALBUM)); // 相册
        funItemBeanList.add(new FunItemBean(R.drawable.ic_homework, getString(R.string.fun_homework), ConstantActivityPath.HOMEWORK)); // 亲子作业
        funItemBeanList.add(new FunItemBean(R.drawable.ic_comments, getString(R.string.fun_comments), ConstantActivityPath.COMMENTS_PEOPLE)); // 点评
        funItemBeanList.add(new FunItemBean(R.drawable.ic_school_photo, getString(R.string.fun_school_photo), ConstantActivityPath.SCHOOL_PHOTO)); // 校园风采
        funItemBeanList.add(new FunItemBean(R.drawable.ic_love_remark, getString(R.string.fun_love_remark), ConstantActivityPath.LOVE_REMARK)); // 爱心备注
        funItemBeanList.add(new FunItemBean(R.drawable.ic_message, getString(R.string.fun_message), ConstantActivityPath.MESSAGE)); // 消息中心
        FunItemBean funItemBean = new FunItemBean(R.drawable.ic_inform_item, getString(R.string.fun_inform), ConstantActivityPath.INFORM);
        funItemBean.setCount(TgSystemHelper.getInformCount()); // 未读数量
        funItemBeanList.add(index, funItemBean); // 通知,将通知加到index位置
    }

    @Override
    public void onResume() {
        super.onResume();
        initInforCount();
    }

    /**
     * 未读消息数量
     */
    private void initInforCount() {
        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_INFORM_COUNT, getHttpTaskKey());
        request.params("userId", TgSystemHelper.getUserId());
        request.execute(new TgHttpCallback(null) {
            @Override
            public void successHandler(final TgResponseBean result) {
                if (result.isSuccess()) {
                    if (result.getData() == null) {
                        return;
                    }
                    Map<String, Object> map = (Map<String, Object>) result.getData();
                    int informCount = (Integer) map.get("count");
                    TgApplication.getTgUserPreferences().setInformCount(informCount);
                    funItemBeanList.get(index).setCount(TgSystemHelper.getInformCount()); // 将第二个数据的消息数改为一个随机的数字
                    mAdapter.notifyItemChanged(index); // adapter刷新
                } else {
                    TgDialogUtil.showToast(result.getMsg()); // 弹出错误信息
                }
            }
        });
    }

    /**
     * 初始化recyclerView和adapter
     */
    private void initRecyclerAndAdapter() {
        rvFun.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        mAdapter.openLoadAnimation(); // 动画
        rvFun.setAdapter(mAdapter);
        mAdapter.setNewData(funItemBeanList);
    }
}
