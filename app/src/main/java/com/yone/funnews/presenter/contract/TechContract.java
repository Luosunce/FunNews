package com.yone.funnews.presenter.contract;

import com.yone.funnews.base.BasePresenter;
import com.yone.funnews.base.BaseView;
import com.yone.funnews.model.been.GankItemBean;

import java.util.List;

/**
 * Created by Yoe on 2016/10/21.
 */

public interface TechContract {

    interface View extends BaseView{

        void showContent(List<GankItemBean> mList);

        void showMoreContent(List<GankItemBean> mList);

        void showGirlImage(String url);
    }

    interface Presenter extends BasePresenter<View>{

        void getGankData(String tech);

        void getMoreGankData(String tech);

        void getGirlImage();
    }
}
