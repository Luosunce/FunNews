package com.yone.funnews.ui.main.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.yone.funnews.R;
import com.yone.funnews.app.App;
import com.yone.funnews.app.Constants;
import com.yone.funnews.base.BaseActivity;
import com.yone.funnews.component.ImageLoader;
import com.yone.funnews.component.RxBus;
import com.yone.funnews.model.been.SearchEvent;
import com.yone.funnews.presenter.MainPresenter;
import com.yone.funnews.presenter.contract.MainContract;
import com.yone.funnews.ui.gank.fragment.GankMainFragment;
import com.yone.funnews.ui.main.fragment.AboutFragment;
import com.yone.funnews.ui.main.fragment.LikeFragment;
import com.yone.funnews.ui.main.fragment.SettingFragment;
import com.yone.funnews.ui.wechat.framgent.WechatMainFragment;
import com.yone.funnews.ui.zhihu.fragment.ZhihuMainFragment;
import com.yone.funnews.util.SharedPreferenceUtil;
import com.yone.funnews.util.SnackbarUtil;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import me.yokeyword.fragmentation.SupportFragment;

import static com.yone.funnews.app.Constants.TYPE_GANK;
import static com.yone.funnews.app.Constants.TYPE_WECHAT;
import static com.yone.funnews.app.Constants.TYPE_ZHIHU;

public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {


    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.view_search)
    MaterialSearchView mSearchView;
    @BindView(R.id.fl_main_content)
    FrameLayout mFlMainContent;
    @BindView(R.id.navigationView)
    NavigationView mNavigationView;
    @BindView(R.id.activity_main)
    RelativeLayout mActivityMain;
    @BindView(R.id.drawer)
    DrawerLayout mDrawerLayout;
    ImageView mIvHeaderBg;

    ActionBarDrawerToggle mDrawerToggle;
    ZhihuMainFragment mZhihuMainFragment;
    GankMainFragment mGankMainFragment;
    WechatMainFragment mWechatMainFragment;
    LikeFragment mLikeFragment;
    SettingFragment mSettingFragment;
    AboutFragment mAboutFragment;
    MenuItem mLastMenuItem;
    MenuItem mSearchMenuItem;

    private int hideFragment = TYPE_ZHIHU;
    private int showFragment = TYPE_ZHIHU;
    private String HeaderBg;


    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    /**
     * 由于recreate 需要特殊处理夜间模式
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            SharedPreferenceUtil.setNightModeState(false);
        } else {
            showFragment = SharedPreferenceUtil.getCurrentItem();
            hideFragment = Constants.TYPE_ZHIHU;
            showHideFragment(getTargetFragment(showFragment),getTargetFragment(hideFragment));
            mNavigationView.getMenu().findItem(R.id.drawer_zhihu).setChecked(false);
            hideFragment = showFragment;
        }

    }
    @Override
    protected void initEventAndData() {
        setToolBar(mToolBar,"知乎");
        mZhihuMainFragment = new ZhihuMainFragment();
        mGankMainFragment = new GankMainFragment();
        mWechatMainFragment = new WechatMainFragment();
        mLikeFragment = new LikeFragment();
        mSettingFragment = new SettingFragment();
        mAboutFragment = new AboutFragment();
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,mToolBar,R.string.drawer_open,R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mLastMenuItem = mNavigationView.getMenu().findItem(R.id.drawer_zhihu);
        HeaderBg = getIntent().getStringExtra("HeaderImg");
        View view = mNavigationView.getHeaderView(0);
        mIvHeaderBg = (ImageView) view.findViewById(R.id.header_image);
        ImageLoader.load(mContext,HeaderBg,new BlurTransformation(mContext,5),mIvHeaderBg);
        loadMultipleRootFragment(R.id.fl_main_content,0,mZhihuMainFragment,mGankMainFragment,mWechatMainFragment,mLikeFragment,mSettingFragment,mAboutFragment);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.drawer_zhihu:
                        showFragment = Constants.TYPE_ZHIHU;
                        mSearchMenuItem.setVisible(false);
                        break;
                    case R.id.drawer_gank:
                        showFragment = Constants.TYPE_GANK;
                        mSearchMenuItem.setVisible(true);
                        break;
                    case R.id.drwaer_wechat:
                        showFragment = Constants.TYPE_WECHAT;
                        mSearchMenuItem.setVisible(true);
                        break;
                    case R.id.drawer_setting:
                        showFragment = Constants.TYPE_SETTING;
                        mSearchMenuItem.setVisible(false);
                        break;
                    case R.id.drawer_like:
                        showFragment = Constants.TYPE_LIKE;
                        mSearchMenuItem.setVisible(false);
                        break;
                    case R.id.drawer_about:
                        showFragment = Constants.TYPE_ABOUT;
                        mSearchMenuItem.setVisible(false);
                        break;
                }
                if (mLastMenuItem != null){
                    mLastMenuItem.setChecked(false);
                }
                mLastMenuItem = item;
                SharedPreferenceUtil.setCurrentItem(showFragment);
                item.setChecked(true);
                mToolBar.setTitle(item.getTitle());
                mDrawerLayout.closeDrawers();
                showHideFragment(getTargetFragment(showFragment),getTargetFragment(hideFragment));
                hideFragment = showFragment;
                return true;
            }
        });

        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (showFragment == TYPE_GANK) {
                    mGankMainFragment.doSearch(query);
                }else if(showFragment == TYPE_WECHAT) {
                    RxBus.getDefault().post(new SearchEvent(query, TYPE_WECHAT));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(false);
        mSearchView.setMenuItem(item);
        mSearchMenuItem = item;
        return true;
    }

    @Override
    public void showError(String msg) {
        SnackbarUtil.showShort(mToolBar,msg);
    }

    @Override
    public void onBackPressedSupport() {
        if (mSearchView.isSearchOpen()){
            mSearchView.closeSearch();
        } else
            showExitDialog();
    }

    private void showExitDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("确定退出FunNews吗？");
        builder.setNegativeButton("取消",null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                App.getInstance().exitApp();
            }
        });
        builder.show();
    }
    private SupportFragment getTargetFragment(int item) {
        switch (item) {
            case Constants.TYPE_ZHIHU:
                return mZhihuMainFragment;
            case Constants.TYPE_GANK:
                return mGankMainFragment;
            case Constants.TYPE_WECHAT:
                return mWechatMainFragment;
            case Constants.TYPE_LIKE:
                return mLikeFragment;
            case Constants.TYPE_SETTING:
                return mSettingFragment;
            case Constants.TYPE_ABOUT:
                return mAboutFragment;
        }
        return mZhihuMainFragment;
    }

}
