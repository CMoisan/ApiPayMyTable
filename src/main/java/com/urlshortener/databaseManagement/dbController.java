package com.urlshortener.databaseManagement;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.urlshortener.classOP.Url;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;



//Ameliorer en faisant en sorte que la db charge tout au début et que les recherches se fassent sur la copie locale. Tout changement se fait en ligne et en locale
public class dbController {

    private static final String username = "user";
    private static final String password = "password12345";
    private static final String databaseName = "shortUrl";
    private static final String host = "cluster0.neo6l.mongodb.net";

    public MongoClient mongoClient;
    public MongoTemplate database;
    public List<Url> Datas;

    public dbController(){
        ConnectionString connectionString = new ConnectionString("mongodb+srv://"+username+":"+password+"@"+host+"/"+databaseName+"?retryWrites=true&w=majority");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        mongoClient = MongoClients.create(settings);
        //MongoDatabase database = mongoClient.getDatabase(databaseName);

        database = new MongoTemplate(mongoClient, databaseName);
        Datas = getAll();
    }

    public Url add(Url url){
        database.save(url, "URLs");
        Datas = getAll();
        return url;
    }
    public Url delete(Url url){
        database.remove(url, "URLs");
        Datas = getAll();
        return url;
    }
    public Url change(String id,String newUrl){
        Url url = get(id);
        url.setUrl(newUrl);
        add(url);
        return url;
    }
    public Url get(String id){
        List<Url> Datas = getAll();
        Url retour = null;
        for(Url u : Datas){
            if(id.equals(String.valueOf(u.getId()))){
                retour = u;
            }
        }
        return retour;
    }

    public Url getFromUrl(String url){
        List<Url> Datas = getAll();
        Url retour = null;
        for(Url u : Datas){
            if(url.equals(u.getUrl())){
                retour = u;
            }
        }
        return retour;
    }

    public Url request(String short_url){
        List<Url> Datas = getAll();
        Url retour = null;
        for(Url u : Datas){
            String viewer = short_url.substring(short_url.length() - 5);
            if(short_url.equals(u.getShortUrl().substring(u.getShortUrl().length() - 5))){
                retour = u;
            }
        }
        return retour;
    }
    public List<Url> getAll(){
        return database.findAll(Url.class);
    }

}
