package com.yone.funnews.presenter.contract;

import com.yone.funnews.base.BasePresenter;
import com.yone.funnews.base.BaseView;

/**
 * Created by Yoe on 2016/10/11.
 */

public interface MainContract {

    interface View extends BaseView{}

    interface Presenter extends BasePresenter<View>{}
}
