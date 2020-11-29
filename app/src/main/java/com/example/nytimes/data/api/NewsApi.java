package com.example.nytimes.data.api;

import com.example.nytimes.data.ApiResponse;
import com.example.nytimes.data.SearchApiResponse;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApi{

    @GET("/svc/news/v3/content/all/all.json")
    Single<ApiResponse> getNewsArticles();

    @GET("/svc/search/v2/articlesearch.json")
    Single<SearchApiResponse> getSearchArticles(@Query(value = "q") String query);




}
