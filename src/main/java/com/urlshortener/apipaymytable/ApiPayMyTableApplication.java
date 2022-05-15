package com.urlshortener.apipaymytable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
public class ApiPayMyTableApplication {

    /**
     * The goal here is start the Spring Boot Application
     * @param args
     */
    public static void main(String[] args) throws UnknownHostException {
        SpringApplication.run(ApiPayMyTableApplication.class, args);
    }
}
