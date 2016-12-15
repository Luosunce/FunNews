package com.yone.funnews.presenter;

import com.yone.funnews.base.RxPresenter;
import com.yone.funnews.model.db.RealmHelper;
import com.yone.funnews.presenter.contract.LikeContract;

import javax.inject.Inject;

/**
 * Created by Yoe on 2016/10/25.
 */

public class LikePresenter extends RxPresenter<LikeContract.View> implements LikeContract.Presenter {

    private RealmHelper mRealmHelper;

    @Inject
    public LikePresenter(RealmHelper mRealmHelper){
        this.mRealmHelper = mRealmHelper;
    }

    @Override
    public void getLikeData() {
        mView.showContent(mRealmHelper.getLikeList());
    }

    @Override
    public void deleteLikeData(String id) {
       mRealmHelper.deleteLikeBean(id);
    }

    @Override
    public void changeLikeTime(String id, long time, boolean isPlus) {
         mRealmHelper.changeLikeTime(id,time,isPlus);
    }
}
