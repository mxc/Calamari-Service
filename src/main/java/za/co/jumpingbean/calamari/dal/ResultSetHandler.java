/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package za.co.jumpingbean.calamari.dal;

import java.sql.ResultSet;

/**
 * This is the interface that must be implemented by clients of the JDBCHelper class
 *
 * @author mark
 */
public interface ResultSetHandler<E> {
    /**
     * The resultset, which will be the result of the SQL passed to the method in JDBCHelper, will be provided by the JDBCHelper class.
     *  This method allows the client to process the results of the query and pass back an object for use in the client code.
     * 
     * @param rs
     * @return
     * @throws DBException
     */
    public E handleResultSet(ResultSet rs) throws DBException;
}
