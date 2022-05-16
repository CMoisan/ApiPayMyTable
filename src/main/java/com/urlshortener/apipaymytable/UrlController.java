package com.urlshortener.apipaymytable;

import com.urlshortener.classOP.ShortUrlOP;
import com.urlshortener.databaseManagement.Tools;
import com.urlshortener.databaseManagement.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.net.UnknownHostException;

@RestController
public class UrlController {

    @Autowired
    Environment environment;
    private static final UrlRepository database = new UrlRepository();

    /**
     * Here we look for a ShortUrl from an id
     * @param id of the ShortUrl we want to edit
     * @return the right ShortUrl or throw 404 error if the ShortUrl is missing
     */
    @GetMapping("/short_url/{id}")
    ShortUrlOP retrieve(@PathVariable String id){
        ShortUrlOP retrievedShortUrlOP = database.get(id);
        //We check if the ShortUrl exist with this Id
        if(retrievedShortUrlOP != null){
            //If it exist we send it back
            return createFullShortUrl(retrievedShortUrlOP);
        }else{
            //If not we throw a 404 error
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Url not working"
            );
        }
    }

    /**
     * Here we can change the URL of a ShortUrl with an ID
     * @param id is the id of the ShortUrl we want to edit
     * @param newUrl is the newly Edited ShortUrl
     * @return the ShortUrl with the new Url or a 404 error
     * @throws IOException
     */
    @PutMapping("/short_url/{id}")
    ShortUrlOP change(@PathVariable String id, @RequestBody String newUrl) throws IOException {
        ShortUrlOP shortUrlOP = database.get(id);
        //We check if the ShortUrl exist in the database and if the newUrl is functional
        if(shortUrlOP != null && Tools.checkIfUrlWorks(newUrl)){
            //If yes we change the Url
            return createFullShortUrl(database.change(id, newUrl));
        }
        else{
            //If no it throw a 404 error, we could try to improve that by separating the not working url error
            // and the missing ShortUrl in the database error
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
    ShortUrlOP delete(@PathVariable String id){
        ShortUrlOP shortUrlOP = database.get(id);
        //We check if the ShortUrl exist with this Id
        if(shortUrlOP !=null) {
            //If it exist we delete it
            return createFullShortUrl(database.delete(shortUrlOP));
        }else {
            //If no it throw a 404 error
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Url not working"
            );
        }
    }

    /**
     * Here we create a new ShortUrl if there is not already one linked to the Url, if so we send back the one already linked
     * @param Url is the url we want to make shorter
     * @return an Url object with the right Url linked to it
     * @throws IOException
     */
    @PostMapping("/short_url")
    ShortUrlOP newShortUrl(@RequestBody String Url) throws IOException {
        ShortUrlOP retrievedShortUrlOP = database.getFromUrl(Url);
        //We check if the Url exist in the database and if it is functional
        if(retrievedShortUrlOP == null && Tools.checkIfUrlWorks(Url)) {
            //Generation of the ShortUrl identifier
            String shortUrlId = Tools.getSaltString();
            //Check if there is not already an Url Linked to this ShortUrl Identifier
            while (database.request(shortUrlId) != null) {
                shortUrlId = Tools.getSaltString();
            }
            String url = Tools.getSaltString();
            //We add it to the database
            return createFullShortUrl(database.add(new ShortUrlOP(database.size(), Url, shortUrlId)));

        }else{
            //If there is already a ShortUrl Linked to this Url we send it back
            return createFullShortUrl(retrievedShortUrlOP);
        }
    }

    /**
     * Here we have the service who's going to get the Url from the ShortUrl complete link and redirect us
     * @param shorturl is the identifier contained in the database which link the complete ShortUrl to the URL
     * @return Return http request to redirect the web browser to the right page
     */
    @GetMapping("/{shorturl}")
    public RedirectView redirect(@PathVariable String shorturl) {
        RedirectView redirectView = new RedirectView();
        //Retrieve the Url Object linked to the Complete ShortUrl
        ShortUrlOP shortUrlOP = database.request(shorturl);
        //We check if the Url Object exist with this ShortUrl
        if(shortUrlOP != null) {
            //If it exist we get the Url and we redirect the web browser by returning the redirectView
            redirectView.setUrl(shortUrlOP.getUrl());
            return redirectView;
        }else
        {
            //If no it throw a 404 error
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Url not working"
            );
        }
    }

    /**
     *Get an Url object we are about to send back to the client and write in the ShortUrl the
     *Complete ShortUrl(With address & port)
     * @param shortUrlOP is the url object we need to change the ShortUrl attribute
     * @return an url object with the Complete ShortUrl
     * @throws UnknownHostException
     */
    public ShortUrlOP createFullShortUrl(ShortUrlOP shortUrlOP){
        shortUrlOP.setLinkShortUrl(environment.getProperty("local.server.port"));
        return shortUrlOP;
    }
}
