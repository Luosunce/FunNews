package com.yone.funnews.model.http;


import android.support.design.BuildConfig;

import com.yone.funnews.app.Constants;
import com.yone.funnews.model.been.CommentBean;
import com.yone.funnews.model.been.DailyBeforeListBean;
import com.yone.funnews.model.been.DailyListBean;
import com.yone.funnews.model.been.DetailExtraBean;
import com.yone.funnews.model.been.GankItemBean;
import com.yone.funnews.model.been.GankSearchItemBean;
import com.yone.funnews.model.been.HotListBean;
import com.yone.funnews.model.been.SectionChildListBean;
import com.yone.funnews.model.been.SectionListBean;
import com.yone.funnews.model.been.ThemeChildListBean;
import com.yone.funnews.model.been.ThemeListBean;
import com.yone.funnews.model.been.WechatItemBean;
import com.yone.funnews.model.been.WelcomeBean;
import com.yone.funnews.model.been.ZhihuDetailBean;
import com.yone.funnews.util.SystemUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;


/**
 * Created by Yoe on 2016/10/9.
 */

public class RetrofitHelper {

    private static OkHttpClient okHttpClient = null;
    private static ZhihuApis zhihuApiService = null;
    private static GankApis gankApiService = null;
    private static WechatApis wechatApiService = null;

   private void init(){
       initOkHttp();
       zhihuApiService = getZhihuApiService();
       gankApiService = getGankApiService();
       wechatApiService = getWechatApiService();
   }

    public RetrofitHelper(){
        init();
    }

    private static void initOkHttp(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG){
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            builder.addInterceptor(loggingInterceptor);
        }

        File cacheFile = new File(Constants.PATH_CACHE);
        Cache cache = new Cache(cacheFile,1024 * 1024 * 50);
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!SystemUtil.isNetworkConnected()){
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response response = chain.proceed(request);
                if (SystemUtil.isNetworkConnected()) {
                    int maxAge = 0;
                    //有网络时不缓存，最大保存时长为0
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma")
                            .build();
                } else {
                    //无网络时，设置超时为4周
                    int maxStale = 60 * 60 * 24 * 28;
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                }
                return response;
            }
        };
        //设置缓存
        builder.addNetworkInterceptor(cacheInterceptor);
        builder.addInterceptor(cacheInterceptor);
        builder.cache(cache);
        //设置超时
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.connectTimeout(20,TimeUnit.SECONDS);
        builder.connectTimeout(20,TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        okHttpClient = builder.build();
    }

    private static ZhihuApis getZhihuApiService(){
        Retrofit zhihuRetrofit = new Retrofit.Builder()
                .baseUrl(ZhihuApis.HOST)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return zhihuRetrofit.create(ZhihuApis.class);
    }

    private static GankApis getGankApiService(){
         Retrofit gankRetrofit = new Retrofit.Builder()
                 .baseUrl(GankApis.HOST)
                 .client(okHttpClient)
                 .addConverterFactory(GsonConverterFactory.create())
                 .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                 .build();
        return gankRetrofit.create(GankApis.class);
    }

    private static WechatApis getWechatApiService(){
        Retrofit gankRetrofit = new Retrofit.Builder()
                .baseUrl(WechatApis.HOST)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return gankRetrofit.create(WechatApis.class);
    }

    public Observable<WelcomeBean> fechWelcomeInfo(String res){
        return zhihuApiService.getWelcomeInfo(res);
    }

    public Observable<DailyListBean> fetchDailyListInfo(){
        return zhihuApiService.getDailyList();
    }

    public Observable<DailyBeforeListBean> fechDailyBeforeListInfo(String date){
        return zhihuApiService.getDailyBeforeList(date);
    }

    public Observable<ZhihuDetailBean> fetchDetailyInfo(int id){
        return zhihuApiService.getDeailInfo(id);
    }

    public Observable<DetailExtraBean> fetchDetailExtraInfo(int id){
        return zhihuApiService.getDetailExtraInfo(id);
    }

    public Observable<CommentBean> fetchLongCommentInfo(int id){
        return zhihuApiService.getLongCommentInfo(id);
    }

    public Observable<CommentBean> fetchShortCommentInfo(int id){
        return zhihuApiService.getShortCommentInfo(id);
    }

    public Observable<ThemeListBean> fetchDailyThemeListInfo(){
        return zhihuApiService.getThemeList();
    }

    public Observable<ThemeChildListBean> fetchThemeChildListInfo(int id){
        return zhihuApiService.getThemeChildList(id);
    }

    public Observable<SectionListBean> fecthSectionListInfo(){
        return zhihuApiService.getSectionList();
    }

    public Observable<SectionChildListBean> fecthSectionChildListInfo(int id){
        return zhihuApiService.getSectionChildList(id);
    }

    public Observable<HotListBean> fecthHotListInfo(){
        return zhihuApiService.getHotList();
    }

    public Observable<GankHttpResponse<List<GankSearchItemBean>>> fetchGankSearchList(String query,String type,int num,int page){
        return gankApiService.getSearchList(query,type,num,page);
    }

    public Observable<GankHttpResponse<List<GankItemBean>>> fetchTechList(String tech,int num,int page){
        return gankApiService.getTechList(tech,num,page);
    }

    public Observable<GankHttpResponse<List<GankItemBean>>> fetchRandomGirl(int num){
        return gankApiService.getRandomGirl(num);
    }

    public Observable<GankHttpResponse<List<GankItemBean>>> fetchGirlList(int num,int page){
        return gankApiService.getGirlList(num,page);
    }

    public Observable<WXHttpResponse<List<WechatItemBean>>> fetchWechatListInfo(int num,int page){
        return wechatApiService.getWechatHot(Constants.KEY_API,num,page);
    }

    public Observable<WXHttpResponse<List<WechatItemBean>>> fetchWechatSearchListInfo(int num,int page,String word){
        return wechatApiService.getWechatHotSearch(Constants.KEY_API,num,page,word);
    }
}
