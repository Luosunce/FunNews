package com.yone.funnews.presenter;

import com.yone.funnews.base.RxPresenter;
import com.yone.funnews.model.been.ThemeChildListBean;
import com.yone.funnews.model.db.RealmHelper;
import com.yone.funnews.model.http.RetrofitHelper;
import com.yone.funnews.presenter.contract.ThemeChildContract;
import com.yone.funnews.util.RxUtil;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Yoe on 2016/10/19.
 */

public class ThemeChildPresenter extends RxPresenter<ThemeChildContract.View> implements ThemeChildContract.Presenter {

    private RetrofitHelper mRetrofitHelper;
    private RealmHelper mRealmHelper;

    @Inject
    public ThemeChildPresenter(RetrofitHelper mRetrofitHelper,RealmHelper mRealmHelper){
         this.mRetrofitHelper = mRetrofitHelper;
         this.mRealmHelper = mRealmHelper;
    }

    @Override
    public void getThemeChildData(int id) {
        Subscription rxSubscription = mRetrofitHelper.fetchThemeChildListInfo(id)
                .compose(RxUtil.<ThemeChildListBean>rxSchedulerHelper())
                .map(new Func1<ThemeChildListBean, ThemeChildListBean>() {
                    @Override
                    public ThemeChildListBean call(ThemeChildListBean themeChildListBean) {
                        List<ThemeChildListBean.StoriesBean> list = themeChildListBean.getStories();
                        for (ThemeChildListBean.StoriesBean item : list) {
                            item.setReadState(mRealmHelper.queryNewsId(item.getId()));
                        }
                        return themeChildListBean;
                    }
                })
                .subscribe(new Action1<ThemeChildListBean>() {
                    @Override
                    public void call(ThemeChildListBean themeChildListBean) {
                        mView.showContent(themeChildListBean);
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
    public void insertReadToDB(int id) {
       mRealmHelper.insertNewsId(id);
    }

}
