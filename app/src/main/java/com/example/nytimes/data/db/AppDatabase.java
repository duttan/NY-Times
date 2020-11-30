package com.example.nytimes.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.nytimes.data.Article;


@Database(entities = {Article.class, Favourite.class}, version = 10)
public abstract class AppDatabase extends RoomDatabase {

    public abstract NewsDao newsDao();

    public abstract FavoriteDao favoriteDao();

    public static AppDatabase getAppDatabase(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "news-database")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build();
    }
}
