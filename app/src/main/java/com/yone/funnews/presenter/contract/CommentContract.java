package com.yone.funnews.presenter.contract;

import com.yone.funnews.base.BasePresenter;
import com.yone.funnews.base.BaseView;
import com.yone.funnews.model.been.CommentBean;

/**
 * Created by Yoe on 2016/10/17.
 */

public interface CommentContract {

    interface View extends BaseView{
        void showContent(CommentBean commentBean);
    }

    interface Presenter extends BasePresenter<View>{
        void getCommentData(int id,int commentKind);
    }
}
