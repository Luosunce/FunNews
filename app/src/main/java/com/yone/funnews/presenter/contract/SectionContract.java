package com.yone.funnews.presenter.contract;

import com.yone.funnews.base.BasePresenter;
import com.yone.funnews.base.BaseView;
import com.yone.funnews.model.been.SectionListBean;

/**
 * Created by Yoe on 2016/10/20.
 */

public interface SectionContract {

    interface View extends BaseView{

        void showContent(SectionListBean sectionListBean);
    }

    interface Presenter extends BasePresenter<View>{

        void getSectionData();
    }
}
