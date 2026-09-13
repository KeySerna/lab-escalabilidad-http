package com.mycompany.httpserver;

import java.net.URI;
import java.net.URL;
import java.net.URISyntaxException;
import java.io.IOException;

/**
 *
 * @author keysi
 */
public class ReadURL {
    public static void main(String[] args) throws URISyntaxException, IOException {
        URI miURI = new URI("http://servidor.com:8080/carpeta/pagina.html?param=valor#seccion");
        URL miURL = miURI.toURL();

        System.out.println("Protocolo: " + miURL.getProtocol());
        System.out.println("Authority: " + miURL.getAuthority());
        System.out.println("Host: " + miURL.getHost());
        System.out.println("Puerto: " + miURL.getPort());
        System.out.println("Path: " + miURL.getPath());
        System.out.println("Query: " + miURL.getQuery());
        System.out.println("File: " + miURL.getFile());
        System.out.println("Ref: " + miURL.getRef());
    }
}
