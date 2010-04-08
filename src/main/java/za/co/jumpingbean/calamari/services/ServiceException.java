/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package za.co.jumpingbean.calamari.services;

/**
 *
 * @author mark
 */
public class ServiceException extends Exception{

   public ServiceException(String string, Exception ex) {
        super(string,ex);
    }
   
}
