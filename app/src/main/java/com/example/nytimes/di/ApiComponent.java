package com.example.nytimes.di;

import com.example.nytimes.data.api.NewsService;
import com.example.nytimes.viewmodel.ListViewModel;

import dagger.Component;

@Component(modules = {ApiModule.class})

public interface ApiComponent {

    void inject(NewsService service);
    void inject(ListViewModel viewModel);
}