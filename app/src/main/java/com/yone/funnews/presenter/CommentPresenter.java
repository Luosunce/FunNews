package com.yone.funnews.presenter;

import com.yone.funnews.base.RxPresenter;
import com.yone.funnews.model.been.CommentBean;
import com.yone.funnews.model.http.RetrofitHelper;
import com.yone.funnews.presenter.contract.CommentContract;
import com.yone.funnews.util.RxUtil;

import javax.inject.Inject;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Yoe on 2016/10/8.
 */

public class CommentPresenter extends RxPresenter<CommentContract.View> implements CommentContract.Presenter{

    public static final int SHORT_COMMENT = 0;

    public static final int LONG_COMMENT = 1;

    private RetrofitHelper mRetrofitHelper;

    @Inject
    public CommentPresenter(RetrofitHelper mRetrofitHelper){
        this.mRetrofitHelper = mRetrofitHelper;
    }


    @Override
    public void getCommentData(int id, int commentKind) {
        if (commentKind == SHORT_COMMENT) {
            Subscription rxSubscription = mRetrofitHelper.fetchShortCommentInfo(id)
                    .compose(RxUtil.<CommentBean>rxSchedulerHelper())
                    .subscribe(new Action1<CommentBean>() {
                        @Override
                        public void call(CommentBean commentBean) {
                            mView.showContent(commentBean);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            mView.showError("数据加载失败ヽ(≧Д≦)ノ");
                        }
                    });
            addSubscrebe(rxSubscription);
        } else {
            Subscription rxSubscription = mRetrofitHelper.fetchLongCommentInfo(id)
                    .compose(RxUtil.<CommentBean>rxSchedulerHelper())
                    .subscribe(new Action1<CommentBean>() {
                        @Override
                        public void call(CommentBean commentBean) {
                            mView.showContent(commentBean);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            mView.showError("数据加载失败ヽ(≧Д≦)ノ");
                        }
                    });
            addSubscrebe(rxSubscription);
        }
    }
}
