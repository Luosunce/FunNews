package com.yone.funnews.presenter;

import com.yone.funnews.base.RxPresenter;
import com.yone.funnews.model.been.ThemeListBean;
import com.yone.funnews.model.http.RetrofitHelper;
import com.yone.funnews.presenter.contract.ThemeContract;
import com.yone.funnews.util.RxUtil;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by Yoe on 2016/10/18.
 */

public class ThemePresenter extends RxPresenter<ThemeContract.View> implements ThemeContract.Presenter {

    private RetrofitHelper mRetrofitHelper;

    @Inject
    public ThemePresenter(RetrofitHelper mRetrofitHelper){
        this.mRetrofitHelper = mRetrofitHelper;
    }

    @Override
    public void getThemeData() {
        mRetrofitHelper.fetchDailyThemeListInfo()
                .compose(RxUtil.<ThemeListBean>rxSchedulerHelper())
                .subscribe(new Action1<ThemeListBean>() {
                    @Override
                    public void call(ThemeListBean themeListBean) {
                        mView.showContent(themeListBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.showError("数据加载失败ヽ(≧Д≦)ノ");
                    }
                });
    }
}
