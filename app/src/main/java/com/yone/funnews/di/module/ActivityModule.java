package com.yone.funnews.di.module;

import android.app.Activity;

import com.yone.funnews.di.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yoe on 2016/10/8.
 */
@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity){this.mActivity = activity;}

    @Provides
    @ActivityScope
    public Activity provideActivity(){return mActivity;}
}
