package com.yone.funnews.ui.zhihu.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yone.funnews.R;
import com.yone.funnews.component.ImageLoader;
import com.yone.funnews.model.been.HotListBean;
import com.yone.funnews.widget.SquareImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yoe on 2016/10/20.
 */

public class HotAdapter extends RecyclerView.Adapter<HotAdapter.ViewHolder> {


    private List<HotListBean.RecentBean> mList;
    private Context mContext;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;

    public HotAdapter(Context mContext, List<HotListBean.RecentBean> mList) {
        this.mList = mList;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_daily, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mTvDailyItemTitle.setText(mList.get(position).getTitle());
        if (mList.get(position).getReadState()) {
            holder.mTvDailyItemTitle.setTextColor(ContextCompat.getColor(mContext,R.color.news_rea));
        } else {
            holder.mTvDailyItemTitle.setTextColor(ContextCompat.getColor(mContext,R.color.news_unread));
        }
        ImageLoader.load(mContext,mList.get(position).getThumbnail(),holder.mIvDailyItemImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    ImageView iv = (ImageView) view.findViewById(R.id.iv_daily_item_image);
                    onItemClickListener.onItemClick(holder.getAdapterPosition(),iv);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_daily_item_image)
        SquareImageView mIvDailyItemImage;
        @BindView(R.id.tv_daily_item_title)
        TextView mTvDailyItemTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setReadState(int position, boolean readState) {
        mList.get(position).setReadState(readState);
    }
}
