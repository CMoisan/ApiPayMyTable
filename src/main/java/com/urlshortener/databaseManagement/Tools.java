package com.urlshortener.databaseManagement;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Random;

public class Tools {

    /**
     * This useful method allow us to generate a random 5 long string alphanumeric to create the Shorter Url
     * @return saltStr
     */
    public static String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 5) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    /**
     * This useful method allow us to check if the url is working
     * @param url we want to test
     * @return true -> Working//404 error -> Not Working
     * @throws IOException
     */
    public static boolean checkIfUrlWorks(String url) throws IOException {
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

    /**
     * This useful method allow us to get the ip address
     * @return the ip address
     * @throws UnknownHostException
     */
    public static String getServerAddress() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }


    /**
     * This useful method allow us to build the complete ShortUrl
     * @param serverPort
     * @return the complete ShortUrl
     * @throws UnknownHostException
     */
    public static String buildFullLink(String serverPort) throws UnknownHostException {
        return "http://"+getServerAddress()+":"+serverPort+"/";
    }
}
