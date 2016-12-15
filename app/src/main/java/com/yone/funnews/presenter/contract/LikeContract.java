package com.yone.funnews.presenter.contract;

import com.yone.funnews.base.BasePresenter;
import com.yone.funnews.base.BaseView;
import com.yone.funnews.model.been.RealmLikeBean;

import java.util.List;

/**
 * Created by Yoe on 2016/10/25.
 */

public interface LikeContract {

    interface View extends BaseView{
        void showContent(List<RealmLikeBean> mList);
    }

    interface Presenter extends BasePresenter<View>{

        void getLikeData();

        void deleteLikeData(String id);

        void changeLikeTime(String id,long time,boolean isPlus);
    }
}
