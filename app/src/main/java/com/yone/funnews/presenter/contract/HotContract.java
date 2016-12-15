package com.yone.funnews.presenter.contract;

import com.yone.funnews.base.BasePresenter;
import com.yone.funnews.base.BaseView;
import com.yone.funnews.model.been.HotListBean;

/**
 * Created by Yoe on 2016/10/20.
 */

public interface HotContract {

    interface View extends BaseView{
        void showContent(HotListBean hotListBean);
    }

    interface Presenter extends BasePresenter<View>{

        void getHotData();

        void insertReadToDB(int id);
    }
}
