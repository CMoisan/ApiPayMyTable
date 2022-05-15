package com.urlshortener.apipaymytable;

import com.urlshortener.classOP.Url;
import com.urlshortener.databaseManagement.UrlRepository;
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
public class ApiPayMyTableApplication {
    /**
     * The goal here is start the Spring Boot Application
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(ApiPayMyTableApplication.class, args);
    }
}
