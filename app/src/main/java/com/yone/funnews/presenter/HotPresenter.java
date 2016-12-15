package com.yone.funnews.presenter;

import com.yone.funnews.base.RxPresenter;
import com.yone.funnews.model.been.HotListBean;
import com.yone.funnews.model.db.RealmHelper;
import com.yone.funnews.model.http.RetrofitHelper;
import com.yone.funnews.presenter.contract.HotContract;
import com.yone.funnews.util.RxUtil;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Yoe on 2016/10/20.
 */

public class HotPresenter extends RxPresenter<HotContract.View> implements HotContract.Presenter {

    private RetrofitHelper mRetrofitHelper;
    private RealmHelper mRealmHelper;

    @Inject
    public HotPresenter(RetrofitHelper mRetrofitHelper,RealmHelper mRealmHelper){
        this.mRetrofitHelper = mRetrofitHelper;
        this.mRealmHelper = mRealmHelper;
    }

    @Override
    public void getHotData() {
        Subscription rxSubscription = mRetrofitHelper.fecthHotListInfo()
                .compose(RxUtil.<HotListBean>rxSchedulerHelper())
                .map(new Func1<HotListBean, HotListBean>() {
                    @Override
                    public HotListBean call(HotListBean hotListBean) {
                        List<HotListBean.RecentBean> list = hotListBean.getRecent();
                        for (HotListBean.RecentBean item : list) {
                            item.setReadState(mRealmHelper.queryNewsId(item.getNews_id()));
                        }
                        return hotListBean;
                    }
                })
                .subscribe(new Action1<HotListBean>() {
                    @Override
                    public void call(HotListBean hotListBean) {
                        mView.showContent(hotListBean);
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
