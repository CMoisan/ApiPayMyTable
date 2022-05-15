package com.urlshortener.apipaymytable;

import com.urlshortener.classOP.Url;
import com.urlshortener.databaseManagement.UrlRepository;
import com.urlshortener.databaseManagement.tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.List;

@RestController
public class UrlController {

    @Autowired
    Environment environment;

    private static UrlRepository database = new UrlRepository();
    private static List<Url> Datas = database.Datas;

    /**
     * Here we look for a ShortUrl from an id
     * @param id
     * @return the right ShortUrl or a 404 error if the ShortUrl is missing
     */
    @GetMapping("/short_url/{id}")
    Url one(@PathVariable String id) {
        Url u = database.get(id);
        //We check if the ShortUrl exist with this Id
        if(u != null){
            //If it exist we send it back
            return u;
        }else{
            //If not we throw a 404 error
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Url not working"
            );
        }
    }

    /**
     * Here we can change the URL of a ShortUrl with an ID
     * @param id
     * @param newUrl
     * @return the ShortUrl with the new Url or a 404 error
     * @throws IOException
     */
    @PutMapping("/short_url/{id}")
    Url change(@PathVariable String id, @RequestBody String newUrl) throws IOException {
        Url u1 = database.get(id);
        //We check if the ShortUrl exist in the database and if the newUrl if functionnal
        if(u1 != null && tools.checkIfUrlWorks(newUrl)){
            //If yes we change the Url
            return database.change(id, newUrl);
        }
        else{
            //If no it throw a 404 error, we could try to improve that by separating the not working url error
            // and the missing ShortUrl in the database
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Url not working"
            );
        }
    }

    /**
     * Here we delete the ShortUrl in the database from an id
     * @param id
     * @return the deleted ShortUrl or a 404 error if the ShortUrl is missing
     */
    @DeleteMapping("/short_url/{id}")
    Url delete(@PathVariable String id) {
        Url u = database.get(id);
        //vérifie si le short url existe
        if(u!=null) {
            //Si oui le supprime
            return database.delete(u);
        }else {
            //Si non throw une erreur 404
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Url not working"
            );
        }
    }

    //Créer une nouvelle short url si il n'en n'existe pas déja de lié pour cette url ou renvoie celle déja lié/ Il verifie aussi que le lien fonctionne
    /**
     * @param Url
     * @return
     * @throws IOException
     */
    @PostMapping("/short_url")
    Url newShortUrl(@RequestBody String Url) throws IOException {
        Url u1 = database.getFromUrl(Url);
        //Verification si l'url fonctionne et si il existe déja une short url lié à cette short url
        if(u1 == null && tools.checkIfUrlWorks(Url)) {
            //Génération du shorturl
            String shorturl = tools.getSaltString();
            //Vérification pour éviter les doublons
            while (database.request(shorturl) != null) {
                shorturl = tools.getSaltString();
            }
            String url = tools.getSaltString();
            //Ajout à la BDD en écrivant l'url dynamiquement
            return database.add(new Url(Datas.size(),
                    Url,
                    "http://"+ InetAddress.getLocalHost().getHostAddress()+":"+environment.getProperty("local.server.port")+"/"+tools.getSaltString()));
        }else{
            //Si il existe déja un short url lié a cette url on la renvoie
            return u1;
        }
    }
    //Redirect View qui va renvoyer l'Url lié a la short URL si celle-ci existe

    /**
     * @param shorturl
     * @return
     */
    @GetMapping("/{shorturl}")
    public RedirectView redirect(@PathVariable String shorturl) {
        RedirectView redirectView = new RedirectView();
        Url u = database.request(shorturl);
        if(u != null) {
            redirectView.setUrl(u.getUrl());
            return redirectView;
        }else
        {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Url not working"
            );
        }
    }
}
