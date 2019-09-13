/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.escuelaing.Apps;

import edu.escuelaing.anotaciones.webs;



/**
 *
 * @author 2112076
 */

public class media {
   @webs(value="prueba.html")
   public static String pagina()
    {
        return "<!DOCTYPE html> <html> <head> <title> WEB </title>  </head> <body> <h1>Prueba </h1> </body> </html>";
    }
    @webs(value="prueba1.html")
    public static String sumar(String n1) {
        int num = Integer.parseInt(n1);
        return "<!DOCTYPE html>"
                +"<html>" + 
                "<head/>" + 
                "<body>" + 
                "<h2> en este momento somos : "
                + Integer.toString(num)
                + "</h2>" + "</body>" + "</html>";
    }
}
