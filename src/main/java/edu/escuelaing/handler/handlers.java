/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.escuelaing.handler;

import java.lang.reflect.Method;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.MessageContext;

/**
 *
 * @author Andres
 */
public class handlers implements hand {

    public Method fun;

    public handlers(Method fun) {
        this.fun = fun;
    }

    public String iniciar() {
        try {
            return (String) fun.invoke(fun, null);
        } catch (Exception e) {
            return "funcion invalida";
        }
    }

    public String inicio(Object[] arg) {
        try {
            return (String) fun.invoke(fun, arg);
        } catch (Exception e) {
            return "funcion invalida";
        }
    }

}
