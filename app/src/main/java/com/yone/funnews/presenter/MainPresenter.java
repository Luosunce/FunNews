package com.yone.funnews.presenter;

import com.yone.funnews.base.RxPresenter;
import com.yone.funnews.component.RxBus;
import com.yone.funnews.model.been.NightModeEvent;
import com.yone.funnews.presenter.contract.MainContract;
import com.yone.funnews.util.RxUtil;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.functions.Func1;


/**
 * Created by Yoe on 2016/10/11.
 */

public class MainPresenter extends RxPresenter<MainContract.View> implements MainContract.Presenter {

    @Inject
    public MainPresenter(){
        registerEvent();
    }

    void registerEvent(){
        Subscription rxSubscription = RxBus.getDefault().toObservable(NightModeEvent.class)
                .compose(RxUtil.<NightModeEvent>rxSchedulerHelper())
                .map(new Func1<NightModeEvent, Boolean>() {
                    @Override
                    public Boolean call(NightModeEvent nightModeEvent) {
                        return nightModeEvent.getNightMode();
                    }
                }).subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError("切换模式失败ヽ(≧Д≦)ノ");
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        mView.useNightMode(aBoolean);
                    }
                });
        addSubscrebe(rxSubscription);
    }
}
