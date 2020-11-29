package com.example.nytimes.utils;

import android.widget.ImageView;

import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.nytimes.R;

public class Util {

    public static void loadImage(ImageView view, String url) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_insert_photo_black_24dp)
                .fallback(R.drawable.placeholder_thumb)
                .error(R.drawable.placeholder_thumb)
                .diskCacheStrategy(DiskCacheStrategy.NONE);

        Glide.with(view.getContext())
                .setDefaultRequestOptions(options)
                .load(url)
                .into(view);
    }

    public static String formatTimestamp(String timestamp)
    {
        String date = timestamp.substring(0,10);
        String time = timestamp.substring(11,19);
        return date+" "+time;
    }

}
