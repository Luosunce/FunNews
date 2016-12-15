package com.yone.funnews.ui.zhihu.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.victor.loading.rotate.RotateLoading;
import com.yone.funnews.R;
import com.yone.funnews.base.BaseFragment;
import com.yone.funnews.component.RxBus;
import com.yone.funnews.model.been.DailyBeforeListBean;
import com.yone.funnews.model.been.DailyListBean;
import com.yone.funnews.presenter.DaliyPresenter;
import com.yone.funnews.presenter.contract.DaliyContract;
import com.yone.funnews.ui.zhihu.activity.CalendarActivity;
import com.yone.funnews.ui.zhihu.activity.ZhihuDetailActivity;
import com.yone.funnews.ui.zhihu.adapter.DailyAdapter;
import com.yone.funnews.util.CircularAnimUtil;
import com.yone.funnews.util.DateUtil;
import com.yone.funnews.util.SnackbarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Yoe on 2016/10/13.
 */

public class DailyFragment extends BaseFragment<DaliyPresenter> implements DaliyContract.View {

    @BindView(R.id.view_loading)
    RotateLoading mViewLoading;
    @BindView(R.id.rv_daily_list)
    RecyclerView mRvDailyList;
    @BindView(R.id.fab_calender)
    FloatingActionButton mFabCalender;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    String currentDate;
    DailyAdapter mAdapter;
    List<DailyListBean.StoriesBean> mList = new ArrayList<>();

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_daily;
    }

    @Override
    protected void initEventAndData() {
        currentDate = DateUtil.getTomorrowDate();
        mAdapter = new DailyAdapter(mContext, mList);
        mAdapter.setOnItemClickListener(new DailyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View shareView) {
                mPresenter.insertReadToDB(mList.get(position).getId());
                mAdapter.setReadState(position, true);
                if (mAdapter.getIsBefore()) {
                    mAdapter.notifyItemChanged(position + 1);
                } else {
                    mAdapter.notifyItemChanged(position + 2);
                }
                Intent intent = new Intent();
                intent.setClass(mContext, ZhihuDetailActivity.class);
                intent.putExtra("id", mList.get(position).getId());
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(mActivity, shareView, "shareView");
                mContext.startActivity(intent,options.toBundle());
            }
        });
        mSwipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (currentDate.equals(DateUtil.getTomorrowDate())){
                    mPresenter.getDailyData();
                } else {
                    int year = Integer.valueOf(currentDate.substring(0,4));
                    int month = Integer.valueOf(currentDate.substring(4,6));
                    int day = Integer.valueOf(currentDate.substring(6,8));
                    CalendarDay date = CalendarDay.from(year,month - 1,day);
                    RxBus.getDefault().post(date);
                }
            }
        });
        mRvDailyList.setLayoutManager(new LinearLayoutManager(mContext));
        mRvDailyList.setAdapter(mAdapter);
        mViewLoading.start();
        mPresenter.getDailyData();
    }


    /**
     * 当天数据
     * @param info
     */
    @Override
    public void showContent(DailyListBean info) {
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        } else {
            mViewLoading.stop();
        }
        mList = info.getStories();
        currentDate = String.valueOf(Integer.valueOf(info.getDate()) + 1);
        mAdapter.addDailyDate(info);
        mPresenter.stopInterval();
        mPresenter.startInterval();
    }

    /**
     * 过往 数据
     * @param data
     * @param info
     */
    @Override
    public void showMoreContent(String data, DailyBeforeListBean info) {
          if (mSwipeRefresh.isRefreshing()){
              mSwipeRefresh.setRefreshing(false);
          } else {
              mViewLoading.stop();
          }
        mPresenter.stopInterval();
        mList = info.getStories();
        currentDate = String.valueOf(Integer.valueOf(info.getDate()));
        mViewLoading.stop();
        mAdapter.addDailyBeforeDate(info);
    }

    @Override
    public void showProgress() {
        mViewLoading.start();
    }

    @Override
    public void doInterval(int currentCount) {
        mAdapter.changeToPager(currentCount);
    }

    @Override
    public void showError(String msg) {
          if (mSwipeRefresh.isRefreshing()){
              mSwipeRefresh.setRefreshing(false);
          } else {
              mViewLoading.stop();
          }
        SnackbarUtil.showShort(mRvDailyList,msg);
    }


    @OnClick(R.id.fab_calender)
    void startCalender() {
        Intent it = new Intent();
        it.setClass(mContext, CalendarActivity.class);
        CircularAnimUtil.startActivity(mActivity,it,mFabCalender,R.color.fab_bg);
    }
}
