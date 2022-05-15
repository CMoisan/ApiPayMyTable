package com.urlshortener.databaseManagement;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.urlshortener.classOP.Url;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;



//Ameliorer en faisant en sorte que la db charge tout au début et que les recherches se fassent sur la copie locale. Tout changement se fait en ligne et en locale
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
     * @param url we want to save
     * @return the url saved
     */
    public Url add(Url url){
        database.save(url, "URLs");
        return url;
    }

    /**
     * Delete a single Url Object in the database
     * @param url we want to remove
     * @return the deleted url
     */
    public Url delete(Url url){
        database.remove(url, "URLs");
        return url;
    }

    /**
     * Change the Url linked to a ShortUrl
     * @param id of the ShortUrl we want to chant to change the URL
     * @param newUrl is the Url we want to replace the current URL of the ShortUrl With
     * @return The new ShortUrl Object
     */
    public Url change(String id,String newUrl){
        Url url = get(id);
        url.setUrl(newUrl);
        add(url);
        return url;
    }

    /**
     * Get the ShortURL object from it's id
     * @param id of the ShortUrl Object we want to retrieve
     * @return the ShortUrl Object linked to the id
     */
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

    /**
     * Get the ShortURL object from it's Url
     * @param url of the ShortUrl Object we want to retrieve
     * @return the ShortUrl Object linked to the Url
     */
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

    /**
     * Get the ShortURL object from it's ShortUrl
     * @param short_url of the ShortUrl Object we want to retrieve
     * @return the ShortUrl Object linked to the ShortURL
     */
    public Url request(String short_url){
        List<Url> Datas = getAll();
        Url retour = null;
        for(Url u : Datas){
            if(u.getShortUrl().equals(short_url)){
                retour = u;
            }
        }
        return retour;
    }

    /**
     * Get all the ShortUrl Objects in the Database
     * @return a list of ShortUrl Objects
     */
    public List<Url> getAll(){
        return database.findAll(Url.class);
    }

    /**
     * Get the number of Objects in the database
     * @return the number of ShortUrl object in the database
     */
    public int size(){
        return getAll().size();
    }

}
