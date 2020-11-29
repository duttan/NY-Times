package com.example.nytimes.utils;

import android.widget.ImageView;

import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.nytimes.R;

public class ImageUtil {

    public static void loadImage(ImageView view, String url) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_insert_photo_black_24dp)
                .fallback(R.drawable.placeholder_thumb)
                .error(R.drawable.placeholder_thumb)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);

        Glide.with(view.getContext())
                .setDefaultRequestOptions(options)
                .load(url)
                .into(view);
    }
}
