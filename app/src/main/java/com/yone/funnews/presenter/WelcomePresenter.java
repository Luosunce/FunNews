package com.yone.funnews.presenter;

import com.yone.funnews.base.RxPresenter;
import com.yone.funnews.model.been.WelcomeBean;
import com.yone.funnews.model.http.RetrofitHelper;
import com.yone.funnews.presenter.contract.WelcomeContract;
import com.yone.funnews.util.RxUtil;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Yoe on 2016/10/10.
 */

public class WelcomePresenter extends RxPresenter<WelcomeContract.View> implements WelcomeContract.Presenter {

    private static final String RES = "1080*1776";

    private static final int COUNT_DOWN_TIME = 2200;

    private RetrofitHelper mRetrofitHelper;

    @Inject
    public WelcomePresenter(RetrofitHelper mRetrofitHelper){
        this.mRetrofitHelper = mRetrofitHelper;
    }

    @Override
    public void getWelcomeData() {
        Subscription rxSubscription = mRetrofitHelper.fechWelcomeInfo(RES)
                .compose(RxUtil.<WelcomeBean>rxSchedulerHelper())
                .subscribe(new Action1<WelcomeBean>() {
                    @Override
                    public void call(WelcomeBean welcomeBean) {
                        mView.showContent(welcomeBean);
                        startCountDown();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.showError("");
                        mView.jumpToMain();
                    }
                });
        addSubscrebe(rxSubscription);
    }

    private void startCountDown(){
        Subscription rxSubscription = Observable.timer(COUNT_DOWN_TIME, TimeUnit.MILLISECONDS)
                .compose(RxUtil.<Long>rxSchedulerHelper())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        mView.jumpToMain();
                    }
                });
        addSubscrebe(rxSubscription);
    }
}
