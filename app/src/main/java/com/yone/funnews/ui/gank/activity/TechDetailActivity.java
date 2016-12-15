package com.yone.funnews.ui.gank.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.victor.loading.rotate.RotateLoading;
import com.yone.funnews.R;
import com.yone.funnews.app.App;
import com.yone.funnews.base.SimpleActivity;
import com.yone.funnews.model.been.RealmLikeBean;
import com.yone.funnews.model.db.RealmHelper;
import com.yone.funnews.presenter.TechPresenter;
import com.yone.funnews.util.ShareUtil;
import com.yone.funnews.util.SharedPreferenceUtil;
import com.yone.funnews.util.SnackbarUtil;
import com.yone.funnews.util.SystemUtil;

import butterknife.BindView;

/**
 * Created by Yoe on 2016/10/23.
 */

public class TechDetailActivity extends SimpleActivity {

    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.wv_tech_content)
    WebView mWvTechContent;
    @BindView(R.id.view_loading)
    RotateLoading mViewLoading;

    RealmHelper mRealmHelper;
    MenuItem mMenuItem;
    String title,url,id,tech;
    boolean isLiked;

    @Override
    protected int getLayout() {
        return R.layout.activity_tech_detail;
    }

    @Override
    protected void initEventAndData() {
        mRealmHelper = App.getAppComponent().realmHelper();
        Intent intent = getIntent();
        tech = intent.getStringExtra("tech");
        title = intent.getStringExtra("title");
        url = intent.getStringExtra("url");
        id = intent.getStringExtra("id");
        setToolBar(mToolBar,title);
        WebSettings settings = mWvTechContent.getSettings();
        if (SharedPreferenceUtil.getNoImageState()){
            settings.setBlockNetworkImage(true);
        }
        if (SharedPreferenceUtil.getAutoCacheState()){
            settings.setAppCacheEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setDatabaseEnabled(true);
            if (SystemUtil.isNetworkConnected()){
                settings.setCacheMode(WebSettings.LOAD_DEFAULT);
            } else {
                settings.setCacheMode(WebSettings.LOAD_CACHE_ONLY);
            }
        }
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);
        mWvTechContent.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                webView.loadUrl(url);
                return true;
            }
        });
        mWvTechContent.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView webView, int newProgress) {
                super.onProgressChanged(webView, newProgress);
                try{
                    if (newProgress == 100) {
                        mViewLoading.stop();
                    } else {
                        if (!mViewLoading.isStart()) {
                            mViewLoading.start();
                        }
                    }
                }catch (Exception e){

                }

            }

            @Override
            public void onReceivedTitle(WebView webView, String title) {
                super.onReceivedTitle(webView, title);
                setTitle(title);
            }
        });
        mWvTechContent.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWvTechContent.canGoBack()){
            mWvTechContent.canGoBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tech_menu,menu);
        mMenuItem = menu.findItem(R.id.action_like);
        setLikeState(mRealmHelper.queryLikeId(id));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_like:
                if (isLiked) {
                    item.setIcon(R.mipmap.ic_toolbar_like_n);
                    mRealmHelper.deleteLikeBean(this.id);
                    isLiked = false;
                    SnackbarUtil.showShort(mToolBar,"取消收藏");
                } else {
                    item.setIcon(R.mipmap.ic_toolbar_like_p);
                    RealmLikeBean bean = new RealmLikeBean();
                    bean.setId(this.id);
                    bean.setTitle(title);
                    bean.setImage(url);
                    bean.setType(TechPresenter.getTechType(tech));
                    bean.setTime(System.currentTimeMillis());
                    mRealmHelper.insertLikeBean(bean);
                    isLiked = true;
                    SnackbarUtil.showShort(mToolBar,"已收藏");
                }
                break;
            case R.id.action_copy:
                SystemUtil.copyToClipBoard(mContext,url);
                break;
            case R.id.action_share:
                ShareUtil.shareText(mContext,url,"分享一篇文章");
        }
        return super.onOptionsItemSelected(item);
    }

    private void setLikeState(boolean state){
        if (state){
            mMenuItem.setIcon(R.mipmap.ic_toolbar_like_p);
            isLiked = true;
        } else {
            mMenuItem.setIcon(R.mipmap.ic_toolbar_like_n);
            isLiked = false;
        }
    }

    @Override
    public void onBackPressedSupport() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1){
            pop();
        } else {
            finishAfterTransition();
        }
    }
}
