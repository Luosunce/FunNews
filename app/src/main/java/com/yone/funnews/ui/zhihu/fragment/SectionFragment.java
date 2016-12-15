package com.yone.funnews.ui.zhihu.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.victor.loading.rotate.RotateLoading;
import com.yone.funnews.R;
import com.yone.funnews.base.BaseFragment;
import com.yone.funnews.model.been.SectionListBean;
import com.yone.funnews.presenter.SectionPresenter;
import com.yone.funnews.presenter.contract.SectionContract;
import com.yone.funnews.ui.zhihu.adapter.SectionAdapter;
import com.yone.funnews.util.SnackbarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Yoe on 2016/10/13.
 */

public class SectionFragment extends BaseFragment<SectionPresenter> implements SectionContract.View {

    @BindView(R.id.rv_section_list)
    RecyclerView mRvSectionList;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.view_loading)
    RotateLoading mViewLoading;

    List<SectionListBean.DataBean> mList;
    SectionAdapter mAdapter;

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_section;
    }

    @Override
    protected void initEventAndData() {
        mList = new ArrayList<>();
        mAdapter = new SectionAdapter(mContext, mList);
        mRvSectionList.setLayoutManager(new GridLayoutManager(mContext, 2));
        mRvSectionList.setAdapter(mAdapter);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getSectionData();
            }
        });
        mPresenter.getSectionData();
        mViewLoading.start();
    }

    @Override
    public void showContent(SectionListBean sectionListBean) {
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        } else {
            mViewLoading.stop();
        }
        mList.clear();
        mList.addAll(sectionListBean.getData());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String msg) {
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        } else {
            mViewLoading.stop();
        }
        SnackbarUtil.showShort(mRvSectionList, msg);
    }

}
