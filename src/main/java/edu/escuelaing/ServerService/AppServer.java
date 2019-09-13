/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.escuelaing.ServerService;

import edu.escuelaing.Apps.media;
import edu.escuelaing.anotaciones.webs;
import edu.escuelaing.handler.hand;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import edu.escuelaing.anotaciones.webs;
import edu.escuelaing.handler.hand;
import edu.escuelaing.handler.handlers;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Handler;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

/**
 *
 * @author 2112076
 */
public class AppServer {

    public DataOutputStream binaryOut;
    private Map<String, hand> lista;

    public AppServer() {
        lista = new HashMap<String, hand>();
    }

    /**
     * Carga las clases de las Apps
     */
    public void inicializar() {
        /*try {
            Class c = Class.forName("edu.escuelaing.Apps.media");
            System.out.println(c.getDeclaredAnnotation(c));
            Method main = c.getDeclaredMethod("pagina", null);
            System.out.println(main.invoke(null, null));
        } catch (Exception ex) {
            Logger.getLogger(AppServer.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        Reflections reflections = new Reflections("edu.escuelaing.Apps", new SubTypesScanner(false));
        Set<Class<? extends Object>> allClasses = reflections.getSubTypesOf(Object.class);

        for (Class clase : allClasses) {
            for (Method method : clase.getMethods()) {
                if (method.isAnnotationPresent(webs.class)) {
                    webs web = method.getAnnotation(webs.class);
                    hand hand = new handlers(method);
                    lista.put("/resources/" + web.value(), hand);
                }
            }
        }

    }

    /**
     * El servidor empezara a escuchar peticiones por el puerto
     */
    public void escuchar() throws Exception {
        while (true) {
            ServerSocket serverSocket = new ServerSocket(AppServer.getPort());

            System.out.println("Listo para recibir ...");
            Socket cliente = serverSocket.accept();
            while (!cliente.isClosed()) {
                PrintWriter out = new PrintWriter(
                        new OutputStreamWriter(cliente.getOutputStream(), StandardCharsets.UTF_8), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                binaryOut = new DataOutputStream(cliente.getOutputStream());
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.toLowerCase().contains("GET".toLowerCase())) {
                        if(line.toLowerCase().contains("/-".toLowerCase())){
                            String recurso="/resources/index.html";
                            enMemoria(recurso, cliente);
                        }
                        else if (line.toLowerCase().contains("/resources/".toLowerCase())) {
                            String recurso = line.split(" ")[1];
                            if (!recurso.toLowerCase().contains("?")) {
                                if (lista.containsKey(recurso)) {
                                    out.print("HTTP/1.1 200 OK \r");
                                    out.print("Content-Type: text/html \r\n");
                                    out.print("\r\n");
                                    out.print(lista.get(recurso).iniciar());
                                    out.flush();
                                } else {
                                    /*String[] ina = line.split(" ");
                                    String[] clas = ina[1].split("/");
                                    Class<?> c = Class.forName("edu.escuelaing.arem.Apps." + clas[2]);
                                    for (Method method : c.getMethods()) {
                                        webs web = method.getAnnotation(webs.class);
                                        hand hand = new handlers(method);
                                        lista.put("/resources/"+ web.value(), hand);*/
                                    enMemoria(recurso, cliente);
                            
                            //}
                                    
                                }
                            } else {
                                String recursoLocacion = recurso.substring(recurso.indexOf("/resources/"), recurso.indexOf("?"));
                                if (lista.containsKey(recursoLocacion)) {
                                    out.print("HTTP/1.1 200 OK \r");
                                    out.print("Content-Type: text/html \r\n");
                                    out.print("\r\n");
                                    out.print(lista.get(recursoLocacion).inicio(new Object[]{recurso.substring(recurso.indexOf("?") + 1)}));
                                    out.flush(); 
                                } else {
                                    enMemoria(recurso, cliente);
                                }
                            }
                        } else {
                            String recurso = line.split(" ")[1];
                            enMemoria(recurso, cliente);
                        }
                    }
                    if (!in.ready()) {
                        break;
                    }
                }
                in.close();
            }
            cliente.close();
            serverSocket.close();
        }
    }

    public String readResource(String direction) {
        String file, contentType = "";
        String resource = "";

        contentType = "text/html";
        try {
            BufferedReader bf = new BufferedReader(new FileReader(direction));
            String temp = "";
            String bfRead = "";
            while ((bfRead = bf.readLine()) != null) {
                temp = temp + bfRead;
            }
            resource = temp;

        } catch (IOException e) {
            resource = "";
            System.err.println("No se ha encontrado el archivo");

        }

        file = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: " + contentType + "\r\n"
                + "\r\n"
                + resource;

        return file;

    }

    private void getHtml(String direction, Socket client) throws Exception {
        String serverAns = readResource(direction);
        PrintWriter out;
        try {
            out = new PrintWriter(client.getOutputStream(), true);
            out.println(serverAns);

        } catch (IOException ex) {
            Logger.getLogger(AppServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public byte[] leerIma(String direction) {
        byte[] finalData = new byte[]{};

        try {
            File graphicResource = new File(direction);
            FileInputStream inputImage = new FileInputStream((graphicResource.getPath()));
            finalData = new byte[(int) graphicResource.length()];
            inputImage.read(finalData);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(AppServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.err.println("Error en la lectura de el archivo");
        }

        return finalData;

    }

    public static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567; // returns default port if heroku-port isn't set (i.e. on localhost)
    }

    private void enMemoria(String camino, Socket cliente) throws Exception {
        String path = System.getProperty("user.dir") + camino;
        try {
            if(path.toLowerCase().contains("?")){
                
            }
            else if (path.toLowerCase().contains(".html".toLowerCase())) {

                getHtml(path, cliente);
            } else if (path.toLowerCase().contains(".png".toLowerCase())) {

                byte[] serverAns = leerIma(path);
                try {
                    binaryOut = new DataOutputStream(cliente.getOutputStream());
                    binaryOut.writeBytes("HTTP/1.1 200 OK \r\n");
                    binaryOut.writeBytes("Content-Type: image/png\r\n");
                    binaryOut.writeBytes("Content-Length: " + serverAns.length);
                    binaryOut.writeBytes("\r\n\r\n");
                    binaryOut.write(serverAns);
                    binaryOut.close();
                } catch (IOException ex) {
                    Logger.getLogger(AppServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (Exception e) {
            System.out.println("recurso no existe");
        }

    }
}
