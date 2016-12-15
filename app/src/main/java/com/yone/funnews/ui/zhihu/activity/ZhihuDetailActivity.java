package com.yone.funnews.ui.zhihu.activity;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.victor.loading.rotate.RotateLoading;
import com.yone.funnews.R;
import com.yone.funnews.base.BaseActivity;
import com.yone.funnews.component.ImageLoader;
import com.yone.funnews.model.been.DetailExtraBean;
import com.yone.funnews.model.been.ZhihuDetailBean;
import com.yone.funnews.presenter.ZhihuDetailPresenter;
import com.yone.funnews.presenter.contract.ZhihuDetailContract;
import com.yone.funnews.util.HtmlUtil;
import com.yone.funnews.util.ShareUtil;
import com.yone.funnews.util.SharedPreferenceUtil;
import com.yone.funnews.util.SnackbarUtil;
import com.yone.funnews.util.SystemUtil;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;


/**
 * Created by Yoe on 2016/10/13.
 */

public class ZhihuDetailActivity extends BaseActivity<ZhihuDetailPresenter> implements ZhihuDetailContract.View{


    @BindView(R.id.detail_bar_image)
    ImageView mDetailBarImage;
    @BindView(R.id.view_toolbar)
    Toolbar mViewToolbar;
    @BindView(R.id.detail_bar_copyright)
    TextView mDetailBarCopyright;
    @BindView(R.id.clp_toolbar)
    CollapsingToolbarLayout mClpToolbar;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;
    @BindView(R.id.wv_detail_content)
    WebView mWvDetailContent;
    @BindView(R.id.view_loading)
    RotateLoading mViewLoading;
    @BindView(R.id.tv_detail_bottom_like)
    TextView mTvDetailBottomLike;
    @BindView(R.id.tv_detail_bottom_comment)
    TextView mTvDetailBottomComment;
    @BindView(R.id.tv_detail_bottom_share)
    TextView mTvDetailBottomShare;
    @BindView(R.id.ll_detail_bottom)
    FrameLayout mLlDetailBottom;
    @BindView(R.id.fab_like)
    FloatingActionButton mFabLike;
    @BindView(R.id.nsv_scroller)
    NestedScrollView mNsvScroller;

