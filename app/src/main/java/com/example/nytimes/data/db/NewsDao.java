package com.example.nytimes.data.db;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.nytimes.data.Article;

import java.util.List;

@Dao
public interface NewsDao {

    @Query("SELECT * FROM articles")
    List<Article> newsArticles();

    @Query("SELECT * FROM articles WHERE id = :pos ")
    Article getArticle(int pos);

    @Query("SELECT * FROM articles WHERE news_title = :currentTitle")
    Article getArticleString(String currentTitle);

    @Query("SELECT COUNT(*) FROM articles")
    int getCount();

    @Query("DELETE FROM articles")
    void deleteTable();

    @Insert
    void insertAt(Article... articles);
}
