package com.example.nytimes.view;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;

import com.example.nytimes.R;
import com.example.nytimes.data.ApiResponse;
import com.example.nytimes.data.Results;
import com.example.nytimes.data.api.NewsApi;
import com.example.nytimes.viewmodel.ListViewModel;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    private ListViewModel viewModel;
    public ApiResponse response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = ViewModelProviders.of(this).get(ListViewModel.class);

        viewModel.fetchArticles();


        viewModel.feeds.observe(this, apiResponse -> {

            List<Results> results = apiResponse.getResults();
            Log.d("@@",results.get(1).toString());
        });




    }
}
