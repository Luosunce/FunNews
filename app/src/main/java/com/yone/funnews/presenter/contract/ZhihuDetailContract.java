package com.yone.funnews.presenter.contract;

import com.yone.funnews.base.BasePresenter;
import com.yone.funnews.base.BaseView;
import com.yone.funnews.model.been.DetailExtraBean;
import com.yone.funnews.model.been.ZhihuDetailBean;

/**
 * Created by Yoe on 2016/10/14.
 */

public interface ZhihuDetailContract  {

    interface View extends BaseView{

        void showContent(ZhihuDetailBean zhihuDetailBean);

        void showExtraInfo(DetailExtraBean detailExtraBean);

        void setLikeButtonState(boolean isLiked);

    }

    interface Presenter extends BasePresenter<View>{

        void getDetailData(int id);

        void getExtraData(int id);

        void insertLikeData();

        void deleteLikeData();

        void queryLikeData(int id);
    }
}
