package com.yone.funnews.ui.gank.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.victor.loading.rotate.RotateLoading;
import com.yone.funnews.R;
import com.yone.funnews.base.BaseFragment;
import com.yone.funnews.model.been.GankItemBean;
import com.yone.funnews.presenter.TechPresenter;
import com.yone.funnews.presenter.contract.TechContract;
import com.yone.funnews.ui.gank.activity.TechDetailActivity;
import com.yone.funnews.ui.gank.adapter.TechAdapter;
import com.yone.funnews.util.DateUtil;
import com.yone.funnews.util.SnackbarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Yoe on 2016/10/21.
 */

public class TechFragment extends BaseFragment<TechPresenter> implements TechContract.View {


    @BindView(R.id.rv_tech_content)
    RecyclerView mRvTechContent;
    @BindView(R.id.view_loading)
    RotateLoading mViewLoading;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    List<GankItemBean> mList;
    TechAdapter mAdapter;
    private boolean isLoadingMore = false;
    private String tech;

    @Override
    protected void initInject() {
         getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tech;
    }

    @Override
    protected void initEventAndData() {
        mPresenter.getGirlImage();
        mList = new ArrayList<>();
        tech = getArguments().getString("tech");
        mAdapter = new TechAdapter(mContext,mList,tech);
        mRvTechContent.setLayoutManager(new LinearLayoutManager(mContext));
        mRvTechContent.setAdapter(mAdapter);
        mViewLoading.start();
        mPresenter.getGankData(tech);
        mAdapter.setOnItemClickListener(new TechAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View shareView) {
                Intent intent = new Intent();
                intent.setClass(mContext, TechDetailActivity.class);
                intent.putExtra("url",mList.get(position).getUrl());
                intent.putExtra("title",mList.get(position).getDesc());
                intent.putExtra("id",mList.get(position).get_id());
                intent.putExtra("tech",tech);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(mActivity,shareView,"shareView");
                mContext.startActivity(intent,options.toBundle());
            }
        });
        mRvTechContent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = ((LinearLayoutManager) mRvTechContent.getLayoutManager()).findLastVisibleItemPosition();
                int totalItemCount = mRvTechContent.getLayoutManager().getItemCount();
                if (lastVisibleItem >= totalItemCount - 2 && dy >0){
                    if (!isLoadingMore){
                        isLoadingMore = true;
                        mPresenter.getMoreGankData(tech);
                    }
                }
                View firstVisibleItem = recyclerView.getChildAt(0);
                int firstItemPosition = ((LinearLayoutManager) mRvTechContent.getLayoutManager()).findFirstVisibleItemPosition();
                int itemHeigt = firstVisibleItem.getHeight();
                int firstItemBottom = mRvTechContent.getLayoutManager().getDecoratedBottom(firstVisibleItem);
                mAdapter.setTopAlpha(((firstItemPosition + 1) * itemHeigt - firstItemBottom) * 2.0 / recyclerView.getChildAt(0).getHeight());
            }
        });
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getGirlImage();
                mPresenter.getGankData(tech);
            }
        });
    }

    @Override
    public void showContent(List<GankItemBean> list) {
        if (mSwipeRefresh.isRefreshing()){
            mSwipeRefresh.setRefreshing(false);
        } else {
            mViewLoading.stop();
        }
        mList.clear();
        mList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMoreContent(List<GankItemBean> list) {
        mViewLoading.stop();
        mList.clear();
        mList.addAll(list);
        mAdapter.notifyDataSetChanged();
        isLoadingMore = false;
    }

    @Override
    public void showGirlImage(String url) {
        mAdapter.setTopInfo(url, DateUtil.getCurrentDateString());
        mAdapter.notifyItemChanged(0);
    }

    @Override
    public void showError(String msg) {
        if (mSwipeRefresh.isRefreshing()){
            mSwipeRefresh.setRefreshing(false);
        } else {
            mViewLoading.stop();
        }
        SnackbarUtil.showShort(mRvTechContent,msg);
    }

}
