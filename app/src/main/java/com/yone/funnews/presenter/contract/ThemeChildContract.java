package com.yone.funnews.presenter.contract;

import com.yone.funnews.base.BasePresenter;
import com.yone.funnews.base.BaseView;
import com.yone.funnews.model.been.ThemeChildListBean;

/**
 * Created by Yoe on 2016/10/19.
 */

public interface ThemeChildContract {

    interface View extends BaseView{

        void showContent(ThemeChildListBean themeChildListBean);

    }

    interface Presenter extends BasePresenter<View>{

        void getThemeChildData(int id);

        void insertReadToDB(int id);
    }
}
