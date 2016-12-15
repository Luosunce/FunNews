package com.yone.funnews.ui.wechat.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yone.funnews.R;
import com.yone.funnews.component.ImageLoader;
import com.yone.funnews.model.been.WechatItemBean;
import com.yone.funnews.presenter.WechatPresenter;
import com.yone.funnews.ui.gank.activity.TechDetailActivity;
import com.yone.funnews.widget.SquareImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yoe on 2016/10/24.
 */

public class WechatAdapter extends RecyclerView.Adapter<WechatAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater inflater;
    private List<WechatItemBean> mList;

    public WechatAdapter(Context mContext, List<WechatItemBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_wechat, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ImageLoader.load(mContext,mList.get(position).getPicUrl(),holder.mIvWechatItemImage);
        holder.mTvWechatItemTitle.setText(mList.get(position).getTitle());
        holder.mTvWechatItemTime.setText(mList.get(position).getCtime());
        holder.mTvWechatItemFrom.setText(mList.get(position).getDescription());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, TechDetailActivity.class);
                intent.putExtra("id",mList.get(position).getPicUrl());
                intent.putExtra("title",mList.get(position).getTitle());
                intent.putExtra("url",mList.get(position).getUrl());
                intent.putExtra("tech", WechatPresenter.TECH_WECHAT);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_wechat_item_image)
        SquareImageView mIvWechatItemImage;
        @BindView(R.id.tv_wechat_item_title)
        TextView mTvWechatItemTitle;
        @BindView(R.id.tv_wechat_item_from)
        TextView mTvWechatItemFrom;
        @BindView(R.id.tv_wechat_item_time)
        TextView mTvWechatItemTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
