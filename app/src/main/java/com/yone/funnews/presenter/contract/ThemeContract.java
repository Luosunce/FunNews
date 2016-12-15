package com.yone.funnews.presenter.contract;

import com.yone.funnews.base.BasePresenter;
import com.yone.funnews.base.BaseView;
import com.yone.funnews.model.been.ThemeListBean;

/**
 * Created by Yoe on 2016/10/18.
 */

public interface ThemeContract {

    interface View extends BaseView{

        void showContent(ThemeListBean themeListBean);
    }

    interface Presenter extends BasePresenter<View>{
        void getThemeData();
    }
}
