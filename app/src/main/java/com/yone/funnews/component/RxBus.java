package com.yone.funnews.component;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by Yoe on 2016/10/11.
 */

public class RxBus {

    //主题
    private final Subject<Object,Object> bus;

    private RxBus(){
        bus = new SerializedSubject<>(PublishSubject.create());
    }

    public static RxBus getDefault(){ return RxBusHolde.sInstance; }

    private static class RxBusHolde{
        private static final RxBus sInstance = new RxBus();
    }

    //提供了一个新事件
    public void post (Object o){ bus.onNext(o);}

    //根据传递的eventType 类型返回特定类型<eventType>的观察者
    public <T>Observable<T> toObservable (Class<T> eventType) { return bus.ofType(eventType); }
}
