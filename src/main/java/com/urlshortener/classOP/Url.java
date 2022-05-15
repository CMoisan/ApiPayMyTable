package com.urlshortener.classOP;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "URLs")
public class Url {
    @Id
    private int id;
    @Field(value="Url")
    private String Url;
    @Field(value="ShortUrl")
    private String ShortUrl;

    public Url(int id, String Url, String ShortUrl){
        this.id = id;
        this.ShortUrl = ShortUrl;
        this.Url = Url;
    }

    public void setUrl(String Url){
        this.Url = Url;
    }

    public String getUrl(){
        return this.Url;
    }

    public void setShortUrl(String ShortUrl){
        this.ShortUrl = ShortUrl;
    }

    public String getShortUrl(){
        return this.ShortUrl;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        id = id;
    }
}
