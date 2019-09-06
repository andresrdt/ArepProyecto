package edu.escuelaing.arem;

import edu.escuelaing.ServerService.AppServer;

/**
 * Hello world!
 *
 */
public class Controlador 
{
    public static void main( String[] args ) throws Exception
    {
        AppServer ap=new AppServer();
        ap.inicializar();
        ap.escuchar();
    }
    
}
