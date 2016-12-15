package com.yone.funnews.ui.zhihu.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.victor.loading.rotate.RotateLoading;
import com.yone.funnews.R;
import com.yone.funnews.base.BaseFragment;
import com.yone.funnews.model.been.HotListBean;
import com.yone.funnews.presenter.HotPresenter;
import com.yone.funnews.presenter.contract.HotContract;
import com.yone.funnews.ui.zhihu.activity.ZhihuDetailActivity;
import com.yone.funnews.ui.zhihu.adapter.HotAdapter;
import com.yone.funnews.util.SnackbarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Yoe on 2016/10/13.
 */

public class HotFragment extends BaseFragment<HotPresenter> implements HotContract.View {

    @BindView(R.id.rv_hot_content)
    RecyclerView mRvHotContent;
    @BindView(R.id.view_loading)
    RotateLoading mViewLoading;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    private List<HotListBean.RecentBean> mList;
    private HotAdapter mAdapter;

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_hot;
    }

    @Override
    protected void initEventAndData() {
        mList = new ArrayList<>();
        mAdapter = new HotAdapter(mContext,mList);
        mRvHotContent.setVisibility(View.INVISIBLE);
        mRvHotContent.setLayoutManager(new LinearLayoutManager(mContext));
        mRvHotContent.setAdapter(mAdapter);
        mViewLoading.start();
        mPresenter.getHotData();
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getHotData();
            }
        });

        mAdapter.setOnItemClickListener(new HotAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View shareView) {
                mPresenter.insertReadToDB(mList.get(position).getNews_id());
                mAdapter.setReadState(position,true);
                mAdapter.notifyItemChanged(position);
                Intent intent = new Intent();
                intent.setClass(mContext, ZhihuDetailActivity.class);
                intent.putExtra("id",mList.get(position).getNews_id());
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(mActivity,shareView,"shareView");
                mContext.startActivity(intent,options.toBundle());
            }
        });
    }

    @Override
    public void showContent(HotListBean hotListBean) {
        if (mSwipeRefresh.isRefreshing()){
            mSwipeRefresh.setRefreshing(false);
        } else {
            mViewLoading.stop();
        }
        mRvHotContent.setVisibility(View.VISIBLE);
        mList.clear();
        mList.addAll(hotListBean.getRecent());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String msg) {
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        } else {
            mViewLoading.stop();
        }
        SnackbarUtil.showShort(mRvHotContent,msg);
    }

}
