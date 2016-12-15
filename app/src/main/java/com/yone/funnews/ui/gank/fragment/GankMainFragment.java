package com.yone.funnews.ui.gank.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.yone.funnews.R;
import com.yone.funnews.app.Constants;
import com.yone.funnews.base.SimpleFragment;
import com.yone.funnews.component.RxBus;
import com.yone.funnews.model.been.SearchEvent;
import com.yone.funnews.presenter.TechPresenter;
import com.yone.funnews.ui.gank.adapter.GankMainAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Yoe on 2016/10/11.
 */

public class GankMainFragment extends SimpleFragment {

    @BindView(R.id.tab_gank_main)
    TabLayout mTabGankMain;
    @BindView(R.id.vp_gank_main)
    ViewPager mVpGankMain;

    String[] tabTitle = new String[]{"Android","iOS","Web","福利"};
    List<Fragment> fragments = new ArrayList<>();

    GankMainAdapter mAdapter;
    TechFragment androidFragment;
    TechFragment iOSFragment;
    TechFragment webFragment;
    GirlFragment girlFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gank_main;
    }

    @Override
    protected void initEventAndData() {
        androidFragment = new TechFragment();
        iOSFragment = new TechFragment();
        webFragment = new TechFragment();
        girlFragment = new GirlFragment();
        Bundle androidBundle = new Bundle();
        androidBundle.putString("tech", TechPresenter.TECH_ANDROID);
        androidFragment.setArguments(androidBundle);
        Bundle iosBundle = new Bundle();
        iosBundle.putString("tech",TechPresenter.TECH_IOS);
        iOSFragment.setArguments(iosBundle);
        Bundle webBundle = new Bundle();
        webBundle.putString("tech",TechPresenter.TECH_WEB);
        webFragment.setArguments(webBundle);

        fragments.add(androidFragment);
        fragments.add(iOSFragment);
        fragments.add(webFragment);
        fragments.add(girlFragment);
        mAdapter = new GankMainAdapter(getChildFragmentManager(),fragments);
        mVpGankMain.setAdapter(mAdapter);

        //TabLayout配合ViewPager有时会出现不显示Tab文字的Bug,需要按如下顺序
        mTabGankMain.addTab(mTabGankMain.newTab().setText(tabTitle[0]));
        mTabGankMain.addTab(mTabGankMain.newTab().setText(tabTitle[1]));
        mTabGankMain.addTab(mTabGankMain.newTab().setText(tabTitle[2]));
        mTabGankMain.addTab(mTabGankMain.newTab().setText(tabTitle[3]));
        mTabGankMain.setupWithViewPager(mVpGankMain);
        mTabGankMain.getTabAt(0).setText(tabTitle[0]);
        mTabGankMain.getTabAt(1).setText(tabTitle[1]);
        mTabGankMain.getTabAt(2).setText(tabTitle[2]);
        mTabGankMain.getTabAt(3).setText(tabTitle[3]);
    }

    public void doSearch(String query){
        switch (mVpGankMain.getCurrentItem()){
            case 0:
                RxBus.getDefault().post(new SearchEvent(query, Constants.TYPE_ANDROID));
                break;
            case 1:
                RxBus.getDefault().post(new SearchEvent(query,Constants.TYPE_IOS));
                break;
            case 2:
                RxBus.getDefault().post(new SearchEvent(query,Constants.TYPE_WEB));
                break;
            case 3:
                RxBus.getDefault().post(new SearchEvent(query,Constants.TYPE_GIRL));
                break;
        }
    }
}



