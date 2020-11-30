package com.example.nytimes.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.nytimes.adapters.DataConverter;
import com.google.gson.annotations.Expose;

import java.util.List;

import lombok.Data;

@Data
@Entity(tableName = "articles")
public class Article {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @Expose(serialize = false, deserialize = false)
    public long idArticle;

    @ColumnInfo(name = "news_section")
    private String section;

    @ColumnInfo(name = "news_title")
    private String title;

    @ColumnInfo(name = "news_source")
    private String source;

    @ColumnInfo(name = "news_updated_date")
    private String updated_date;

    @ColumnInfo(name = "news_url")
    private String url;

    @TypeConverters(DataConverter.class)
    @ColumnInfo(name = "news_multimedia")
    private List<Multimedia> multimedia;


    public long getIdArticle() {
        return idArticle;
    }

    public void setIdArticle(long idArticle) {
        this.idArticle = idArticle;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Multimedia> getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(List<Multimedia> multimedia) {
        this.multimedia = multimedia;
    }

}
