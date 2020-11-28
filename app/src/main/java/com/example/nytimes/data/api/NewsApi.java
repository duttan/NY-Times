package com.example.nytimes.data.api;

import com.example.nytimes.data.ApiResponse;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface NewsApi{

    @GET("/svc/news/v3/content/all/all.json")
    Single<ApiResponse> getNewsArticles();
}
