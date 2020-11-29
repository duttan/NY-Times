package com.example.nytimes.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nytimes.R;
import com.example.nytimes.data.Multimedia;
import com.example.nytimes.data.Results;
import com.example.nytimes.utils.ImageUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ArticleViewHolder> {

    private List<Results> newsarticles;

    public ArticleListAdapter(List<Results> articles) {
        this.newsarticles = articles;
    }

    public void updateArticles(List<Results> updatedarticles) {
        newsarticles.clear();
        newsarticles.addAll(updatedarticles);
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

        @BindView(R.id.favourite_icon)
        CheckBox likebutton;

        @BindView(R.id.share_button)
        ImageButton sharebutton;

        private List<Multimedia> imageurls;
        private String image;


        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Results results) {
            imageurls = results.getMultimedia();

            if(imageurls == null) { image = "";
            } else {
               image = imageurls.get(1).getUrl().isEmpty() ? "" : imageurls.get(1).getUrl();
            }

            description.setText(results.getTitle());
            ImageUtil.loadImage(newsimage,image);
            timestamp.setText(results.getSection() + " | Published: "+results.getUpdated_date());

        }
    }
}
