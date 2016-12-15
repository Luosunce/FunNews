package com.yone.funnews.ui.zhihu.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yone.funnews.R;
import com.yone.funnews.component.ImageLoader;
import com.yone.funnews.model.been.DailyBeforeListBean;
import com.yone.funnews.model.been.DailyListBean;
import com.yone.funnews.widget.SquareImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yoe on 2016/10/13.
 */

public class DailyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DailyListBean.StoriesBean> mList;
    private List<DailyListBean.TopStoriesBean> mTopList;
    private LayoutInflater mInflater;
    private Context mContext;
    private TopPagerAdapter mAdapter;
    private ViewPager topViewPager;
    private OnItemClickListener onItemClickListener;
    RecyclerView.ViewHolder holder;

    private boolean isBefore = false;
    private String currentTitle = "今日热闻";

    public enum ITEM_TYPE {
        ITEM_TOP,       //滚动栏
        ITEM_DATE,     //日期
        ITEM_CONTENT  //内容
    }

    public DailyAdapter(Context mContext, List<DailyListBean.StoriesBean> mList) {
        this.mList = mList;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getItemViewType(int position) {
        if (!isBefore) {
            if (position == 0) {
                return ITEM_TYPE.ITEM_TOP.ordinal();
            } else if (position == 1) {
                return ITEM_TYPE.ITEM_DATE.ordinal();
            } else {
                return ITEM_TYPE.ITEM_CONTENT.ordinal();
            }
        } else {
            if (position == 0) {
                return ITEM_TYPE.ITEM_TOP.ordinal();
            } else if (position == 1){
                return ITEM_TYPE.ITEM_DATE.ordinal();
            } else {
                return ITEM_TYPE.ITEM_CONTENT.ordinal();
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_TOP.ordinal()) {
            mAdapter = new TopPagerAdapter(mContext, mTopList);
            return new TopViewHolder(mInflater.inflate(R.layout.item_top, parent, false));
        } else if (viewType == ITEM_TYPE.ITEM_DATE.ordinal()) {
            return new DateViewHolder(mInflater.inflate(R.layout.item_date, parent, false));
        }
        return new ContentViewHolder(mInflater.inflate(R.layout.item_daily, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContentViewHolder) {
            final int contentPostion;
            if (isBefore) {
                contentPostion = position - 1;
            } else {
                contentPostion = position - 2;
            }
            ((ContentViewHolder) holder).title.setText(mList.get(contentPostion).getTitle());
            if (mList.get(contentPostion).getReadState()) {
                ((ContentViewHolder) holder).title.setTextColor(ContextCompat.getColor(mContext,R.color.news_rea));
            } else {
                ((ContentViewHolder) holder).title.setTextColor(ContextCompat.getColor(mContext,R.color.news_unread));
            }
            ImageLoader.load(mContext,mList.get(contentPostion).getImages().get(0),((ContentViewHolder)holder).image);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        ImageView iv = (ImageView) view.findViewById(R.id.iv_daily_item_image);
                        onItemClickListener.onItemClick(contentPostion,iv);
                    }
                }
            });
        } else if (holder instanceof DateViewHolder){
            ((DateViewHolder) holder).tvDate.setText(currentTitle);
        } else {
            ((TopViewHolder) holder).VpTop.setAdapter(mAdapter);
            topViewPager = ((TopViewHolder) holder).VpTop;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_daily_item_title)
        TextView title;
        @BindView(R.id.iv_daily_item_image)
        SquareImageView image;

        public ContentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public static class DateViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_daily_date)
        TextView tvDate;

        public DateViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public static class TopViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.vp_top)
        ViewPager VpTop;
        @BindView(R.id.ll_point_container)
        LinearLayout llContainer;

        public TopViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public void addDailyDate(DailyListBean info) {
        currentTitle = "今日热闻";
        mList = info.getStories();
        mTopList = info.getTop_stories();
        isBefore = false;
        notifyDataSetChanged();
    }

    public void addDailyBeforeDate(DailyBeforeListBean info) {
        int year = Integer.valueOf(info.getDate().substring(0,4));
        int month = Integer.valueOf(info.getDate().substring(4,6));
        int day = Integer.valueOf(info.getDate().substring(6,8));
        currentTitle = String.format("%d年%d月%d日",year,month,day);
        mList = info.getStories();
        mTopList = info.getTopstories();

        isBefore = true;
        notifyDataSetChanged();
    }

    public boolean getIsBefore() {
        return isBefore;
    }

    public void setReadState(int position, boolean readState) {
        mList.get(position).setReadState(readState);
    }

    public void changeToPager(int currentCount) {
        if (!isBefore && topViewPager != null) {
            topViewPager.setCurrentItem(currentCount);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

}
