package com.yone.funnews.ui.main.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.yone.funnews.R;
import com.yone.funnews.base.BaseFragment;
import com.yone.funnews.model.been.RealmLikeBean;
import com.yone.funnews.presenter.LikePresenter;
import com.yone.funnews.presenter.contract.LikeContract;
import com.yone.funnews.ui.main.adapter.LikeAdapter;
import com.yone.funnews.util.SharedPreferenceUtil;
import com.yone.funnews.util.SnackbarUtil;
import com.yone.funnews.widget.DefaultItemTouchHelpCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Yoe on 2016/10/11.
 */

public class LikeFragment extends BaseFragment<LikePresenter> implements LikeContract.View {

    @BindView(R.id.rv_like_list)
    RecyclerView mRvLikeList;
    LikeAdapter mAdapter;
    List<RealmLikeBean> mList;
    DefaultItemTouchHelpCallback mCallback;

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_like;
    }

    @Override
    protected void initEventAndData() {
        mList = new ArrayList<>();
        mAdapter = new LikeAdapter(mContext,mList);
        mRvLikeList.setLayoutManager(new LinearLayoutManager(mContext));
        mRvLikeList.setAdapter(mAdapter);
        mCallback = new DefaultItemTouchHelpCallback(new DefaultItemTouchHelpCallback.OnItemTouchCallbackListener() {
            @Override
            public void onSwiped(int adapterPosition) {
                //滑动删除的时候，从数据库、数据源移除，并刷新UI
                if (mList != null) {
                    mPresenter.deleteLikeData(mList.get(adapterPosition).getId());
                    mList.remove(adapterPosition);
                    mAdapter.notifyItemRemoved(adapterPosition);
                }
            }

            @Override
            public boolean onMove(int srcPosition, int targetPosition) {
                if (mList != null) {
                    //更换数据库中的数据Item的位置
                    boolean isPlus = srcPosition < targetPosition;
                    mPresenter.changeLikeTime(mList.get(srcPosition).getId(),mList.get(targetPosition).getTime(),isPlus);
                    //更换数据源中的数据Item的位置
                    Collections.swap(mList,srcPosition,targetPosition);
                    //更新UI中Item的位置，主要给用户看到交互效果
                    mAdapter.notifyItemMoved(srcPosition,targetPosition);
                    return true;
                }
                return false;
            }
        });
        mCallback.setDragEnable(true);
        mCallback.setSwipeEnable(true);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mCallback);
        itemTouchHelper.attachToRecyclerView(mRvLikeList);
        mPresenter.getLikeData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mPresenter.getLikeData();
            if (!SharedPreferenceUtil.getLikePoint()) {
                SnackbarUtil.show(mRvLikeList,"支持侧滑删除，长安拖拽哦(。`ω´・)");
                SharedPreferenceUtil.setLikePoint(true);
            }
        }
    }

    @Override
    public void showContent(List<RealmLikeBean> list) {
        mList.clear();
        mList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String msg) {
        SnackbarUtil.showShort(getActivity().getWindow().getDecorView(),msg);
    }

}
