package com.yone.funnews.ui.zhihu.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.victor.loading.rotate.RotateLoading;
import com.yone.funnews.R;
import com.yone.funnews.base.BaseFragment;
import com.yone.funnews.model.been.ThemeListBean;
import com.yone.funnews.presenter.ThemePresenter;
import com.yone.funnews.presenter.contract.ThemeContract;
import com.yone.funnews.ui.zhihu.activity.ThemeActivity;
import com.yone.funnews.ui.zhihu.adapter.ThemeAdapter;
import com.yone.funnews.util.SnackbarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Yoe on 2016/10/13.
 */

public class ThemeFragment extends BaseFragment<ThemePresenter> implements ThemeContract.View {

    @BindView(R.id.rv_theme_list)
    RecyclerView mRvThemeList;
    @BindView(R.id.view_loading)
    RotateLoading mViewLoading;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    ThemeAdapter mAdapter;
    List<ThemeListBean.OthersBean> mList = new ArrayList<>();

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_theme;
    }

    @Override
    protected void initEventAndData() {
        mAdapter = new ThemeAdapter(mContext,mList);
        mRvThemeList.setLayoutManager(new GridLayoutManager(mContext,2));
        mRvThemeList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ThemeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int id) {
                Intent intent = new Intent();
                intent.setClass(mContext, ThemeActivity.class);
                intent.putExtra("id",id);
                mContext.startActivity(intent);
            }
        });
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getThemeData();
            }
        });
        mPresenter.getThemeData();
        mViewLoading.start();
    }

    @Override
    public void showContent(ThemeListBean themeListBean) {
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        } else {
            mViewLoading.stop();
        }
        mList.clear();
        mList.addAll(themeListBean.getOthers());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String msg) {
        if (mSwipeRefresh.isRefreshing()){
            mSwipeRefresh.setRefreshing(false);
        } else {
           mViewLoading.stop();
        }
        SnackbarUtil.showShort(mRvThemeList,msg);
    }

}
