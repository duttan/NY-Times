package com.example.nytimes.view;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.nytimes.R;
import com.example.nytimes.adapters.ArticleListAdapter;
import com.example.nytimes.adapters.FavouriteListAdapter;
import com.example.nytimes.adapters.SearchListAdapter;
import com.example.nytimes.data.ApiResponse;
import com.example.nytimes.data.Article;
import com.example.nytimes.data.SearchArticle;
import com.example.nytimes.data.db.AppDatabase;
import com.example.nytimes.data.db.Favourite;
import com.example.nytimes.utils.Util;
import com.example.nytimes.viewmodel.ListViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private ListViewModel viewModel;
    private ApiResponse response;
    private ActionBar actionBar;
    private AppDatabase db;

    List<Article> favlistArticles = new ArrayList<>();
    ArticleListAdapter adapter;
    SearchListAdapter searchadapter;
    FavouriteListAdapter favadapter;

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
        db = AppDatabase.getAppDatabase(Objects.requireNonNull(this));

        adapter = new ArticleListAdapter(new ArrayList<>(),this,db);
        searchadapter = new SearchListAdapter(new ArrayList<>(),this);
        favadapter = new FavouriteListAdapter(new ArrayList<>(),this,db);

        actionBar = getSupportActionBar();
        setUpNavigation();
        viewModel = ViewModelProviders.of(this).get(ListViewModel.class);

        viewModel.refresh();
        loadingView.show();
        listError.setVisibility(View.GONE);
        if(!Util.hasNetwork()) {
            Toast.makeText(this,"Please check your internet connection!",Toast.LENGTH_LONG).show();
        }

        refreshLayout.setOnRefreshListener(() -> {
            if(!actionBar.getTitle().equals("Favourite") && !actionBar.getTitle().equals("Search")) {
                viewModel.refresh();
                if (!Util.hasNetwork()) {
                    Toast.makeText(this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
                }
            }
            refreshLayout.setRefreshing(false);
        });


        viewModel.feeds.observe(this, apiResponse -> {

            articleList.setLayoutManager(new LinearLayoutManager(this));
            articleList.setAdapter(adapter);
            articleList.setVisibility(View.VISIBLE);

            List<Article> results = apiResponse.getResults();

            adapter.updateArticles(results);
        });

        viewModel.searchfeeds.observe(this, searchApiResponse -> {

            articleList.setLayoutManager(new LinearLayoutManager(this));
            articleList.setAdapter(searchadapter);
            articleList.setVisibility(View.VISIBLE);

            List<SearchArticle> results = searchApiResponse.getResponse().getDocs();

            searchadapter.updateArticles(results);
        });


        viewModel.newsLoadError.observe(this, isError -> {
            if(isError != null) {
                listError.setVisibility(isError ? View.VISIBLE : View.GONE);
                if(isError){
                Toast.makeText(this,"Please check your internet connection!",Toast.LENGTH_LONG).show();}
            }
        });

        viewModel.loading.observe(this, isLoading -> {
            if(isLoading != null) {
                loadingView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                if(isLoading) {
                    listError.setVisibility(View.GONE);
                    articleList.setVisibility(View.GONE);
                }
            }
        });
    }

    private void init()
    {
        favlistArticles = getSavedNews();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(false);

        if(actionBar.getTitle() == "Search") {
            item.setVisible(true);
        }

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
                    invalidateOptionsMenu();
                    init();
                    viewModel.refresh();
                    return true;

                case R.id.navigation_search:
                    actionBar.setTitle("Search");
                    invalidateOptionsMenu();
                    adapter.clearRecyclerView();
                    return true;


                case R.id.navigation_favourite:
                    actionBar.setTitle("Favourite");
                    invalidateOptionsMenu();
                    init();
                    articleList.setAdapter(favadapter);
                    favadapter.clearRecyclerView();
                    favadapter.updateArticles(favlistArticles);
                    //adapter.setArticlesList(getSavedNews());

                    return true;

                default:
                    actionBar.setTitle("Headlines");

                    return true;
            }
        });


    }

    private List<Article> getSavedNews(){
        db = AppDatabase.getAppDatabase(this);
        List<Favourite> favouriteList = db.favoriteDao().getFavourites();
        favlistArticles.clear();
        for (int i = 0; i < favouriteList.size(); i++) {
            favlistArticles.add(favouriteList.get(i).articles);
        }
        return favlistArticles;
    }
}
