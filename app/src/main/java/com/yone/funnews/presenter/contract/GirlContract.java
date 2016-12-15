package com.yone.funnews.presenter.contract;

import com.yone.funnews.base.BasePresenter;
import com.yone.funnews.base.BaseView;
import com.yone.funnews.model.been.GankItemBean;

import java.util.List;

/**
 * Created by Yoe on 2016/10/23.
 */

public interface GirlContract {

    interface View extends BaseView {

        void showContent(List<GankItemBean> list);

        void showMoreContent(List<GankItemBean> list);

    }

    interface Presenter extends BasePresenter<View>{

        void getGirlData();

        void getMoreGirlData();

    }
}
