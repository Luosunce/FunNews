package com.yone.funnews.di.component;

import com.yone.funnews.app.App;
import com.yone.funnews.di.ContextLife;
import com.yone.funnews.di.module.AppModule;
import com.yone.funnews.model.db.RealmHelper;
import com.yone.funnews.model.http.RetrofitHelper;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Yoe on 2016/10/8.
 */

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    @ContextLife("Application")
    App getContext(); //提供App的Context

    RetrofitHelper retrofitHelper(); //提供http的帮助类

    RealmHelper realmHelper();
}