    int id = 0;
    int allNum = 0;
    int shortNum = 0;
    int longNum = 0;
    String shareUrl;
    String imgUrl;
    boolean isBottomShow = true;
    boolean isImageShow = false;
    boolean isTransitionEnd = false;
    BlurTransformation mBlurTransformation;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_zhihu_detail;
    }

    @Override
    protected void initEventAndData() {
        setToolBar(mViewToolbar, "");
        Intent intent = getIntent();
        id = intent.getExtras().getInt("id");
        mPresenter.queryLikeData(id);
        mPresenter.getDetailData(id);
        mPresenter.getExtraData(id);
        mViewLoading.start();
        mBlurTransformation = new BlurTransformation(mContext);
        WebSettings settings = mWvDetailContent.getSettings();
        if (SharedPreferenceUtil.getNoImageState()) {
            settings.getBlockNetworkImage();
        }
        if (SharedPreferenceUtil.getAutoCacheState()) {
            settings.setAppCacheEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setDatabaseEnabled(true);
            if (SystemUtil.isNetworkConnected()) {
                settings.setCacheMode(WebSettings.LOAD_DEFAULT);
            } else {
                settings.setCacheMode(WebSettings.LOAD_CACHE_ONLY);
            }
        }
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);
        mWvDetailContent.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                webView.loadUrl(url);
                return true;
            }
        });
        mNsvScroller.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY - oldScrollY > 0 && isBottomShow) {  //下移隐藏
                    isBottomShow = false;
                    mLlDetailBottom.animate().translationY(mLlDetailBottom.getHeight());
                } else if (scrollY - oldScrollY < 0 && !isBottomShow) {  //上移出现
                    isBottomShow = true;
                    mLlDetailBottom.animate().translationY(0);
                }
            }
        });

    }

    @Override
    public void showContent(ZhihuDetailBean zhihuDetailBean) {
        mViewLoading.stop();
        imgUrl = zhihuDetailBean.getImage();
        shareUrl = zhihuDetailBean.getShare_url();
        if (!isImageShow && isTransitionEnd) {
            ImageLoader.load(mContext,zhihuDetailBean.getImage(),new BlurTransformation(mContext,5),mDetailBarImage);
        }
        (getWindow().getSharedElementEnterTransition()).addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
            }
            @Override
            public void onTransitionEnd(Transition transition) {
                /**
                 * 测试发现部分手机(如红米note2)上加载图片会变形,没有达到centerCrop效果
                 * 查阅资料发现Glide配合SharedElementTransition是有坑的,需要在Transition动画结束后再加载图片
                 * https://github.com/TWiStErRob/glide-support/blob/master/src/glide3/java/com/bumptech/glide/supportapp/github/_847_shared_transition/DetailFragment.java
                 */


            }
            @Override
            public void onTransitionCancel(Transition transition) {
            }
            @Override
            public void onTransitionPause(Transition transition) {
            }
            @Override
            public void onTransitionResume(Transition transition) {
            }
        });
        mClpToolbar.setTitle(zhihuDetailBean.getTitle());
        mDetailBarCopyright.setText(zhihuDetailBean.getImage_soure());
        String htmlData = HtmlUtil.createHtmlData(zhihuDetailBean.getBody(), zhihuDetailBean.getCss(), zhihuDetailBean.getJs());
        mWvDetailContent.loadData(htmlData, HtmlUtil.MIME_TYPE, HtmlUtil.ENCODING);
    }

    @Override
    public void showExtraInfo(DetailExtraBean detailExtraBean) {
        mViewLoading.stop();
        mTvDetailBottomLike.setText(String.format("%d个赞", detailExtraBean.getPopularity()));
        mTvDetailBottomComment.setText(String.format("%d条评论",detailExtraBean.getComments()));
        allNum = detailExtraBean.getComments();
        shortNum = detailExtraBean.getShort_comments();
        longNum = detailExtraBean.getLong_comments();
        isTransitionEnd = true;
        if (imgUrl != null) {
            isImageShow = true;
            ImageLoader.load(mContext, imgUrl, new BlurTransformation(mContext,5), mDetailBarImage);
        }
    }

    @Override
    public void setLikeButtonState(boolean isLiked) {
        mFabLike.setSelected(isLiked);
    }

    @Override
    public void showError(String msg) {
        mViewLoading.stop();
        SnackbarUtil.showShort(getWindow().getDecorView(), msg);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWvDetailContent.canGoBack()) {
            mWvDetailContent.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick(R.id.fab_like)
    public void likeArticle() {
        if (mFabLike.isSelected()) {
            mFabLike.setSelected(false);
            mPresenter.deleteLikeData();
            SnackbarUtil.showShort(mViewToolbar,"取消收藏");
        } else {
            mFabLike.setSelected(true);
            mPresenter.insertLikeData();
            SnackbarUtil.showShort(mViewToolbar,"已收藏");
        }

    }


    @OnClick(R.id.tv_detail_bottom_comment)
    public void gotoComment() {
        Intent intent = getIntent();
        intent.setClass(this,CommentActivity.class);
        intent.putExtra("id",id);
        intent.putExtra("allNum",allNum);
        intent.putExtra("short",shortNum);
        intent.putExtra("long",longNum);
        startActivity(intent);
    }

    @OnClick(R.id.tv_detail_bottom_share)
    public void shareUrl() {
        ShareUtil.shareText(mContext,shareUrl,"分享一篇文章");
    }

    @Override
    public void onBackPressedSupport() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            pop();
        } else {
            finishAfterTransition();
        }
    }

}
