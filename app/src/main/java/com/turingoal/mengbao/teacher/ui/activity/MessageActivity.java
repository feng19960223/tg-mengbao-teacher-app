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
import com.turingoal.common.constants.TgConstantGetDataType;
import com.turingoal.common.utils.TgDialogUtil;
import com.turingoal.common.utils.TgHttpCallback;
import com.turingoal.common.utils.TgHttpUtil;
import com.turingoal.common.utils.TgJsonUtil;
import com.turingoal.mengbao.common.biz.domain.Message;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;
import com.turingoal.mengbao.teacher.constants.ConstantUrls;
import com.turingoal.mengbao.teacher.ui.adapter.MessageAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 通知
 */
@Route(path = ConstantActivityPath.MESSAGE)
public class MessageActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.rvMessage)
    RecyclerView rvMessage;
    private MessageAdapter mAdapter = new MessageAdapter();
    private int limitSize = 10; // 一次加载多少条数据
    private int pageSize = 2; // 第几页

    @Override
    protected int getLayoutID() {
        return R.layout.activity_message;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_message);
        initRecyclerAndAdapter();
        getData(TgConstantGetDataType.INIT);
    }

    /**
     * 加载消息数据
     */
    private void getData(final int getDataType) {
        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_MESSAGE, getHttpTaskKey());
        request.params("userId", TgSystemHelper.getUserId());
        if (TgConstantGetDataType.LOAD_MORE == getDataType) {
            request.params("page", pageSize);
        }
        request.params("limit", limitSize);
        request.execute(new TgHttpCallback(this) {
            @Override
            public void successHandler(final TgResponseBean result) {
                if (result.isSuccess()) {
                    if (result.getData() == null) {
                        return;
                    }
                    List<Message> messages = TgJsonUtil.jsonResultBean2List(result, Message.class);
                    if (messages != null) {
                        if (TgConstantGetDataType.INIT == getDataType) {
                            pageSize = 2;
                            mAdapter.setNewData(messages);
                        } else {
                            pageSize++;
                            // 去重
                            List<Message> messageList = new ArrayList<>();
                            messageList.addAll(messages);
                            messageList.removeAll(mAdapter.getData());
                            mAdapter.addData(messageList);
                        }
                        mAdapter.loadMoreComplete(); // 成功获取更多数据
                        if (messages.size() < limitSize) {
                            mAdapter.loadMoreEnd(false); // 加载结束，没有数据，false显示没有更多数据，true不显示任何提示信息
                        }
                        if (mAdapter.getItemCount() < limitSize) { // 第一页如果不够一页就不显示没有更多数据布局
                            mAdapter.loadMoreEnd(true);
                        }
                    }
                } else {
                    mAdapter.loadMoreFail(); // 获取更多数据失败，点击重试
                    TgDialogUtil.showToast(result.getMsg()); // 弹出错误信息
                }
            }
        });
    }

    /**
     * 初始化recyclerView和adapter
     */
    private void initRecyclerAndAdapter() {
        rvMessage.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() { // 加载更多
            @Override
            public void onLoadMoreRequested() {
                getData(TgConstantGetDataType.LOAD_MORE);
            }
        }, rvMessage);
        mAdapter.isFirstOnly(false); // 来回都要动画
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM); // 动画
        rvMessage.setAdapter(mAdapter);
        mAdapter.setEmptyView(getEmptyView((ViewGroup) rvMessage.getParent()));
    }

    /**
     * OnClick
     */
    @OnClick(R.id.ivStart)
    public void onViewClicked() {
        defaultFinish();
    }
}
