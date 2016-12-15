package com.yone.funnews.presenter;

import com.yone.funnews.base.RxPresenter;
import com.yone.funnews.model.been.SectionListBean;
import com.yone.funnews.model.http.RetrofitHelper;
import com.yone.funnews.presenter.contract.SectionContract;
import com.yone.funnews.util.RxUtil;

import javax.inject.Inject;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Yoe on 2016/10/20.
 */

public class SectionPresenter extends RxPresenter<SectionContract.View> implements SectionContract.Presenter{

    private RetrofitHelper mRetrofitHelper;

    @Inject
    public SectionPresenter(RetrofitHelper mRetrofitHelper){
        this.mRetrofitHelper = mRetrofitHelper;
    }

    @Override
    public void getSectionData() {
        Subscription rxSubscription = mRetrofitHelper.fecthSectionListInfo()
                .compose(RxUtil.<SectionListBean>rxSchedulerHelper())
                .subscribe(new Action1<SectionListBean>() {
                    @Override
                    public void call(SectionListBean sectionListBean) {
                        mView.showContent(sectionListBean);
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
