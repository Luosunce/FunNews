package com.yone.funnews.di.module;



import android.app.Activity;
import android.support.v4.app.Fragment;

import com.yone.funnews.di.FragmentScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yoe on 2016/10/8.
 */

@Module
public class FragmentModule {

    private Fragment fragment;

    public FragmentModule(Fragment fragment){
        this.fragment = fragment;
    }

    @Provides
    @FragmentScope
    public Activity provideActivity(){
        return fragment.getActivity();
    }

}
