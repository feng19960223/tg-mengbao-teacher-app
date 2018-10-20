package com.turingoal.mengbao.teacher.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
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
import com.turingoal.mengbao.common.biz.domain.Album;
import com.turingoal.mengbao.common.biz.domain.AlbumGroup;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;
import com.turingoal.mengbao.teacher.constants.ConstantUrls;
import com.turingoal.mengbao.teacher.ui.adapter.PicturesAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 班级组相册
 */
@Route(path = ConstantActivityPath.PICTURES)
public class PicturesActivity extends TgBaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ivEnd)
    ImageView ivEnd; // 增加相片
    @BindView(R.id.rvPictures)
    RecyclerView rvPictures; // 班级相册
    @Autowired
    Album album; // 相册item
    PicturesAdapter mAdapter = new PicturesAdapter(); // adapter
    private int limitSize = 10; // 一次加载多少条数据
    private int pageSize = 2; // 第几页

    @Override
    protected int getLayoutID() {
        return R.layout.activity_pictures;
    }

    @Override
    protected void initialized() {
        tvTitle.setText(R.string.activity_pictures);
        ivEnd.setImageResource(R.drawable.ic_add_black_24dp);
        ivEnd.setVisibility(View.VISIBLE);
        initRecyclerAndAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(TgConstantGetDataType.INIT);
    }

    /**
     * 加载班级相册数据
     */
    private void getData(final int getDataType) {
        PostRequest request = TgHttpUtil.requestPost(ConstantUrls.URL_ALBUM_GROUP, getHttpTaskKey());
        request.params("albumId", album.getId());
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
                    List<AlbumGroup> albumGroups = TgJsonUtil.jsonResultBean2List(result, AlbumGroup.class);
                    if (albumGroups != null) {
                        if (TgConstantGetDataType.INIT == getDataType) {
                            pageSize = 2;
                            mAdapter.setNewData(albumGroups);
                        } else {
                            pageSize++;
                            // 去重
                            List<AlbumGroup> albumGroupList = new ArrayList<>();
                            albumGroupList.addAll(albumGroups);
                            albumGroupList.removeAll(mAdapter.getData());
                            mAdapter.addData(albumGroupList);
                        }
                        mAdapter.loadMoreComplete(); // 成功获取更多数据
                        if (albumGroups.size() < limitSize) {
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
        rvPictures.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() { // 加载更多
            @Override
            public void onLoadMoreRequested() {
                getData(TgConstantGetDataType.LOAD_MORE);
            }
        }, rvPictures);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM); // 动画
        rvPictures.setAdapter(mAdapter);
        mAdapter.setEmptyView(getEmptyView((ViewGroup) rvPictures.getParent()));
    }

    /**
     * OnClick
     */
    @OnClick({R.id.ivStart, R.id.ivEnd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivStart: // 返回
                defaultFinish();
                break;
            case R.id.ivEnd: // 增加
                TgSystemHelper.handleIntentWithObj(ConstantActivityPath.PICTURES_ADD,"album",album);
                break;
            default:
                break;
        }
    }
}
