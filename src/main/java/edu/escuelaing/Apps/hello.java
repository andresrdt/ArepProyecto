/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.escuelaing.Apps;

import edu.escuelaing.anotaciones.webs;

/**
 *
 * @author Andres
 */
public class hello {
    @webs(value="prueba")
   public static String pagina()
    {
        return "<!DOCTYPE html> <html> <head> <title> WEB </title>  </head> <body> <h1>Prueba </h1> </body> </html>";
    }
}
