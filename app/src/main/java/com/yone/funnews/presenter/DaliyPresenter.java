package com.yone.funnews.presenter;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.yone.funnews.base.RxPresenter;
import com.yone.funnews.component.RxBus;
import com.yone.funnews.model.been.DailyBeforeListBean;
import com.yone.funnews.model.been.DailyListBean;
import com.yone.funnews.model.db.RealmHelper;
import com.yone.funnews.model.http.RetrofitHelper;
import com.yone.funnews.presenter.contract.DaliyContract;
import com.yone.funnews.util.DateUtil;
import com.yone.funnews.util.RxUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Yoe on 2016/10/13.
 * 日期为明天时，取latest接口的数据
 * 其他日期，取before接口的数据
 */

public class DaliyPresenter extends RxPresenter<DaliyContract.View> implements DaliyContract.Presenter{

    private RetrofitHelper mRetrofitHelper;
    private RealmHelper mRealmHelper;
    private Subscription intervalSubscription;

    private static final int INTERVAL_INSTANCE = 6;

    private int topCount = 0;
    private int currentTopCount = 0;

    @Inject
    public DaliyPresenter(RetrofitHelper mRetrofitHelper,RealmHelper mRealmHelper){
        this.mRetrofitHelper = mRetrofitHelper;
        this.mRealmHelper = mRealmHelper;
        registerEvent();
    }

    private void registerEvent(){
         Subscription rxSubscription = RxBus.getDefault().toObservable(CalendarDay.class)
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .map(new Func1<CalendarDay, String>() {
                     @Override
                     public String call(CalendarDay calendarDay) {
                         mView.showProgress();
                         StringBuilder date = new StringBuilder();
                         String year = String.valueOf(calendarDay.getYear());
                         String month = String.valueOf(calendarDay.getMonth() + 1);
                         String day = String.valueOf(calendarDay.getDay() + 1);
                         if (month.length() < 2){
                             month = "0" + month;
                         }
                         if (day.length() < 2) {
                             day = "0" + day;
                         }
                         return date.append(year).append(month).append(day).toString();
                     }
                 })
                 .filter(new Func1<String, Boolean>() {
                     @Override
                     public Boolean call(String s) {
                         if (s.equals(DateUtil.getTomorrowDate())) {
                             getDailyData();
                             return false;
                         }
                         return true;
                     }
                 })
                 .observeOn(Schedulers.io())
                 .flatMap(new Func1<String, Observable<DailyBeforeListBean>>() {
                     @Override
                     public Observable<DailyBeforeListBean> call(String date) {
                         return mRetrofitHelper.fechDailyBeforeListInfo(date);
                     }
                 })
                 .observeOn(AndroidSchedulers.mainThread())
                 .map(new Func1<DailyBeforeListBean, DailyBeforeListBean>() {
                     @Override
                     public DailyBeforeListBean call(DailyBeforeListBean dailyBeforeListBean) {
                         List<DailyListBean.StoriesBean> list = dailyBeforeListBean.getStories();
                         for (DailyListBean.StoriesBean item : list) {
                             item.setReadState(mRealmHelper.queryNewsId(item.getId()));
                         }
                         return dailyBeforeListBean;
                     }
                 })
                 .subscribe(new Action1<DailyBeforeListBean>() {
                                @Override
                                public void call(DailyBeforeListBean dailyBeforeListBean) {
                                    int year = Integer.valueOf(dailyBeforeListBean.getDate().substring(0,4));
                                    int month = Integer.valueOf(dailyBeforeListBean.getDate().substring(4,6));
                                    int day = Integer.valueOf(dailyBeforeListBean.getDate().substring(6,8));
                                    mView.showMoreContent(String.format("%d年%d月%d日",year,month,day),dailyBeforeListBean);
                                }
                            },
                         new Action1<Throwable>() {
                             @Override
                             public void call(Throwable throwable) {
                                 registerEvent();
                                 mView.showError("数据加载失败ヽ(≧Д≦)ノ");
                             }});
        addSubscrebe(rxSubscription);
    }

    @Override
    public void getDailyData() {
         Subscription rxSubscription = mRetrofitHelper.fetchDailyListInfo()
                 .compose(RxUtil.<DailyListBean>rxSchedulerHelper())
                 .map(new Func1<DailyListBean, DailyListBean>() {
                     @Override
                     public DailyListBean call(DailyListBean dailyListBean) {
                         List<DailyListBean.StoriesBean> list = dailyListBean.getStories();
                         for (DailyListBean.StoriesBean item : list) {
                             item.setReadState(mRealmHelper.queryNewsId(item.getId()));
                         }
                         return dailyListBean;
                     }
                 })
                 .subscribe(new Action1<DailyListBean>() {
                     @Override
                     public void call(DailyListBean dailyListBean) {
                         topCount = dailyListBean.getTop_stories().size();
                         mView.showContent(dailyListBean);
                     }
                 }, new Action1<Throwable>() {
                     @Override
                     public void call(Throwable throwable) {
                         mView.showError("数据加载失败ヽ(≧Д≦)ノ");
                     }
                 });
        addSubscrebe(rxSubscription);
    }

    @Override
    public void getBeforeDate(String date) {
        Subscription rxSubscription = mRetrofitHelper.fechDailyBeforeListInfo(date)
                .compose(RxUtil.<DailyBeforeListBean>rxSchedulerHelper())
                .map(new Func1<DailyBeforeListBean, DailyBeforeListBean>() {
                    @Override
                    public DailyBeforeListBean call(DailyBeforeListBean dailyBeforeListBean) {
                        List<DailyListBean.StoriesBean> list = dailyBeforeListBean.getStories();
                        for (DailyListBean.StoriesBean item :list){
                            item.setReadState(mRealmHelper.queryNewsId(item.getId()));
                        }
                        return dailyBeforeListBean;
                    }
                })
                .subscribe(new Action1<DailyBeforeListBean>() {
                    @Override
                    public void call(DailyBeforeListBean dailyBeforeListBean) {
                        int year = Integer.valueOf(dailyBeforeListBean.getDate().substring(0, 4));
                        int month = Integer.valueOf(dailyBeforeListBean.getDate().substring(4, 6));
                        int day = Integer.valueOf(dailyBeforeListBean.getDate().substring(6, 8));
                        mView.showMoreContent(String.format("%d年%d月%d日", year, month, day), dailyBeforeListBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.showError("数据加载失败ヽ(≧Д≦)ノ");
                    }
                });
        addSubscrebe(rxSubscription);
    }

    @Override
    public void startInterval() {
         intervalSubscription = Observable.interval(INTERVAL_INSTANCE, TimeUnit.SECONDS)
                 .compose(RxUtil.<Long>rxSchedulerHelper())
                 .subscribe(new Action1<Long>() {
                     @Override
                     public void call(Long aLong) {
                         if (currentTopCount == topCount)
                             currentTopCount = 0;
                         mView.doInterval(currentTopCount++);
                     }
                 });
        addSubscrebe(intervalSubscription);
    }

    @Override
    public void stopInterval() {
         if (intervalSubscription != null){
             intervalSubscription.unsubscribe();
         }
    }

    @Override
    public void insertReadToDB(int id) {
        mRealmHelper.insertNewsId(id);
    }
}
