package com.yone.funnews.ui.wechat.framgent;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.victor.loading.rotate.RotateLoading;
import com.yone.funnews.R;
import com.yone.funnews.base.BaseFragment;
import com.yone.funnews.model.been.WechatItemBean;
import com.yone.funnews.presenter.WechatPresenter;
import com.yone.funnews.presenter.contract.WechatContract;
import com.yone.funnews.ui.wechat.adapter.WechatAdapter;
import com.yone.funnews.util.SnackbarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Yoe on 2016/10/11.
 */

public class WechatMainFragment extends BaseFragment<WechatPresenter> implements WechatContract.View {

    @BindView(R.id.rv_wechat_list)
    RecyclerView mRvWechatList;
    @BindView(R.id.view_loading)
    RotateLoading mViewLoading;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    WechatAdapter mAdapter;
    List<WechatItemBean> mList;
    boolean isLodingMore = false;

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_wechat;
    }

    @Override
    protected void initEventAndData() {
        mList = new ArrayList<>();
        mAdapter = new WechatAdapter(mContext,mList);
        mRvWechatList.setLayoutManager(new LinearLayoutManager(mContext));
        mRvWechatList.setAdapter(mAdapter);
        mRvWechatList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = ((LinearLayoutManager) mRvWechatList.getLayoutManager()).findLastVisibleItemPosition();
                int totalItemCount = mRvWechatList.getLayoutManager().getItemCount();
                if (lastVisibleItem >= totalItemCount - 2 && dy > 0){  //还剩2个Item时加载更多
                    if (isLodingMore) {
                        isLodingMore = true;
                        mPresenter.getMoreWechatData();
                    }
                }
            }
        });
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getWechatData();
            }
        });
        mViewLoading.start();
        mPresenter.getWechatData();
    }

    @Override
    public void showContent(List<WechatItemBean> list) {
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        } else {
            mViewLoading.stop();
        }
        mList.clear();
        mList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMoreContent(List<WechatItemBean> list) {
        mViewLoading.stop();
        mList.addAll(list);
        mAdapter.notifyDataSetChanged();
        isLodingMore = false;
    }

    @Override
    public void showError(String msg) {
        if (mSwipeRefresh.isRefreshing()){
            mSwipeRefresh.setRefreshing(false);
        } else {
            mViewLoading.stop();
        }
        SnackbarUtil.showShort(mRvWechatList,msg);
    }

}
