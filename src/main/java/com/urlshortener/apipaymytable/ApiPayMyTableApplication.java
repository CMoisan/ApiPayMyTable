package com.urlshortener.apipaymytable;

import com.urlshortener.classOP.Url;
import com.urlshortener.databaseManagement.dbController;
import com.urlshortener.databaseManagement.tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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

@SpringBootApplication
@RestController
public class ApiPayMyTableApplication {
    @Autowired
    Environment environment;

    private static dbController database;
    private static List<Url> Datas;
    public static void main(String[] args) {
        database = new dbController();
        Datas = database.Datas;
        SpringApplication.run(ApiPayMyTableApplication.class, args);
    }

    //Recupère une shorturl a partir d'un id
    @GetMapping("/short_url/{id}")
    Url one(@PathVariable String id) {
        Url u = database.get(id);
        //Vérifie si la shorturl existe avec cette did
        if(u != null){
            //Si oui la renvoie
            return u;
        }else{
            //Si non erreur 404
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Url not working"
            );
        }
    }

    //Verifie que l'url fonctionne et que la short url existe encore
    @PutMapping("/short_url/{id}")
    Url change(@PathVariable String id, @RequestBody String newUrl) throws IOException {
        Url u1 = database.get(id);
        //Verifie que la short url existe et que la nouvelle url fonctionne
        if(u1 != null && checkIfUrlWorks(newUrl)){
            //Si oui modifie l'objet
            return database.change(id, newUrl);
        }
        else{
            //Si non throw une erreur 404 --> Piste d'amélioration séparer les deux
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Url not working"
            );
        }
    }

    //Delete l'url ou renvoi un 404 so l'url n'existe pas
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
    @PostMapping("/short_url")
    Url newShortUrl(@RequestBody String Url) throws IOException {
        Url u1 = database.getFromUrl(Url);
        //Verification si l'url fonctionne et si il existe déja une short url lié à cette short url
        if(u1 == null && checkIfUrlWorks(Url)) {
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
                    "http://"+InetAddress.getLocalHost().getHostAddress()+":"+environment.getProperty("local.server.port")+"/"+tools.getSaltString()));
        }else{
            //Si il existe déja un short url lié a cette url on la renvoie
            return u1;
        }
    }
    //Redirect View qui va renvoyer l'Url lié a la short URL si celle-ci existe
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

    public boolean checkIfUrlWorks(String url) throws IOException {
        URL u = new URL(url);
        HttpURLConnection huc = (HttpURLConnection) u.openConnection();
        huc.setRequestMethod("GET");
        try {
            huc.connect();
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Url not working"
            );
        }
        return true;
    }

}
