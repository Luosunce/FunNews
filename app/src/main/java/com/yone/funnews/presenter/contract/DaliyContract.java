package com.yone.funnews.presenter.contract;

import com.yone.funnews.base.BasePresenter;
import com.yone.funnews.base.BaseView;
import com.yone.funnews.model.been.DailyBeforeListBean;
import com.yone.funnews.model.been.DailyListBean;

/**
 * Created by Yoe on 2016/10/13.
 */

public interface DaliyContract {

    interface View extends  BaseView {

        void showContent(DailyListBean info);

        void showMoreContent(String data, DailyBeforeListBean info);

        void showProgress();

        void doInterval(int currentCount);
    }

    interface Presenter extends BasePresenter<View>{

        void getDailyData();

        void getBeforeDate(String date);

        void startInterval();

        void stopInterval();

        void insertReadToDB(int id);
    }
}
