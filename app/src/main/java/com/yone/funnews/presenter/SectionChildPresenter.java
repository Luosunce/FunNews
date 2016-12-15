package com.yone.funnews.presenter;


import com.yone.funnews.base.RxPresenter;
import com.yone.funnews.model.been.SectionChildListBean;
import com.yone.funnews.model.db.RealmHelper;
import com.yone.funnews.model.http.RetrofitHelper;
import com.yone.funnews.presenter.contract.SectionChildContract;
import com.yone.funnews.util.RxUtil;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Yoe on 2016/10/20.
 */

public class SectionChildPresenter extends RxPresenter<SectionChildContract.View> implements SectionChildContract.Presenter {

    private RetrofitHelper mRetrofitHelper;
    private RealmHelper mRealmHelper;

    @Inject
    public SectionChildPresenter(RetrofitHelper mRetrofitHelper,RealmHelper mRealmHelper){
        this.mRetrofitHelper = mRetrofitHelper;
        this.mRealmHelper = mRealmHelper;
    }

    @Override
    public void getSectionChildData(int id) {
        Subscription rxSubscription = mRetrofitHelper.fecthSectionChildListInfo(id)
                .compose(RxUtil.<SectionChildListBean>rxSchedulerHelper())
                .map(new Func1<SectionChildListBean, SectionChildListBean>() {
                    @Override
                    public SectionChildListBean call(SectionChildListBean sectionChildListBean) {
                        List<SectionChildListBean.StoriesBean> list = sectionChildListBean.getStories();
                        for (SectionChildListBean.StoriesBean item : list) {
                            item.setReadState(mRealmHelper.queryNewsId(item.getId()));
                        }
                        return sectionChildListBean;
                    }
                })
                .subscribe(new Action1<SectionChildListBean>() {
                    @Override
                    public void call(SectionChildListBean sectionChildListBean) {
                        mView.showContent(sectionChildListBean);
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
    public void insertReadToDB(int id) {
        mRealmHelper.insertNewsId(id);
    }
}
