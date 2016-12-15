package com.yone.funnews.presenter.contract;

import com.yone.funnews.base.BasePresenter;
import com.yone.funnews.base.BaseView;
import com.yone.funnews.model.been.WechatItemBean;

import java.util.List;

/**
 * Created by Yoe on 2016/10/24.
 */

public interface WechatContract {

    interface View extends BaseView{

        void showContent(List<WechatItemBean> mList);

        void showMoreContent(List<WechatItemBean> mList);

    }

    interface Presenter extends BasePresenter<View>{

        void getWechatData();

        void getMoreWechatData();

    }
}
