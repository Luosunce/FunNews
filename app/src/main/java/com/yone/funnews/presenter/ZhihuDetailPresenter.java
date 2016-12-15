package com.yone.funnews.presenter;

import com.yone.funnews.app.Constants;
import com.yone.funnews.base.RxPresenter;
import com.yone.funnews.model.been.DetailExtraBean;
import com.yone.funnews.model.been.RealmLikeBean;
import com.yone.funnews.model.been.ZhihuDetailBean;
import com.yone.funnews.model.db.RealmHelper;
import com.yone.funnews.model.http.RetrofitHelper;
import com.yone.funnews.presenter.contract.ZhihuDetailContract;
import com.yone.funnews.util.RxUtil;

import javax.inject.Inject;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Yoe on 2016/10/14.
 */

public class ZhihuDetailPresenter extends RxPresenter<ZhihuDetailContract.View> implements ZhihuDetailContract.Presenter{

    private RetrofitHelper mRetrofitHelper;
    private RealmHelper mRealmHelper;
    private ZhihuDetailBean mData;

    @Inject
    public ZhihuDetailPresenter(RetrofitHelper mRetrofitHelper, RealmHelper mRealmHelpe){
        this.mRetrofitHelper = mRetrofitHelper;
        this.mRealmHelper = mRealmHelpe;
    }

    @Override
    public void getDetailData(int id) {
        Subscription rxSubscription = mRetrofitHelper.fetchDetailyInfo(id)
                .compose(RxUtil.<ZhihuDetailBean>rxSchedulerHelper())
                .subscribe(new Action1<ZhihuDetailBean>() {
                    @Override
                    public void call(ZhihuDetailBean zhihuDetailBean) {
                        mData = zhihuDetailBean;
                        mView.showContent(zhihuDetailBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.showError("加载数据失败");
                    }
                });
        addSubscrebe(rxSubscription);
    }

    @Override
    public void getExtraData(int id) {

        Subscription rxSubscription = mRetrofitHelper.fetchDetailExtraInfo(id)
                .compose(RxUtil.<DetailExtraBean>rxSchedulerHelper())
                .subscribe(new Action1<DetailExtraBean>() {
                    @Override
                    public void call(DetailExtraBean detailExtraBean) {
                        mView.showExtraInfo(detailExtraBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.showError("加载额外信息失败");
                    }
                });
        addSubscrebe(rxSubscription);
    }

    @Override
    public void insertLikeData() {

        if (mData != null) {
            RealmLikeBean bean = new RealmLikeBean();
            bean.setId(String.valueOf(mData.getId()));
            bean.setImage(mData.getImage());
            bean.setTitle(mData.getTitle());
            bean.setType(Constants.TYPE_ZHIHU);
            bean.setTime(System.currentTimeMillis());
            mRealmHelper.insertLikeBean(bean);
        } else {
            mView.showError("操作失败");
        }
    }

    @Override
    public void deleteLikeData() {
        if (mData != null){
            mRealmHelper.deleteLikeBean(String.valueOf(mData.getId()));
        } else {
            mView.showError("操作失败");
        }
    }

    @Override
    public void queryLikeData(int id) {
        mView.setLikeButtonState(mRealmHelper.queryLikeId(String.valueOf(id)));
    }
}
