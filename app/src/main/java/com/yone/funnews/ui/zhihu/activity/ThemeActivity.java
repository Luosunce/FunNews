package com.yone.funnews.ui.zhihu.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.victor.loading.rotate.RotateLoading;
import com.yone.funnews.R;
import com.yone.funnews.base.BaseActivity;
import com.yone.funnews.model.been.ThemeChildListBean;
import com.yone.funnews.presenter.ThemeChildPresenter;
import com.yone.funnews.presenter.contract.ThemeChildContract;
import com.yone.funnews.ui.zhihu.adapter.ThemeChildAdapter;
import com.yone.funnews.util.SnackbarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Yoe on 2016/10/18.
 */

public class ThemeActivity extends BaseActivity<ThemeChildPresenter> implements ThemeChildContract.View {


    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.rv_theme_child_list)
    RecyclerView mRvThemeChildList;
    @BindView(R.id.view_loading)
    RotateLoading mViewLoading;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    ThemeChildAdapter mAdapter;
    List<ThemeChildListBean.StoriesBean> mList;
    private int id;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_theme;
    }

    @Override
    protected void initEventAndData() {
        Intent intent = getIntent();
        id = intent.getExtras().getInt("id");
        mViewLoading.start();
        mList = new ArrayList<>();
        mAdapter = new ThemeChildAdapter(mContext,mList);
        mRvThemeChildList.setLayoutManager(new LinearLayoutManager(mContext));
        mRvThemeChildList.setAdapter(mAdapter);
        mPresenter.getThemeChildData(id);
        mAdapter.setOnItemClickListener(new ThemeChildAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View shareView) {
                mPresenter.insertReadToDB(mList.get(position).getId());
                mAdapter.setReadState(position,true);
                mAdapter.notifyItemChanged(position);
                Intent intent = new Intent();
                intent.setClass(mContext,ZhihuDetailActivity.class);
                intent.putExtra("id",mList.get(position).getId());
                if (shareView != null) {
                    mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(mContext,shareView,"shareView").toBundle());
                } else {
                    startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(mContext).toBundle());
                }
            }
        });
        mRvThemeChildList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View firstVisibleItem = recyclerView.getChildAt(0);
                int firstItemPosition = ((LinearLayoutManager) mRvThemeChildList.getLayoutManager()).findFirstVisibleItemPosition();
                int itemHeight = firstVisibleItem.getHeight();
                int firstItemBottom = mRvThemeChildList.getLayoutManager().getDecoratedBottom(firstVisibleItem);
                mAdapter.setTopAlpha(((firstItemPosition + 1) * itemHeight - firstItemBottom) * 2.0 / recyclerView.getChildAt(0).getHeight());
            }
        });
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getThemeChildData(id);
            }
        });
    }

    @Override
    public void showContent(ThemeChildListBean themeChildListBean) {
          if (mSwipeRefresh.isRefreshing()){
              mSwipeRefresh.setRefreshing(false);
          } else {
              mViewLoading.stop();
          }
        setToolBar(mToolBar,themeChildListBean.getName());
        mList.clear();
        mList.addAll(themeChildListBean.getStories());
        mAdapter.notifyDataSetChanged();
        mAdapter.setTopInfo(themeChildListBean.getBackground(),themeChildListBean.getDescription());
    }

    @Override
    public void showError(String msg) {
        if (mSwipeRefresh.isRefreshing()){
            mSwipeRefresh.setRefreshing(false);
        } else {
            mViewLoading.stop();
        }
        SnackbarUtil.showShort(getWindow().getDecorView(),msg);
    }

}
