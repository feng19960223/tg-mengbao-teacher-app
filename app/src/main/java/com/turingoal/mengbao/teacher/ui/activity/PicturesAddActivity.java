package com.turingoal.mengbao.teacher.ui.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.lzy.okgo.request.PostRequest;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseActivity;
import com.turingoal.common.bean.TgResponseBean;
import com.turingoal.common.utils.TgDialogUtil;
import com.turingoal.common.utils.TgHttpCallback;
import com.turingoal.common.utils.TgHttpUtil;
import com.turingoal.common.widget.ClearEditText;
import com.turingoal.mengbao.common.biz.domain.Album;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;
import com.turingoal.mengbao.teacher.constants.ConstantUrls;
import com.turingoal.mengbao.teacher.event.EventConsts;
import com.turingoal.mengbao.teacher.event.EventLogger;
import com.turingoal.mengbao.teacher.ui.adapter.PicturesAddAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 发布照片
 */
@Route(path = ConstantActivityPath.PICTURES_ADD)
public class PicturesAddActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvEnd)
    TextView tvEnd; // 确定
    @BindView(R.id.etTitle)
    ClearEditText etTitle; // title
    @BindView(R.id.tvTitleCount)
    TextView tvTitleCount;
    @BindView(R.id.rvPictures)
    RecyclerView rvPictures;
    @Autowired
    Album album;
    private boolean isTitle = false;
    private PicturesAddAdapter mAdapter = new PicturesAddAdapter();
    private static final int SPAN_COUNT = 4;
    private ItemTouchHelper mItemTouchHelper = null;
    private ItemDragAndSwipeCallback mItemDragAndSwipeCallback = null;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_pictures_add;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_pictures_add);
        tvEnd.setVisibility(View.VISIBLE);
        tvEnd.setEnabled(false);
        etTitle.addTextChangedListener(titleTextWatcher);
        etTitle.setOnTextClearListener(new ClearEditText.OnTextClearListener() { // 清空按钮
            @Override
            public void textClear() {
                isTitle = false;
                tvEnd.setEnabled(false);
            }
        });
        initRecyclerAndAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isTitle && mAdapter.getData().size() > 1) {
            tvEnd.setEnabled(true);
        } else {
            tvEnd.setEnabled(false);
        }
    }

    /**
     * 初始化recyclerView和adapter
     */
    private void initRecyclerAndAdapter() {
        rvPictures.setLayoutManager(new GridLayoutManager(this, SPAN_COUNT));
        rvPictures.setAdapter(mAdapter);
        mItemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter) {
            @Override
            public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder current, RecyclerView.ViewHolder target) {
                // 最后一个为加号，所以不支持【拖拽换位置】
                return target.getAdapterPosition() != recyclerView.getAdapter().getItemCount() - 1 && super.canDropOver(recyclerView, current, target);
            }
        };
        mItemTouchHelper = new ItemTouchHelper(mItemDragAndSwipeCallback);
        mItemTouchHelper.attachToRecyclerView(rvPictures); // 附加到RecyclerView
        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                // 如果item不是最后一个，则可以拖拽【不是加号，可以拖拽】
                if (position != adapter.getData().size() - 1) {
                    mItemTouchHelper.startDrag(rvPictures.getChildViewHolder(view));
                }
                return false;
            }
        });
    }

    /**
     * 标题EditText监听
     */
    private TextWatcher titleTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }

        @Override
        public void onTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }

        @Override
        public void afterTextChanged(final Editable editable) {
            if (editable.toString().trim().length() != 0) {
                isTitle = true;
            } else {
                isTitle = false;
            }
            tvTitleCount.setText(editable.toString().trim().length() + "/10");
            if (isTitle && mAdapter.getData().size() > 1) {
                tvEnd.setEnabled(true);
            } else {
                tvEnd.setEnabled(false);
            }
        }
    };

    /**
     * OnClick
     */
    @OnClick({R.id.ivStart, R.id.tvEnd})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.ivStart: // 返回
                defaultFinish();
                break;
            case R.id.tvEnd: // 发布
                addRequest();
                break;
            default:
                break;
        }
    }

    /**
     * 增加
     */
    private void addRequest() {
        EventLogger.logEvent(EventConsts.e_Id_Pictures);
        List<File> files = new ArrayList<>();
        for (String path : mAdapter.getData().subList(0, mAdapter.getData().size() - 1)) { // 数据结果去除最后一个加号
            files.add(new File(path));
        }
        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_ALBUM_GROUP_ADD, getHttpTaskKey());
        request.params("albumId", album.getId());
        request.params("description", etTitle.getText().toString().trim());
        request.addFileParams("photo", files);
        request.params("userId", TgSystemHelper.getUserId());
        request.params("userCodeNum", TgSystemHelper.getUserCodeNum());
        request.params("userRealname", TgSystemHelper.getRealname());
        request.execute(new TgHttpCallback(this) {
            @Override
            public void successHandler(final TgResponseBean result) {
                if (result.isSuccess()) {
                    defaultFinish();
                } else {
                    TgDialogUtil.showToast(result.getMsg()); // 登录失败，弹出错误信息
                }
            }
        });
    }
}
