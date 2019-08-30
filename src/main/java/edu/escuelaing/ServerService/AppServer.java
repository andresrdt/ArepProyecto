/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.escuelaing.ServerService;

import edu.escuelaing.Apps.media;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 2112076
 */
public class AppServer {

    public AppServer() {
    }
    /**
     * Carga las clases de las Apps
     */
    public void inicializar(){
        try{
            Class c=Class.forName("edu.escuelaing.Apps.media");
            System.out.println(c.getDeclaredAnnotation(c));
            Method main = c.getDeclaredMethod("pagina",null);
            System.out.println(main.invoke(null,null));
        } catch (Exception ex) {
            Logger.getLogger(AppServer.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    /**
     * El servidor empezara a escuchar peticiones por el puerto
     */
    /*public void escuchar(){
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        Socket clientSocket = null;
        while (true) {
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
        }
    }*/
}
