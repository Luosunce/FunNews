package com.yone.funnews.presenter;

import com.yone.funnews.base.RxPresenter;
import com.yone.funnews.model.been.GankItemBean;
import com.yone.funnews.model.http.GankHttpResponse;
import com.yone.funnews.model.http.RetrofitHelper;
import com.yone.funnews.presenter.contract.GirlContract;
import com.yone.funnews.util.RxUtil;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Yoe on 2016/10/23.
 */

public class GirlPresenter extends RxPresenter<GirlContract.View> implements GirlContract.Presenter {

    private RetrofitHelper mRetrofitHelper;

    public static final int NUM_OF_PAGE = 20;

    private int currentPage = 1;

    @Inject
    public GirlPresenter(RetrofitHelper mRetrofitHelper){
        this.mRetrofitHelper = mRetrofitHelper;
    }

    @Override
    public void getGirlData() {
        currentPage = 1;
        Subscription rxSubscription = mRetrofitHelper.fetchGirlList(NUM_OF_PAGE,currentPage)
                .compose(RxUtil.<GankHttpResponse<List<GankItemBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<GankItemBean>>handleResult())
                .subscribe(new Action1<List<GankItemBean>>() {
                    @Override
                    public void call(List<GankItemBean> gankItemBeen) {
                        mView.showContent(gankItemBeen);
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
    public void getMoreGirlData() {
         Subscription rxSubscription = mRetrofitHelper.fetchGirlList(NUM_OF_PAGE,++currentPage)
                 .compose(RxUtil.<GankHttpResponse<List<GankItemBean>>>rxSchedulerHelper())
                 .compose(RxUtil.<List<GankItemBean>>handleResult())
                 .subscribe(new Action1<List<GankItemBean>>() {
                     @Override
                     public void call(List<GankItemBean> gankItemBeen) {
                         mView.showMoreContent(gankItemBeen);
                     }
                 }, new Action1<Throwable>() {
                     @Override
                     public void call(Throwable throwable) {
                         mView.showError("数据加载失败ヽ(≧Д≦)ノ");
                     }
                 });
        addSubscrebe(rxSubscription);
    }
}
