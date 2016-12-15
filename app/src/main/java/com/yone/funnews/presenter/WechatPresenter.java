package com.yone.funnews.presenter;

import com.yone.funnews.app.Constants;
import com.yone.funnews.base.RxPresenter;
import com.yone.funnews.component.RxBus;
import com.yone.funnews.model.been.SearchEvent;
import com.yone.funnews.model.been.WechatItemBean;
import com.yone.funnews.model.http.RetrofitHelper;
import com.yone.funnews.model.http.WXHttpResponse;
import com.yone.funnews.presenter.contract.WechatContract;
import com.yone.funnews.util.RxUtil;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Yoe on 2016/10/24.
 */

public class WechatPresenter extends RxPresenter<WechatContract.View> implements WechatContract.Presenter {

    public static final String TECH_WECHAT = "微信";
    private static final int NUM_OF_PAGE = 20;

    private int currentPage = 1;
    private String queryStr = null;
    private RetrofitHelper mRetrofitHelper;

    @Inject
    public WechatPresenter(RetrofitHelper mRetrofitHelper){
        this.mRetrofitHelper = mRetrofitHelper;
        registerEvent();
    }

    void registerEvent(){
        Subscription rxSubscription = RxBus.getDefault().toObservable(SearchEvent.class)
                .compose(RxUtil.<SearchEvent>rxSchedulerHelper())
                .filter(new Func1<SearchEvent, Boolean>() {
                    @Override
                    public Boolean call(SearchEvent searchEvent) {
                        return searchEvent.getType() == Constants.TYPE_WECHAT;
                    }
                })
                .map(new Func1<SearchEvent, String>() {
                    @Override
                    public String call(SearchEvent searchEvent) {
                        return searchEvent.getQuery();
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        queryStr = s;
                        getSearchWechatData(s);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.showError("搜索失败ヽ(≧Д≦)ノ");
                    }
                });
        addSubscrebe(rxSubscription);
    }

    @Override
    public void getWechatData() {
        queryStr = null;
        currentPage = 1;
        Subscription rxSubscription = mRetrofitHelper.fetchWechatListInfo(NUM_OF_PAGE,currentPage)
                .compose(RxUtil.<WXHttpResponse<List<WechatItemBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<WechatItemBean>>handleWXResult())
                .subscribe(new Action1<List<WechatItemBean>>() {
                    @Override
                    public void call(List<WechatItemBean> wechatItemBeen) {
                        mView.showContent(wechatItemBeen);
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
    public void getMoreWechatData() {
        Observable<WXHttpResponse<List<WechatItemBean>>> observable;
        if (queryStr != null) {
            observable = mRetrofitHelper.fetchWechatSearchListInfo(NUM_OF_PAGE,++currentPage,queryStr);
        } else {
            observable = mRetrofitHelper.fetchWechatListInfo(NUM_OF_PAGE,++currentPage);
        }
        Subscription rxSubscription = observable
                .compose(RxUtil.<WXHttpResponse<List<WechatItemBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<WechatItemBean>>handleWXResult())
                .subscribe(new Action1<List<WechatItemBean>>() {
                    @Override
                    public void call(List<WechatItemBean> wechatItemBeen) {
                        mView.showMoreContent(wechatItemBeen);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.showError("没有更多了ヽ(≧Д≦)ノ");
                    }
                });
        addSubscrebe(rxSubscription);
    }

    private void getSearchWechatData(String query){
        currentPage = 1;
        Subscription rxSubscription = mRetrofitHelper.fetchWechatSearchListInfo(NUM_OF_PAGE,currentPage,query)
                .compose(RxUtil.<WXHttpResponse<List<WechatItemBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<WechatItemBean>>handleWXResult())
                .subscribe(new Action1<List<WechatItemBean>>() {
                    @Override
                    public void call(List<WechatItemBean> wechatItemBeen) {
                        mView.showContent(wechatItemBeen);
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
