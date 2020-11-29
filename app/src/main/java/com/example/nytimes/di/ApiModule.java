package com.example.nytimes.di;

import com.example.nytimes.BuildConfig;
import com.example.nytimes.data.api.NewsApi;
import com.example.nytimes.data.api.NewsService;
import com.example.nytimes.view.MainActivity;
import com.example.nytimes.view.MainApplication;

import java.util.logging.Level;

import dagger.Module;
import dagger.Provides;
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



            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.level(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(apiKeyInterceptor)
                    .addInterceptor(logging)
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

