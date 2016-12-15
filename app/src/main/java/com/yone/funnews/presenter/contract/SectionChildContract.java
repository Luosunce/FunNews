package com.yone.funnews.presenter.contract;

import com.yone.funnews.base.BasePresenter;
import com.yone.funnews.base.BaseView;
import com.yone.funnews.model.been.SectionChildListBean;

/**
 * Created by Yoe on 2016/10/20.
 */

public interface SectionChildContract {

    interface View extends BaseView{
        void showContent(SectionChildListBean sectionChildListBean);
    }

    interface Presenter extends BasePresenter<View>{

        void getSectionChildData(int id);

        void insertReadToDB(int id);
    }
}
