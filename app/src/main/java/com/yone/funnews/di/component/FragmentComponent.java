package com.yone.funnews.di.component;

import android.app.Activity;

import com.yone.funnews.di.FragmentScope;
import com.yone.funnews.di.module.FragmentModule;
import com.yone.funnews.ui.gank.fragment.GirlFragment;
import com.yone.funnews.ui.gank.fragment.TechFragment;
import com.yone.funnews.ui.main.fragment.LikeFragment;
import com.yone.funnews.ui.wechat.framgent.WechatMainFragment;
import com.yone.funnews.ui.zhihu.fragment.CommentFragment;
import com.yone.funnews.ui.zhihu.fragment.DailyFragment;
import com.yone.funnews.ui.zhihu.fragment.HotFragment;
import com.yone.funnews.ui.zhihu.fragment.SectionFragment;
import com.yone.funnews.ui.zhihu.fragment.ThemeFragment;

import dagger.Component;

/**
 * Created by Yoe on 2016/10/9.
 */
@FragmentScope
@Component(dependencies = AppComponent.class,modules = FragmentModule.class)
public interface FragmentComponent {

    Activity getActivity();

    void inject(DailyFragment dailyFragment);
    void inject(CommentFragment commentFragment);
    void inject(ThemeFragment themeFragment);
    void inject(SectionFragment sectionFragment);
    void inject(HotFragment hotFragment);
    void inject(TechFragment techFragment);
    void inject(GirlFragment girlFragment);
    void inject(WechatMainFragment wechatMainFragment);
    void inject(LikeFragment likeFragment);
}
