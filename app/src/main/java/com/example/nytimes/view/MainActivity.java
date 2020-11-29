package com.example.nytimes.view;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nytimes.R;
import com.example.nytimes.adapters.ArticleListAdapter;
import com.example.nytimes.adapters.SearchListAdapter;
import com.example.nytimes.data.ApiResponse;
import com.example.nytimes.data.Article;
import com.example.nytimes.data.Docs;
import com.example.nytimes.data.SearchArticle;
import com.example.nytimes.utils.Util;
import com.example.nytimes.viewmodel.ListViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.internal.Utils;

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
        SearchListAdapter searchadapter = new SearchListAdapter(new ArrayList<>(),this);

        viewModel.fetchArticles();



        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(false);
        });


        viewModel.feeds.observe(this, apiResponse -> {

            articleList.setLayoutManager(new LinearLayoutManager(this));
            articleList.setAdapter(adapter);
            articleList.setVisibility(View.VISIBLE);

            List<Article> results = apiResponse.getResults();
            Log.d("@@",results.get(1).toString());


            listError.setVisibility(View.GONE);
            loadingView.hide();
            adapter.updateArticles(results);
        });

        viewModel.searchfeeds.observe(this, searchApiResponse -> {

            articleList.setLayoutManager(new LinearLayoutManager(this));
            articleList.setAdapter(searchadapter);
            articleList.setVisibility(View.VISIBLE);

            List<SearchArticle> results = searchApiResponse.getResponse().getDocs();
            Log.d("@@",results.get(1).toString());


            listError.setVisibility(View.GONE);
            loadingView.hide();
            searchadapter.updateArticles(results);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_search_black_24dp);
        drawable.setTint(ContextCompat.getColor(this, R.color.white));
        item.setIcon(drawable);

        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Util.showKeyboard(getWindow().getDecorView().getRootView());
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        });

        SearchView searchView = new SearchView(getBaseContext());
        searchView.setBackgroundColor(Color.WHITE);
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (Util.hasNetwork()) {
                    viewModel.fetchSearchArticles(query);
                    Util.hideKeyboard(getWindow().getDecorView().getRootView());
                } else {
                    Toast.makeText(getBaseContext(), "Network not available!", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        item.setActionView(searchView);

        return true;

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
