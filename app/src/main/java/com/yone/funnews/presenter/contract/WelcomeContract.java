package com.yone.funnews.presenter.contract;


import com.yone.funnews.base.BasePresenter;
import com.yone.funnews.base.BaseView;
import com.yone.funnews.model.been.WelcomeBean;

/**
 * Created by Yoe on 2016/10/10.
 */

public interface WelcomeContract {

    interface View extends BaseView{

        void showContent(WelcomeBean welcomeBean);

        void jumpToMain();

    }

    interface Presenter extends BasePresenter<View>{

        void getWelcomeData();
    }
}
