package com.yone.funnews.base;

/**
 * Created by Yoe on 2016/10/8.
 * Presenter基类
 */

public interface BasePresenter<T extends BaseView> {

    void attachView(T view);

    void detachView();
}
