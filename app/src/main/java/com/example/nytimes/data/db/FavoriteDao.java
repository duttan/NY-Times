package com.example.nytimes.data.db;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Query("SELECT * FROM favourites")
    List<Favourite> getFavourites();

    @Query("SELECT COUNT(*) FROM favourites WHERE articlenews_title = :title")
    int isPresent(String title);

    @Query("DELETE FROM favourites WHERE articlenews_title = :title")
    void unfavouriteNews(String title);

    @Insert
    void insertFavourite(Favourite... favourites);
}
