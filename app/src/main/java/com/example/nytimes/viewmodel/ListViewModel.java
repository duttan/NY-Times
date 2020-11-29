package com.example.nytimes.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.nytimes.data.ApiResponse;
import com.example.nytimes.data.SearchApiResponse;
import com.example.nytimes.data.api.NewsService;
import com.example.nytimes.di.DaggerApiComponent;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ListViewModel extends ViewModel {

    @Inject
    NewsService newsService;

    public MutableLiveData<ApiResponse> feeds = new MutableLiveData<ApiResponse>();
    public MutableLiveData<SearchApiResponse> searchfeeds = new MutableLiveData<SearchApiResponse>();
    public MutableLiveData<Boolean> newsLoadError = new MutableLiveData<Boolean>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<Boolean>();
    private CompositeDisposable disposable = new CompositeDisposable();

    public ListViewModel() {
        super();
        DaggerApiComponent.create().inject(this);
    }

    public void refresh() {
        fetchArticles();
    }

    public void fetchArticles()
    {
        newsService.getNewsArticles()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<ApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        feeds.setValue(apiResponse);
                        newsLoadError.setValue(false);
                        loading.setValue(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        newsLoadError.setValue(true);
                        loading.setValue(false);


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
                        newsLoadError.setValue(false);
                        loading.setValue(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        newsLoadError.setValue(true);
                        loading.setValue(false);
                    }
                });

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
