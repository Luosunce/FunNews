package com.yone.funnews.ui.zhihu.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.victor.loading.rotate.RotateLoading;
import com.yone.funnews.R;
import com.yone.funnews.base.BaseFragment;
import com.yone.funnews.model.been.CommentBean;
import com.yone.funnews.presenter.CommentPresenter;
import com.yone.funnews.presenter.contract.CommentContract;
import com.yone.funnews.ui.zhihu.adapter.CommentAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Yoe on 2016/10/17.
 */

public class CommentFragment extends BaseFragment<CommentPresenter> implements CommentContract.View {
    @BindView(R.id.rv_comment_list)
    RecyclerView mRvCommentList;
    @BindView(R.id.view_loading)
    RotateLoading mViewLoading;

    CommentAdapter mAdapter;
    List<CommentBean.CommentsBean> mList;


    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_comment;
    }

    @Override
    protected void initEventAndData() {
        Bundle bundle = getArguments();
        mPresenter.getCommentData(bundle.getInt("id"),bundle.getInt("kind"));
        mViewLoading.start();
        mRvCommentList.setVisibility(View.INVISIBLE);
        mList = new ArrayList<>();
        mAdapter = new CommentAdapter(mContext,mList);
        mRvCommentList.setLayoutManager(new LinearLayoutManager(mContext));
        mRvCommentList.setAdapter(mAdapter);
    }

    @Override
    public void showContent(CommentBean commentBean) {
        mViewLoading.stop();
        mRvCommentList.setVisibility(View.VISIBLE);
        mList.clear();
        mList.addAll(commentBean.getComments());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String msg) {
        mViewLoading.stop();
        mRvCommentList.setVisibility(View.VISIBLE);
    }

}
