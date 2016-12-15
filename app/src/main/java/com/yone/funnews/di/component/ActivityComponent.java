package com.yone.funnews.di.component;

import android.app.Activity;

import com.yone.funnews.di.ActivityScope;
import com.yone.funnews.di.module.ActivityModule;
import com.yone.funnews.ui.main.activity.MainActivity;
import com.yone.funnews.ui.main.activity.WelcomeActivity;
import com.yone.funnews.ui.zhihu.activity.SectionActivity;
import com.yone.funnews.ui.zhihu.activity.ThemeActivity;
import com.yone.funnews.ui.zhihu.activity.ZhihuDetailActivity;

import dagger.Component;

/**
 * Created by Yoe on 2016/10/9.
 */
@ActivityScope
@Component(dependencies = AppComponent.class,modules = ActivityModule.class)
public interface ActivityComponent {

    Activity getActivity();

    void inject(MainActivity mainActivity);

    void inject(WelcomeActivity welcomeActivity);

    void inject(ZhihuDetailActivity zhihuDetailActivity);

    void inject(ThemeActivity themeActivity);

    void inject(SectionActivity sectionActivity);
}
