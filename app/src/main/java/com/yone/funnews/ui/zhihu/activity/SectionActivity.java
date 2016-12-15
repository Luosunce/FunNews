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
import com.yone.funnews.model.been.SectionChildListBean;
import com.yone.funnews.presenter.SectionChildPresenter;
import com.yone.funnews.presenter.contract.SectionChildContract;
import com.yone.funnews.ui.zhihu.adapter.SectionChildAdapter;
import com.yone.funnews.util.SnackbarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Yoe on 2016/10/20.
 */

public class SectionActivity extends BaseActivity<SectionChildPresenter> implements SectionChildContract.View {

    @BindView(R.id.rv_section_content)
    RecyclerView mRvSectionContent;
    @BindView(R.id.view_loading)
    RotateLoading mViewLoading;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.tool_bar)
    Toolbar mToolBar;

    List<SectionChildListBean.StoriesBean> mList;
    SectionChildAdapter mAdapter;

    private int id;
    private String title;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_section;
    }

    @Override
    protected void initEventAndData() {
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        title = intent.getStringExtra("title");
        setToolBar(mToolBar,title);
        mList = new ArrayList<>();
        mAdapter = new SectionChildAdapter(mContext,mList);
        mRvSectionContent.setLayoutManager(new LinearLayoutManager(mContext));
        mRvSectionContent.setAdapter(mAdapter);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getSectionChildData(id);
            }
        });
        mAdapter.setOnItemClickListener(new SectionChildAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position, View shareView) {
                mPresenter.insertReadToDB(mList.get(position).getId());
                mAdapter.setReadState(position,true);
                mAdapter.notifyItemChanged(position);
                Intent intent = new Intent();
                intent.setClass(mContext,ZhihuDetailActivity.class);
                intent.putExtra("id",mList.get(position).getId());
                if (shareView != null){
                    mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(mContext,shareView,"shareView").toBundle());
                } else {
                    startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(mContext).toBundle());
                }
            }
        });
        mPresenter.getSectionChildData(id);
        mViewLoading.start();
    }

    @Override
    public void showContent(SectionChildListBean sectionChildListBean) {
        if (mSwipeRefresh.isRefreshing()){
            mSwipeRefresh.setRefreshing(false);
        } else {
            mViewLoading.stop();
        }
        mList.clear();
        mList.addAll(sectionChildListBean.getStories());
        mAdapter.notifyDataSetChanged();
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
