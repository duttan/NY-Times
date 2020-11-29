package com.example.nytimes.di;

import com.example.nytimes.BuildConfig;
import com.example.nytimes.data.api.NewsApi;
import com.example.nytimes.data.api.NewsService;
import com.example.nytimes.utils.Util;
import com.example.nytimes.view.MainActivity;
import com.example.nytimes.view.MainApplication;

import java.io.File;
import java.util.logging.Level;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

    @Module
    public class ApiModule {

        private static final String BASE_URL = "https://api.nytimes.com";

        @Provides
        public NewsApi provideNewsApi() {

            Interceptor apiKeyInterceptor = chain -> {
                Request request = chain.request();
                HttpUrl url = request.url().newBuilder().addQueryParameter("api-key", BuildConfig.API_KEY).build();
                request = request.newBuilder().url(url).build();
                return chain.proceed(request);
            };

            Interceptor offlineCacheInterceptor = chain -> {
                Request request = chain.request();
                if(Util.hasNetwork()) {
                    request = request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build(); }
                else {
                    request = request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build();
                }

                return chain.proceed(request);
            };

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.level(HttpLoggingInterceptor.Level.BODY);

            File httpCacheDirectory = new File(MainApplication.context.getCacheDir(), "offlineCache");
            //10 MB
            Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);


            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(apiKeyInterceptor)
                    .addInterceptor(logging)
                    .addInterceptor(offlineCacheInterceptor)
                    .cache(cache)
                    .build();

            return new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build()
                    .create(NewsApi.class);
        }

        @Provides
        public NewsService provideNewsService() {
            return NewsService.getInstance();
        }

    }

