package com.example.nytimes.data.db;


import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.nytimes.data.Article;

import lombok.Data;

@Data
@Entity(tableName = "favourites")
public class Favourite {

    @PrimaryKey(autoGenerate = true)
    public int idFavourite;

    @Embedded(prefix = "article")
    public Article articles;
}

