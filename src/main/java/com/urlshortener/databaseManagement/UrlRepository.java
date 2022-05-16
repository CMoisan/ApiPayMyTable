package com.urlshortener.databaseManagement;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.urlshortener.classOP.ShortUrlOP;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

public class UrlRepository {

    private static final String username = "user";
    private static final String password = "password12345";
    private static final String databaseName = "shortUrl";
    private static final String host = "cluster0.neo6l.mongodb.net";

    public MongoClient mongoClient;
    public MongoTemplate database;

    /**
     * Setup the database connexion
     */
    public UrlRepository(){
        ConnectionString connectionString = new ConnectionString("mongodb+srv://"+username+":"+password+"@"+host+"/"+databaseName+"?retryWrites=true&w=majority");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        mongoClient = MongoClients.create(settings);
        //MongoDatabase database = mongoClient.getDatabase(databaseName);

        database = new MongoTemplate(mongoClient, databaseName);
    }

    /**
     * Save a single Url object in the database
     * @param shortUrlOPToSave we want to save
     * @return the url saved
     */
    public ShortUrlOP add(ShortUrlOP shortUrlOPToSave){
        database.save(shortUrlOPToSave, "URLs");
        return shortUrlOPToSave;
    }

    /**
     * Delete a single Url Object in the database
     * @param shortUrlOPToDelete we want to remove
     * @return the deleted url
     */
    public ShortUrlOP delete(ShortUrlOP shortUrlOPToDelete){
        database.remove(shortUrlOPToDelete, "URLs");
        return shortUrlOPToDelete;
    }

    /**
     * Change the Url linked to a ShortUrl
     * @param id of the ShortUrl we want to chant to change the URL
     * @param newUrl is the Url we want to replace the current URL of the ShortUrl With
     * @return The new ShortUrl Object
     */
    public ShortUrlOP change(String id, String newUrl){
        ShortUrlOP shortUrlOP = get(id);
        shortUrlOP.setUrl(newUrl);
        add(shortUrlOP);
        return shortUrlOP;
    }

    /**
     * Get the ShortURL object from it's id
     * @param idToRetrieve of the ShortUrl Object we want to retrieve
     * @return the ShortUrl Object linked to the id
     */
    public ShortUrlOP get(String idToRetrieve){
        List<ShortUrlOP> Datas = getAll();
        ShortUrlOP retrievedShortUrlOP = null;
        for(ShortUrlOP shortUrlOPIterate : Datas){
            if(idToRetrieve.equals(String.valueOf(shortUrlOPIterate.getId()))){
                retrievedShortUrlOP = shortUrlOPIterate;
            }
        }
        return retrievedShortUrlOP;
    }

    /**
     * Get the ShortURL object from it's Url
     * @param url of the ShortUrl Object we want to retrieve
     * @return the ShortUrl Object linked to the Url
     */
    public ShortUrlOP getFromUrl(String url){
        List<ShortUrlOP> Datas = getAll();
        ShortUrlOP retrievedShortUrlOP = null;
        for(ShortUrlOP shortUrlOPIterate : Datas){
            if(url.equals(shortUrlOPIterate.getUrl())){
                retrievedShortUrlOP = shortUrlOPIterate;
            }
        }
        return retrievedShortUrlOP;
    }

    /**
     * Get the ShortURL object from it's ShortUrl
     * @param shortUrlToRetrieve of the ShortUrl Object we want to retrieve
     * @return the ShortUrl Object linked to the ShortURL
     */
    public ShortUrlOP request(String shortUrlToRetrieve){
        List<ShortUrlOP> Datas = getAll();
        ShortUrlOP retrievedShortUrlOP = null;
        for(ShortUrlOP shortUrlOPIterate : Datas){
            if(shortUrlOPIterate.getShortUrl().equals(shortUrlToRetrieve)){
                retrievedShortUrlOP = shortUrlOPIterate;
            }
        }
        return retrievedShortUrlOP;
    }

    /**
     * Get all the ShortUrl Objects in the Database
     * @return a list of ShortUrl Objects
     */
    public List<ShortUrlOP> getAll(){
        return database.findAll(ShortUrlOP.class);
    }

    /**
     * Get the number of Objects in the database
     * @return the number of ShortUrl object in the database
     */
    public int size(){
        return getAll().size();
    }

}
