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
import androidx.recyclerview.widget.RecyclerView;
import com.example.nytimes.R;
import com.example.nytimes.data.Multimedia;
import com.example.nytimes.data.SearchArticle;
import com.example.nytimes.utils.Constants;
import com.example.nytimes.utils.Util;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ArticleViewHolder> {

    private List<SearchArticle> newsarticles;
    private Context context;

    public SearchListAdapter(List<SearchArticle> articles,Context context) {
        this.newsarticles = articles;
        this.context = context;
    }

    public void updateArticles(List<SearchArticle> updatedarticles) {
        newsarticles.clear();
        newsarticles.addAll(updatedarticles);
        notifyDataSetChanged();
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



        private void configureViewHolder(ArticleViewHolder viewHolder, SearchArticle currentItem) {

            viewHolder.itemView.setOnClickListener(view -> {
                url = currentItem.getWeb_url();
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(context.getResources().getColor(R.color.colorPrimary));
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(context, Uri.parse(url));
            });

        }

        private void bind(SearchArticle results) {
            imageurls = results.getMultimedia();

            if(imageurls == null || !(imageurls.size() > 0)) { image = "";
            } else {
                image = imageurls.get(1).getUrl().isEmpty() ? "" : imageurls.get(1).getUrl(); }

            source.setText(results.getSource() !=  null ? results.getSource() : "");
            description.setText(results.getSnippet() != null ? Util.shortenText(results.getSnippet()) : "");
            Util.loadImage(newsimage, Constants.APPEND_URL_FOR_IMAGE+image);
        }
    }
}

