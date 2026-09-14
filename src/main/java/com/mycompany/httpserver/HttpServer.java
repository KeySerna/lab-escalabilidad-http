package com.mycompany.httpserver;

import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;

public class HttpServer {

    public static void main(String[] args) throws IOException, URISyntaxException {
        int port = 35000;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Puerto inválido, usando 35000 por defecto.");
            }
        }
        ServerSocket serverSocket = new ServerSocket(port);
        boolean running = true;
        while (running) {
            System.out.println("Ready to receive...");
            Socket clientSocket = serverSocket.accept();

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            boolean isFirstLine = true;
            String reqMethod = "";
            String reqURIStr = "";
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (isFirstLine) {
                    String[] requestParts = inputLine.split(" ");
                    reqMethod = requestParts[0];
                    reqURIStr = requestParts[1];
                    System.out.println("Requested: " + reqMethod + " " + reqURIStr);
                    isFirstLine = false;
                }
                if (!in.ready()) {
                    break;
                }
            }

            URI reqURI = new URI(reqURIStr);
            String path = reqURI.getPath();
            String queryStr = reqURI.getQuery();

            String output;

            if (!reqMethod.equals("GET")) {
                output = "HTTP/1.1 405 Method Not Allowed\r\n"
                        + "Content-Type: text/plain\r\n\r\n"
                        + "Método no soportado";
            } else if (path.equals("/health")) {
                output = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: application/json\r\n\r\n"
                        + "{\"status\":\"ok\"}";
            } else if (path.equals("/server-time")) {
                output = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: application/json\r\n\r\n"
                        + "{\"time\":\"" + LocalDateTime.now() + "\"}";
            } else if (path.equals("/greeting")) {
                String name = getQueryParam(queryStr, "name");
                if (name == null || name.isEmpty()) {
                    output = "HTTP/1.1 400 Bad Request\r\n"
                            + "Content-Type: application/json\r\n\r\n"
                            + "{\"error\":\"Falta el parámetro 'name'\"}";
                } else {
                    output = "HTTP/1.1 200 OK\r\n"
                            + "Content-Type: application/json\r\n\r\n"
                            + "{\"greeting\":\"Hello, " + escapeJson(name) + "!\"}";
                }
            } else if (path.equals("/square")) {
                String valueStr = getQueryParam(queryStr, "value");
                Double value = null;
                if (valueStr != null) {
                    try {
                        value = Double.parseDouble(valueStr);
                    } catch (NumberFormatException e) {
                        value = null;
                    }
                }
                if (value == null) {
                    output = "HTTP/1.1 400 Bad Request\r\n"
                            + "Content-Type: application/json\r\n\r\n"
                            + "{\"error\":\"Falta o es inválido el parámetro 'value'\"}";
                } else {
                    double result = value * value;
                    output = "HTTP/1.1 200 OK\r\n"
                            + "Content-Type: application/json\r\n\r\n"
                            + "{\"input\":" + value + ",\"square\":" + result + "}";
                }
            } else {
                String resourcePath = path.equals("/") ? "/index.html" : path;

                if (resourcePath.contains("..")) {
                    output = "HTTP/1.1 400 Bad Request\r\n"
                            + "Content-Type: text/plain\r\n\r\n"
                            + "Ruta no permitida";
                } else {
                    File file = new File("public" + resourcePath);
                    if (file.exists() && file.isFile()) {
                        byte[] fileBytes = Files.readAllBytes(file.toPath());
                        String contentType = "application/octet-stream";
                        if (resourcePath.endsWith(".html")) {
                            contentType = "text/html";
                        } else if (resourcePath.endsWith(".js")) {
                            contentType = "application/javascript";
                        } else if (resourcePath.endsWith(".png")) {
                            contentType = "image/png";
                        } else if (resourcePath.endsWith(".jpg") || resourcePath.endsWith(".jpeg")) {
                            contentType = "image/jpeg";
                        }

                        String header = "HTTP/1.1 200 OK\r\n"
                                + "Content-Type: " + contentType + "\r\n"
                                + "Content-Length: " + fileBytes.length + "\r\n\r\n";

                        OutputStream rawOut = clientSocket.getOutputStream();
                        rawOut.write(header.getBytes());
                        rawOut.write(fileBytes);
                        rawOut.flush();
                        output = null;
                    } else {
                        output = "HTTP/1.1 404 Not Found\r\n"
                                + "Content-Type: text/plain\r\n\r\n"
                                + "Recurso no encontrado";
                    }
                }
            }

            if (output != null) {
                out.println(output);
            }

            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    private static String getQueryParam(String queryStr, String key) {
        if (queryStr == null) {
            return null;
        }
        String[] pairs = queryStr.split("&");
        for (String pair : pairs) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2 && kv[0].equals(key)) {
                try {
                    return URLDecoder.decode(kv[1], "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    return kv[1];
                }
            }
        }
        return null;
    }

    private static String escapeJson(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
