package com.example.nytimes.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.nytimes.R;
import com.example.nytimes.view.MainApplication;

public class Util {

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

    public static String formatTimestamp(String timestamp)
    {
        String date = timestamp.substring(0,10);
        String time = timestamp.substring(11,19);
        return date+" "+time;
    }

    public static String shortenText(String text)
    {
        String date = text.length() >= 75 ? text.substring(0,75) : text;
        return date+"...click to open ";
    }


    public static void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            view.clearFocus();
            if (manager != null) {
                manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public static void showKeyboard(View view){
        if(view != null){
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
            }
        }
    }

    public static boolean hasNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) MainApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
