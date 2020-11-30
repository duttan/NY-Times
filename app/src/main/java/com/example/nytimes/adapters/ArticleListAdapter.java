package com.example.nytimes.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nytimes.R;
import com.example.nytimes.data.Multimedia;
import com.example.nytimes.data.Article;
import com.example.nytimes.data.db.AppDatabase;
import com.example.nytimes.data.db.Favourite;
import com.example.nytimes.utils.Util;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ArticleViewHolder> {

    private List<Article> newsarticles;
    private Context context;
    private AppDatabase database;

    public ArticleListAdapter(List<Article> articles, Context context, AppDatabase db) {
        this.newsarticles = articles;
        this.context = context;
        this.database = db;
    }

    public void updateArticles(List<Article> updatedarticles) {
        newsarticles.clear();
        newsarticles.addAll(updatedarticles);
        notifyDataSetChanged();
    }

    public void setArticlesList(final List<Article> newList) {
        if (newsarticles == null) {
            newsarticles = newList;
            notifyItemRangeInserted(0, newList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return newsarticles.size();
                }

                @Override
                public int getNewListSize() {
                    return newList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return newsarticles.get(oldItemPosition).getUrl()
                            .equals(newList.get(newItemPosition).getUrl());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return newsarticles.get(oldItemPosition)
                            .equals(newList.get(newItemPosition));
                }
            });
            newsarticles = newList;
            result.dispatchUpdatesTo(ArticleListAdapter.this);
        }
    }

    public void clearRecyclerView(){
        newsarticles.clear();
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_headline, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {

        holder.configureViewHolder(holder,newsarticles.get(position));
        holder.bind(newsarticles.get(position));

    }

    @Override
    public int getItemCount() {
       return newsarticles.size();
    }


    class ArticleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_news)
        ImageView newsimage;

        @BindView(R.id.description)
        TextView description;

        @BindView(R.id.news_time)
        TextView timestamp;

        @BindView(R.id.sourcetxt)
        TextView source;

        @BindView(R.id.favourite_icon)
        CheckBox likebutton;

        @BindView(R.id.share_button)
        ImageButton sharebutton;

        private List<Multimedia> imageurls;
        private String image;
        private String url;


        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void configureViewHolder(ArticleViewHolder viewHolder, Article currentItem) {

            viewHolder.itemView.setOnClickListener(view -> {
                url = currentItem.getUrl();
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(context.getResources().getColor(R.color.colorPrimary));
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(context, Uri.parse(url));

                Favourite favourite = new Favourite();
                favourite.articles = currentItem;
                int count = database.favoriteDao().isPresent(currentItem.getTitle());
                viewHolder.likebutton.setChecked(count == 1);
                viewHolder.likebutton.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                    if (isChecked)
                        database.favoriteDao().insertFavourite(favourite);
                    else
                        database.favoriteDao().unfavouriteNews(currentItem.getTitle());
                });

            });

            viewHolder.sharebutton.setOnClickListener(view -> {

                Util.shareNews(context, currentItem.getUrl());
            });

        }



        private void bind(Article results) {
            imageurls = results.getMultimedia();

            if(imageurls == null) { image = "";
            } else {
               image = imageurls.get(1).getUrl().isEmpty() ? "" : imageurls.get(1).getUrl(); }

            source.setText(results.getSection() !=  null ? results.getSection() : "");
            description.setText(results.getTitle() != null ? results.getTitle() : "");
            Util.loadImage(newsimage,image);
            timestamp.setText("| Published: "+Util.formatTimestamp(results.getUpdated_date()));

        }
    }
}
