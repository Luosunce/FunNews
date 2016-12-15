package com.yone.funnews.ui.gank.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.yone.funnews.R;
import com.yone.funnews.app.App;
import com.yone.funnews.app.Constants;
import com.yone.funnews.base.SimpleActivity;
import com.yone.funnews.model.been.RealmLikeBean;
import com.yone.funnews.model.db.RealmHelper;
import com.yone.funnews.util.ShareUtil;
import com.yone.funnews.util.SnackbarUtil;
import com.yone.funnews.util.SystemUtil;

import butterknife.BindView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Yoe on 2016/10/23.
 */

public class GirlDetailActivity extends SimpleActivity {

    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.iv_girl_detail)
    ImageView mIvGirlDetail;

    Bitmap mBitmap;
    RealmHelper mRealmHelper;
    PhotoViewAttacher mAttacher;
    MenuItem mMenuItem;

    private String url;
    private String id;
    private boolean isLiked;

    @Override
    protected int getLayout() {
        return R.layout.activity_girl_detail;
    }

    @Override
    protected void initEventAndData() {
        setToolBar(mToolBar, "");
        mRealmHelper = App.getAppComponent().realmHelper();
        Intent intent = getIntent();
        url = intent.getExtras().getString("url");
        id = intent.getExtras().getString("id");
        if (url != null) {
            Glide.with(mContext).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    mBitmap = resource;
                    mIvGirlDetail.setImageBitmap(resource);
                    mAttacher = new PhotoViewAttacher(mIvGirlDetail);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.girl_menu, menu);
        mMenuItem = menu.findItem(R.id.action_like);
        setLikeState(mRealmHelper.queryLikeId(id));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_like:
                if (isLiked) {
                    item.setIcon(R.mipmap.ic_toolbar_like_n);
                    mRealmHelper.deleteLikeBean(this.id);
                    isLiked = false;
                    SnackbarUtil.showShort(mToolBar, "取消收藏");
                } else {
                    item.setIcon(R.mipmap.ic_toolbar_like_p);
                    RealmLikeBean bean = new RealmLikeBean();
                    bean.setId(this.id);
                    bean.setImage(url);
                    bean.setType(Constants.TYPE_GIRL);
                    bean.setTime(System.currentTimeMillis());
                    mRealmHelper.insertLikeBean(bean);
                    isLiked = true;
                    SnackbarUtil.showShort(mToolBar, "漂亮妹子已收藏");
                }
                break;
            case R.id.action_save:
                SystemUtil.saveBitmapToFile(mContext, url, mBitmap, mIvGirlDetail, false);
                break;
            case R.id.action_share:
                ShareUtil.shareImage(mContext, SystemUtil.saveBitmapToFile(mContext, url, mBitmap, mIvGirlDetail, true), "分享一只妹子");
        }
        return super.onOptionsItemSelected(item);
    }

    private void setLikeState(boolean state) {
        if (state) {
            mMenuItem.setIcon(R.mipmap.ic_toolbar_like_p);
            isLiked = true;
        } else {
            mMenuItem.setIcon(R.mipmap.ic_toolbar_like_n);
            isLiked = false;
        }
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
