package com.mycompany.httpserver;

/**
 *
 * @author keysi
 */
import java.net.*;
import java.io.*;
import java.nio.file.Files;

public class HttpServer {

    public static void main(String[] args) throws IOException, URISyntaxException {
        ServerSocket serverSocket = new ServerSocket(35000);
        Boolean running = true;
        while (running) {
            System.out.println("Ready to receive...");
            Socket clientSocket = serverSocket.accept();

            PrintWriter out = new PrintWriter(
                    clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            boolean isFirstLine = true;
            String reqURIStr = "";
            String reqMethod = "";
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (isFirstLine) {
                    String[] requestParts = inputLine.split(" ");
                    reqMethod = requestParts[0];
                    reqURIStr = requestParts[1];
                    System.out.println("Requested URI: " + reqURIStr);
                    isFirstLine = false;
                }
                System.out.println("Received: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }

            String output = "";
            URI reqURI = new URI(reqURIStr);
            String path = reqURI.getPath();
            String queryStr = reqURI.getQuery();

            if (reqMethod.equals("GET") && path.equals("/hello")) {
                System.out.println("Query str: " + queryStr);
                output = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: text/html\r\n\r\n"
                        + "{\"response\":\"Hello world. " + queryStr + "\" }";
            } else if (reqMethod.equals("POST") && path.equals("/hellopost")) {
                System.out.println("Query str: " + queryStr);
                output = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: text/html\r\n\r\n"
                        + "{\"response\":\"Hello world (via POST). " + queryStr + "\" }";
            } else {
                File file = new File("public" + path);
                if (file.exists() && file.isFile()) {
                    byte[] fileBytes = Files.readAllBytes(file.toPath());
                    String contentType = "application/octet-stream";
                    if (path.endsWith(".html")) contentType = "text/html";
                    else if (path.endsWith(".js")) contentType = "application/javascript";
                    else if (path.endsWith(".png")) contentType = "image/png";
                    else if (path.endsWith(".jpg") || path.endsWith(".jpeg")) contentType = "image/jpeg";

                    String header = "HTTP/1.1 200 OK\r\n"
                            + "Content-Type: " + contentType + "\r\n"
                            + "Content-Length: " + fileBytes.length + "\r\n\r\n";

                    OutputStream rawOut = clientSocket.getOutputStream();
                    rawOut.write(header.getBytes());
                    rawOut.write(fileBytes);
                    rawOut.flush();
                    output = null;
                } else {
                    output = "HTTP/1.1 200 OK\r\n"
                            + "Content-Type: text/html\r\n\r\n"
                            + "<!DOCTYPE html>\n"
                            + "<html>\n"
                            + "    <head>\n"
                            + "        <title>Form Example</title>\n"
                            + "        <meta charset=\"UTF-8\">\n"
                            + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                            + "    </head>\n"
                            + "    <body>\n"
                            + "        <h1>Form with GET</h1>\n"
                            + "        <form action=\"/hello\">\n"
                            + "            <label for=\"name\">Name:</label><br>\n"
                            + "            <input type=\"text\" id=\"name\" name=\"name\" value=\"John\"><br><br>\n"
                            + "            <input type=\"button\" value=\"Submit\" onclick=\"loadGetMsg()\">\n"
                            + "        </form> \n"
                            + "        <div id=\"getrespmsg\"></div>\n"
                            + "\n"
                            + "        <script>\n"
                            + "            function loadGetMsg() {\n"
                            + "                let nameVar = document.getElementById(\"name\").value;\n"
                            + "                const xhttp = new XMLHttpRequest();\n"
                            + "                xhttp.onload = function() {\n"
                            + "                    document.getElementById(\"getrespmsg\").innerHTML =\n"
                            + "                    this.responseText;\n"
                            + "                }\n"
                            + "                xhttp.open(\"GET\", \"/hello?name=\"+nameVar);\n"
                            + "                xhttp.send();\n"
                            + "            }\n"
                            + "        </script>\n"
                            + "\n"
                            + "        <h1>Form with POST</h1>\n"
                            + "        <form action=\"/hellopost\">\n"
                            + "            <label for=\"postname\">Name:</label><br>\n"
                            + "            <input type=\"text\" id=\"postname\" name=\"name\" value=\"John\"><br><br>\n"
                            + "            <input type=\"button\" value=\"Submit\" onclick=\"loadPostMsg(postname)\">\n"
                            + "        </form>\n"
                            + "        \n"
                            + "        <div id=\"postrespmsg\"></div>\n"
                            + "        \n"
                            + "        <script>\n"
                            + "            function loadPostMsg(name){\n"
                            + "                let url = \"/hellopost?name=\" + name.value;\n"
                            + "\n"
                            + "                fetch (url, {method: 'POST'})\n"
                            + "                    .then(x => x.text())\n"
                            + "                    .then(y => document.getElementById(\"postrespmsg\").innerHTML = y);\n"
                            + "            }\n"
                            + "        </script>\n"
                            + "    </body>\n"
                            + "</html>";
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
}