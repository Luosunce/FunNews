package com.yone.funnews.di;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * Created by Yoe on 2016/10/8.
 */

@Scope
@Retention(RUNTIME)
public @interface ActivityScope {
}
