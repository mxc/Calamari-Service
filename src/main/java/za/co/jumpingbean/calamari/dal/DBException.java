/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package za.co.jumpingbean.calamari.dal;


/**
 *
 * @author mark
 */
public class DBException extends Exception {

   public DBException(String string, Exception ex) {
        super(string,ex);
    }

}
