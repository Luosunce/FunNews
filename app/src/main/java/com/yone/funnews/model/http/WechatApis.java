package com.yone.funnews.model.http;

import com.yone.funnews.model.been.WechatItemBean;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Yoe on 2016/10/24.
 */

public interface WechatApis {

    String HOST = "http://api.tianapi.com/";

    /**
     * 微信精选列表
     */
    @GET("wxnew")
    Observable<WXHttpResponse<List<WechatItemBean>>> getWechatHot(@Query("key") String key,@Query("num") int num,@Query("page") int page);

    /**
     * 微信搜索列表
     */
    @GET("wxnew")
    Observable<WXHttpResponse<List<WechatItemBean>>> getWechatHotSearch(@Query("key") String key,@Query("num") int num,@Query("page") int page,@Query("word") String word);
}
