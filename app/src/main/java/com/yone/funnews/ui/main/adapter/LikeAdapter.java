package com.yone.funnews.ui.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yone.funnews.R;
import com.yone.funnews.app.Constants;
import com.yone.funnews.component.ImageLoader;
import com.yone.funnews.model.been.RealmLikeBean;
import com.yone.funnews.presenter.TechPresenter;
import com.yone.funnews.presenter.WechatPresenter;
import com.yone.funnews.ui.gank.activity.GirlDetailActivity;
import com.yone.funnews.ui.gank.activity.TechDetailActivity;
import com.yone.funnews.ui.zhihu.activity.ZhihuDetailActivity;
import com.yone.funnews.widget.SquareImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yoe on 2016/10/25.
 */

public class LikeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<RealmLikeBean> mList;
    private LayoutInflater inflater;

    private static final int TYPE_ARTICLE = 0;
    private static final int TYPE_GIRL = 1;

    public LikeAdapter(Context mContext, List<RealmLikeBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemViewType(int position) {
        if (mList.get(position).getType() == Constants.TYPE_GIRL) {
            return TYPE_GIRL;
        } else {
            return TYPE_ARTICLE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ARTICLE) {
            return new ArticleViewHolder(inflater.inflate(R.layout.item_like_article, parent, false));
        } else {
            return new GirlViewHolder(inflater.inflate(R.layout.item_like_girl, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ArticleViewHolder){
            ((ArticleViewHolder) holder).mTvArticleTitle.setText(mList.get(position).getTitle());
            switch (mList.get(position).getType()){
                case Constants.TYPE_ZHIHU:
                    ImageLoader.load(mContext,mList.get(position).getImage(),((ArticleViewHolder) holder).mIvArticleImage);
                    ((ArticleViewHolder) holder).mTvArticleFrom.setText("来自知乎");
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            gotoDailyDetail(Integer.valueOf(mList.get(holder.getAdapterPosition()).getId()));
                        }
                    });
                    break;
                case Constants.TYPE_ANDROID:
                    ((ArticleViewHolder) holder) .mIvArticleImage.setImageResource(R.mipmap.ic_android);
                    ((ArticleViewHolder) holder) .mTvArticleFrom.setText("来自Android");
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            gotoTechDetail(mList.get(holder.getAdapterPosition()).getImage(),mList.get(holder.getAdapterPosition()).getTitle(),
                                    mList.get(holder.getAdapterPosition()).getId(), TechPresenter.TECH_ANDROID);
                        }
                    });
                    break;
                case Constants.TYPE_IOS:
                    ((ArticleViewHolder) holder).mIvArticleImage.setImageResource(R.mipmap.ic_ios);
                    ((ArticleViewHolder) holder).mTvArticleFrom.setText("来自iOS");
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            gotoTechDetail(mList.get(holder.getAdapterPosition()).getImage(),mList.get(holder.getAdapterPosition()).getTitle(),
                                    mList.get(holder.getAdapterPosition()).getId(),TechPresenter.TECH_IOS);
                        }
                    });
                    break;
                case Constants.TYPE_WEB:
                    ((ArticleViewHolder) holder).mIvArticleImage.setImageResource(R.mipmap.ic_web);
                    ((ArticleViewHolder) holder).mTvArticleFrom.setText("来自Web");
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            gotoTechDetail(mList.get(holder.getAdapterPosition()).getImage(),mList.get(holder.getAdapterPosition()).getTitle(),
                                    mList.get(holder.getAdapterPosition()).getId(),TechPresenter.TECH_WEB);
                        }
                    });
                    break;
                case Constants.TYPE_WECHAT:
                    ImageLoader.load(mContext,mList.get(position).getId(),((ArticleViewHolder) holder).mIvArticleImage);
                    ((ArticleViewHolder) holder).mTvArticleFrom.setText("来自微信");
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            gotoTechDetail(mList.get(holder.getAdapterPosition()).getImage(),mList.get(holder.getAdapterPosition()).getTitle(),
                                    mList.get(holder.getAdapterPosition()).getId(), WechatPresenter.TECH_WECHAT);
                        }
                    });
                    break;
            }
        } else if (holder instanceof GirlViewHolder){
            ImageLoader.load(mContext,mList.get(position).getImage(),((GirlViewHolder) holder).mIvGirlImage);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gotoGirlDetail(mList.get(holder.getAdapterPosition()).getImage(),mList.get(holder.getAdapterPosition()).getId());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_article_image)
        SquareImageView mIvArticleImage;
        @BindView(R.id.tv_article_title)
        TextView mTvArticleTitle;
        @BindView(R.id.tv_article_from)
        TextView mTvArticleFrom;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public static class GirlViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_girl_image)
        ImageView mIvGirlImage;
        @BindView(R.id.tv_girl_from)
        TextView mTvGirlFrom;

        public GirlViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public void gotoDailyDetail(int id){
        Intent intent = new Intent();
        intent.setClass(mContext, ZhihuDetailActivity.class);
        intent.putExtra("id",id);
        mContext.startActivity(intent);
    }

    public void gotoTechDetail(String url,String title,String id,String tech){
        Intent intent = new Intent();
        intent.setClass(mContext, TechDetailActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("title",title);
        intent.putExtra("id",id);
        intent.putExtra("tech",tech);
        mContext.startActivity(intent);
    }

    public void gotoGirlDetail(String url,String id){
        Intent intent = new Intent();
        intent.setClass(mContext, GirlDetailActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("id",id);
        mContext.startActivity(intent);
    }
}
