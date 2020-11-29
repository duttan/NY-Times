package com.example.nytimes.data.api;

import com.example.nytimes.data.ApiResponse;
import com.example.nytimes.data.SearchApiResponse;
import com.example.nytimes.di.DaggerApiComponent;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class NewsService {

    private static NewsService instance;

    @Inject
    public NewsApi api;

    private NewsService() {
        DaggerApiComponent.create().inject(this);
    }

    public static NewsService getInstance() {
        if(instance == null) {
            instance = new NewsService();
        }
        return instance;
    }

    public Single<ApiResponse> getNewsArticles() {
        return api.getNewsArticles();
    }

    public Single<SearchApiResponse> getSearchArticles(String query)
    {
        return api.getSearchArticles(query);
    }
}

