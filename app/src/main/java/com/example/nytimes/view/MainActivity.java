package com.example.nytimes.view;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.example.nytimes.R;
import com.example.nytimes.adapters.ArticleListAdapter;
import com.example.nytimes.data.ApiResponse;
import com.example.nytimes.data.Article;
import com.example.nytimes.viewmodel.ListViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private ListViewModel viewModel;
    private ApiResponse response;
    private ActionBar actionBar;

    @BindView(R.id.navigation)
    BottomNavigationView navigationView;

    @BindView(R.id.articleList)
    RecyclerView articleList;

    @BindView(R.id.list_error)
    TextView listError;

    @BindView(R.id.loading_view)
    ContentLoadingProgressBar loadingView;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout refreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        actionBar = getSupportActionBar();

        setUpNavigation();



        viewModel = ViewModelProviders.of(this).get(ListViewModel.class);
        ArticleListAdapter adapter = new ArticleListAdapter(new ArrayList<>(),this);

        viewModel.fetchArticles();

        articleList.setLayoutManager(new LinearLayoutManager(this));
        articleList.setAdapter(adapter);
        articleList.setVisibility(View.VISIBLE);

        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(false);
        });


        viewModel.feeds.observe(this, apiResponse -> {

            List<Article> results = apiResponse.getResults();
            Log.d("@@",results.get(1).toString());


            listError.setVisibility(View.GONE);
            loadingView.hide();
            adapter.updateArticles(results);
        });




    }

    private void setUpNavigation() {

        navigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_headline:
                    actionBar.setTitle("Headlines");
                    return true;

                case R.id.navigation_search:
                    actionBar.setTitle("Search");

                    return true;

                case R.id.navigation_category:
                    actionBar.setTitle("Category");

                    return true;

                case R.id.navigation_favourite:
                    actionBar.setTitle("Favourite");

                    return true;

                default:
                    actionBar.setTitle("Headlines");

                    return true;
            }
        });


    }
}
