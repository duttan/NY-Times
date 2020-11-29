package com.example.nytimes.viewmodel;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.nytimes.data.ApiResponse;
import com.example.nytimes.data.SearchApiResponse;
import com.example.nytimes.data.api.NewsApi;
import com.example.nytimes.data.api.NewsService;
import com.example.nytimes.di.DaggerApiComponent;

import org.w3c.dom.Text;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ListViewModel extends ViewModel {

    @Inject
    NewsService newsService;

    public MutableLiveData<ApiResponse> feeds = new MutableLiveData<ApiResponse>();
    public MutableLiveData<SearchApiResponse> searchfeeds = new MutableLiveData<SearchApiResponse>();

    public ListViewModel() {
        super();
        DaggerApiComponent.create().inject(this);
    }

    public void fetchArticles()
    {
        newsService.getNewsArticles()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<ApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        feeds.setValue(apiResponse);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("@@apifailure",e.getLocalizedMessage());


                    }
                });
    }

    public void fetchSearchArticles(String query)
    {

        newsService.getSearchArticles(query)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<SearchApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(SearchApiResponse apiResponse) {
                        searchfeeds.setValue(apiResponse);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("@@apifailure",e.getLocalizedMessage());


                    }
                });

    }



}
