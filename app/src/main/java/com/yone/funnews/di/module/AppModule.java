package com.yone.funnews.di.module;

import com.yone.funnews.app.App;
import com.yone.funnews.di.ContextLife;
import com.yone.funnews.model.db.RealmHelper;
import com.yone.funnews.model.http.RetrofitHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Yoe on 2016/10/8.
 */

@Module
public class AppModule {
    private final App application;

    public AppModule(App application){ this.application =application; }

    @Provides
    @Singleton
    @ContextLife("Application")
    App provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    RetrofitHelper provideRetrofitHelper() {
        return new RetrofitHelper();
    }

    @Provides
    @Singleton
    RealmHelper provideRealmHelper() {
        return new RealmHelper(application);
    }
}
